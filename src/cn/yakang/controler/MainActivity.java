package cn.yakang.controler;


import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.content.res.XmlResourceParser;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import cn.yakang.controler.adapter.MainViewPagerAdapter;
import cn.yakang.controler.entity.CommanUrl;
import cn.yakang.controler.entity.command.ICommand;
import cn.yakang.controler.entity.handleObj.IHandleObj;
import cn.yakang.controler.entity.handleObj.Module;
import cn.yakang.controler.frag.ControlFragment;
import cn.yakang.controler.frag.H1Fragment;
import cn.yakang.controler.frag.H2Fragment;
import cn.yakang.controler.mq.ISender;
import cn.yakang.controler.mq.ISender.ConnectionListener;
import cn.yakang.controler.mq.RabbitMQSender;
import cn.yakang.controler.queryTask.GetHTask;
import cn.yakang.controler.queryTask.GetHTask.HResultListener;
import cn.yakang.controler.queryTask.IQueryTask;
import cn.yakang.controler.queryTask.IQueryTask.OnResultListener;
import cn.yakang.controler.queryTask.IQueryTask.QueryType;
import cn.yakang.controler.queryTask.QueryTask;
import cn.yakang.controler.util.ToastUtil;
import cn.yakang.controler.util.net.HttpUtil;
import cn.yakang.controler.util.net.NetworkTools;
import cn.yakang.controler.widget.slidingMenu.SlidingMenu;

