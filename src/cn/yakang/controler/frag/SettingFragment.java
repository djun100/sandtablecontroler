package cn.yakang.controler.frag;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import cn.yakang.controler.MainActivity;
import cn.yakang.controler.R;
import cn.yakang.controler.entity.CommanUrl;
import cn.yakang.controler.util.ToastUtil;
import cn.yakang.controler.util.UpdateUtil;
import cn.yakang.controler.util.UpdateUtil.OnUpdateListener;
import cn.yakang.controler.util.Validater;
import cn.yakang.controler.widget.SummaryButton;

public class SettingFragment extends Fragment implements OnClickListener,OnUpdateListener{
	private MainActivity act;
	private SummaryButton serverBtn,updateBtn;
	
	private SharedPreferences pre;
	private String server; //服务器
	private int port; //端口号
	private String updateTime; //更新时间
	private UpdateUtil updateUtil;
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		act = (MainActivity) activity;
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		pre = getActivity().getSharedPreferences("serverInfo", Activity.MODE_PRIVATE);
		getServerInfo();
		
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.setting_layout, container,false);
		initView(view);
		updateUtil = new UpdateUtil(act);
		updateUtil.setOnUpdateListener(this);
		if(pre.getBoolean("needUpdate", true)){
			//doUpdate();
		}
		
		return view;
	}
	
	private void getServerInfo() {
		server = pre.getString("server", "192.168.0.209");
		port = pre.getInt("port", 5672);
		updateTime = pre.getString("updateTime", "暂未更新");
	}
	
	private void initView(View view) {
		serverBtn = (SummaryButton) view.findViewById(R.id.item1);
		updateBtn = (SummaryButton) view.findViewById(R.id.item3);
		serverBtn.setOnClickListener(this);
		updateBtn.setOnClickListener(this);
		updateBtn.setSummaryText("更新时间:"+updateTime);
		serverBtn.setSummaryText("服务器:" + server + "\n端    口:" + port);
	}
	
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.item1:
			showDialog();
			break;
		case R.id.item3:
			doUpdate();
			break;
		}
	}
	
	private void showDialog() {
		getServerInfo();
		
		final Dialog dialog = new Dialog(act, R.style.SDL_Dialog);
		Drawable dialogBackground = getResources().getDrawable(R.drawable.sdl_background_dark);
		dialog.getWindow().setBackgroundDrawable(dialogBackground);
		dialog.setCanceledOnTouchOutside(true);
		View view = View.inflate(act, R.layout.server_set_dialog_layout, null);
		final EditText addressEt = (EditText) view.findViewById(R.id.server_address);
		final EditText portEt = (EditText) view.findViewById(R.id.port_num);
		//addressEt.setKeyListener(IPAddressKeyListener.getInstance());
		addressEt.setText(server);
		portEt.setText(port + "");
		view.findViewById(R.id.ok_btn).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String server = addressEt.getText().toString();
				String port = portEt.getText().toString();
				if(Validater.ipAddressValidate(server)){
					saveIntoSharedPre("server", server);
					saveIntoSharedPre("port", Integer.valueOf(port));
					serverBtn.setSummaryText("服务器:" + server + "\n端     口:" + port);
					act.initSender();
				}else{
					ToastUtil.showToast(act, "请输入正确的地址");
				}
				dialog.dismiss();
			}
		});
		view.findViewById(R.id.cancel_btn).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		dialog.setContentView(view);
		dialog.show();
	}
	
	/**
	 * 更新数据
	 */
	private void doUpdate() {
		if(act.isNetworkConnect()){
			updateTime = getCurrentTime();
			saveIntoSharedPre("updateTime", updateTime);
			String [] urls = CommanUrl.urls;
			updateUtil.doUpdate(urls);
		}
	}
	
	/**
	 * 得到当前时间
	 * @return
	 */
	private String getCurrentTime() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.CHINA);
		return df.format(new Date());
	}
	
	/**
	 * 保存至共享参数
	 * @param key
	 * @param value
	 */
	private void saveIntoSharedPre(String key,Object value){
		Editor editor = pre.edit();
		if(value instanceof String){
			editor.putString(key, (String)value).commit();
		}else if(value instanceof Integer){
			editor.putInt(key, (Integer)value).commit();
		}else if(value instanceof Boolean){
			editor.putBoolean(key, (Boolean)value).commit();
		}
	}
	
	@Override
	public void onUpdating(int taskNum,int successNum,int failedNum) {
		updateBtn.setSummaryText("正在更新…… 共" + taskNum + "条，已更新" + successNum + "条，" + failedNum + "失败");
	}
	@Override
	public void onUpdateFinish(int taskNum,int successNum,int failedNum) {
		updateBtn.setSummaryText("更新时间:" + updateTime + "\n" + successNum + "条成功," + failedNum + "失败");
		boolean needUpdateNextTime = (failedNum > 1);
		saveIntoSharedPre("needUpdate", needUpdateNextTime);
		if(successNum > 0){
			act.notifyDataSetChange();
		}
	}
}