public class MainActivity extends BaseActivity implements OnClickListener,
		ConnectionListener ,OnResultListener,HResultListener{
	private SlidingMenu sm;
	private ViewPager viewPager;
	private MainViewPagerAdapter adapter;
	private RadioGroup navigationRg;
	private TextView serverStateTv,mqServerStateTv,deviceStateTv;
	
	private ISender sender;
	private MyHandler mHandler;
	private boolean isNetworkAvailable;
	
	private long time;
	private long currentTime;
	private long interval = 3 * 1000;
	
	private static final int TEST_SERVER = 1;
	private static final int CONNECT_MQ = 2;
	
	private List<Module> modules;
	private static final int MODULE_QUERY = 1;
	private static final int HIERARCHY_QUERY = 2;
	
	private IQueryTask task;
	private GetHTask hTask;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_main);
		
		task = new QueryTask(this);
		hTask = new GetHTask(this);
		task.setOnResultListener(this);
		hTask.setOnResultListener(this);
		
		
		initView();
		initSeconderySlidingMenu();
		initSender();
		mHandler = new MyHandler(this);
		testServerState();
		registConnectionReceiver();
		query(MODULE_QUERY,new String[]{CommanUrl.BASE,CommanUrl.MODULE});
	}
	
	/**
	 * 下载或加载模块配置文件，返回模块信息
	 * @param what
	 * @param url
	 */
	private void query(int what,String...params) {
		task.doQueryTask(what, QueryType.Xml, params);
	}
	
	/**
	 * 下载或加载模块详细信息，返回层级信息
	 * @param params
	 */
	private void startHQuery(String...params){
		hTask.doQueryTask(params);
	}

	/**
	 * 网络连接状态改变广播
	 */
	private void registConnectionReceiver() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		registerReceiver(networkStateReceiver, filter);
	}

	/**
	 * 初始化消息发送对象
	 */
	public void initSender() {
		SharedPreferences sharePre = getSharedPreferences("serverInfo",
				MODE_PRIVATE);
		String server = sharePre.getString("server", "192.168.0.209");
		int port = sharePre.getInt("port", 5672);
		sender = new RabbitMQSender(server, port, "guest", "guest");
		sender.setConnectionListener(this);
		sender.connectToServer();
	}
	
	/**
	 * 测试服务器状态
	 */
	private void testServerState(){
		new Thread(new Runnable() {
			@Override
			public void run() {
				boolean b = HttpUtil.testServerState("http://192.168.0.216:8080");
				Message msg = mHandler.obtainMessage();
				msg.what = TEST_SERVER;
				msg.obj = b;
				mHandler.sendMessage(msg);
			}
		}).start();
	}

	/**
	 * 发送消息
	 * @param command
	 */
	public void sendCommand(ICommand command) {
		if (isNetworkAvailable) {
			sender.send(command);
		}else{
			time = currentTime;
			currentTime = System.currentTimeMillis();
			if (!isNetworkAvailable && (currentTime - time) > interval) {
				ToastUtil.showToast(this, "未开启网络连接！");
			}
		}
	}
	
	/**
	 * 初始化模块（底部导航）
	 * 调用startHQuery();
	 * @param modules
	 */
	private void initModule(List<Module> modules) {
		viewPager = (ViewPager) this.findViewById(R.id.noScrollViewpager1);
		for(int i = 0 ; i < modules.size() ; i++){
			Module module = modules.get(i);
			String url = module.getId();
			startHQuery(new String[]{CommanUrl.BASE,url,i+""});
			addRadioButton(i,module.getName());
		}
		navigationRg.check(0);
	}
	
	/**
	 * 向RadioGroup中动态添加RadioButton;
	 * @param id
	 * @param text
	 */
	private void addRadioButton(int id,String text){
		RadioButton rb = new RadioButton(this);
		RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.MATCH_PARENT,1);
		rb.setBackgroundResource(R.drawable.common_topbar_home_selector);
		rb.setTextSize(getResources().getDimensionPixelSize(R.dimen.text_size_one));
		try {
		    XmlResourceParser parser = getResources().getXml(R.color.navigation_text);
		    ColorStateList colors = ColorStateList.createFromXml(getResources(), parser);
		    rb.setTextColor(colors);
		} catch (Exception e) {
		    // handle exceptions
		}
		rb.setPadding(0, 10, 0, 10);
		Bitmap bitmap = null;
		rb.setButtonDrawable(new BitmapDrawable(bitmap));
		rb.setGravity(Gravity.CENTER);
		rb.setText(text);
		rb.setId(id);
		navigationRg.addView(rb,params);
	}
	
	public void notifyDataSetChange(){
		adapter.notifyDataSetChanged();
	}

	private void initSeconderySlidingMenu() {
		sm = getSlidingMenu();
		sm.setMode(SlidingMenu.LEFT_RIGHT);
		sm.setSecondaryMenu(R.layout.menu_frame_two);
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.menu_control, new ControlFragment()).commit();
		sm.setSecondaryShadowDrawable(R.drawable.shadowright);
		sm.setShadowDrawable(R.drawable.shadow);
	}

	private void initView() {
		this.findViewById(R.id.setting).setOnClickListener(this);
		this.findViewById(R.id.control).setOnClickListener(this);
		navigationRg = (RadioGroup) this.findViewById(R.id.navigation);
		navigationRg.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				viewPager.setCurrentItem(checkedId,false);
			}
		});
		serverStateTv = (TextView) this.findViewById(R.id.server_state_tv);
		serverStateTv.setText("正在检测服务器");
		mqServerStateTv = (TextView) this.findViewById(R.id.mq_server_state_tv);
		mqServerStateTv.setText("正在检测消息服务器");
		deviceStateTv = (TextView) this.findViewById(R.id.device_state_tv);
		deviceStateTv.setText("");
	}

	private static class MyHandler extends Handler {
		private WeakReference<MainActivity> ref;

		public MyHandler(MainActivity act) {
			ref = new WeakReference<MainActivity>(act);
		}

		@Override
		public void handleMessage(Message msg) {
			MainActivity act = ref.get();
			if (act != null) {
				switch(msg.what){
				case CONNECT_MQ:
					String text = (Boolean) msg.obj ? "消息服务器已连接" : "消息服务器未连接";
					act.mqServerStateTv.setText(text);
				break;
				case TEST_SERVER:
					String result = (Boolean)msg.obj ? "服务器正常" : "服务器异常";
					act.serverStateTv.setText(result);
					break;
				}
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.setting:
			sm.showMenu();
			break;
		case R.id.control:
			sm.showSecondaryMenu();
			break;
		}
	}

	/**
	 * 判断网络连接是否可用
	 * @return
	 */
	public boolean isNetworkConnect() {
		boolean flag = NetworkTools.isNetworkConnected(this);
		time = currentTime;
		currentTime = System.currentTimeMillis();
		if (!flag && (currentTime - time) > interval) {
			ToastUtil.showToast(this, "未开启网络连接！");
		}
		return flag;
	}
	
	private BroadcastReceiver networkStateReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			// 获得网络连接服务
			ConnectivityManager connManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo info = connManager.getActiveNetworkInfo();
			if (info != null && info.isAvailable()) { 
				isNetworkAvailable = true;
				sender.connectToServer();
				testServerState();
			}else{
				isNetworkAvailable = false;
				mqServerStateTv.setText("消息服务器连接断开");
				serverStateTv.setText("未开启网络连接");
			}
		}
	};
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		sender.destory();
		unregisterReceiver(networkStateReceiver);
	}

	@Override
	public void onConnectionBreak() {
		ToastUtil.showToast(MainActivity.this, "消息服务器连接断开");
	}

	@Override
	public void onConnectionResult(boolean login) {
		String result = login ? "消息服务器连接成功" : "消息服务器连接失败";
		mqServerStateTv.setText(result);
	}

	@Override
	public void onPreExcute(int what) {
		
	}

	@Override
	public void onResultBack(int what, List<? extends IHandleObj> result) {
		switch (what) {
		case MODULE_QUERY:
			modules = (List<Module>) result;
			if(modules != null && modules.size() > 0){
				initModule(modules);
			}
			break;
		}
	}
	
	/**
	 * 下载模块的数量
	 */
	private int num;
	private Map<Integer,Fragment> fragMap = new HashMap<Integer, Fragment>();
	@Override
	public void onHResult(Map<String, Object> result) {
		num++;
		if(result != null){
			int position = Integer.parseInt(result.get("position").toString());
			boolean hasSubs = (Boolean) result.get("hasSubs");
			String url = (String) result.get("url");
			if(hasSubs){
				H2Fragment frag = new H2Fragment();
				frag.setQueryUrl(url);
				fragMap.put(position, frag);
			}else{
				H1Fragment frag = new H1Fragment();
				frag.setQueryUrl(url);
				fragMap.put(position, frag);
			}
		}
		if(num == modules.size()){ //等于模块个数时，初始化ViewPager
			num = 0;
			initViewPager();
		}
	}
	
	/**
	 * 将Fragment添加到相应的位置
	 * @return
	 */
	private List<Fragment> addIntoFragmentList(){
		List<Fragment> fragList = new ArrayList<Fragment>();
		for(int i = 0 ; i < modules.size() ; i++){
			Fragment frag = fragMap.get(i);
			if(frag == null){
				frag = new Fragment();
			}
			fragList.add(frag);
		}
		return fragList;
	}
	
	/**
	 * 初始化ViewPager;
	 */
	private void initViewPager() {
		List<Fragment> fragList = addIntoFragmentList();
		adapter = new MainViewPagerAdapter(
				getSupportFragmentManager(), fragList);
		viewPager.setAdapter(adapter);
		viewPager.setOffscreenPageLimit(3);
	}
}
