package cn.yakang.controler.frag;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import cn.yakang.controler.MainActivity;
import cn.yakang.controler.R;
import cn.yakang.controler.adapter.CategoryListViewAdapter;
import cn.yakang.controler.adapter.SubsListViewAdapter;
import cn.yakang.controler.entity.CommanUrl;
import cn.yakang.controler.entity.command.ICommand;
import cn.yakang.controler.entity.handleObj.BranchHandleObj;
import cn.yakang.controler.entity.handleObj.IHandleObj;
import cn.yakang.controler.entity.handleObj.LeafHandleObj;
import cn.yakang.controler.queryTask.IQueryTask;
import cn.yakang.controler.queryTask.IQueryTask.OnResultListener;
import cn.yakang.controler.queryTask.IQueryTask.QueryType;
import cn.yakang.controler.queryTask.QueryTask;
import cn.yakang.controler.widget.MySideBar;
import cn.yakang.controler.widget.MySideBar.OnTouchingLetterChangedListener;
import cn.yakang.controler.widget.slideExpandable.ActionSlideExpandableListView;
import cn.yakang.controler.widget.slideExpandable.SlideExpandableListView;

public class H2Fragment extends Fragment implements OnItemClickListener,OnResultListener,OnTouchingLetterChangedListener {
	private int selected = -1;

	private SubsListViewAdapter detailListViewadapter; // 信息查询,分类列表
	private CategoryListViewAdapter itemAdapter; // 信息查询，分类详细列表
	private ProgressBar searcheItemProgress;

	private ListView detailListView;
	private SlideExpandableListView listView;
	private TextView reminderTv,overlayTv;

	private MainActivity act;
	
	public static final int ITEM = 1;
	public static final int DETAIL = 2;
	private IQueryTask queryTask;
	private String queryUrl;
	private MySideBar sideBar;
	private List<LeafHandleObj> subs;
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		act = (MainActivity) activity;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		queryTask = new QueryTask(getActivity());
		queryTask.setOnResultListener(this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.double_listview_layout,
				container, false);
		initSearchInfoView(view);
		startQuery(ITEM, new String[]{CommanUrl.BASE,queryUrl});
		return view;
	}

	/**
	 * @return
	 */
	private void initSearchInfoView(View searchInfoView) {
		listView = (SlideExpandableListView) searchInfoView
				.findViewById(R.id.listView1);
		LayoutParams params = (LayoutParams) listView.getLayoutParams();
		params.width = getResources().getDisplayMetrics().widthPixels/3;
		listView.setLayoutParams(params);
		
		detailListView = (ListView) searchInfoView.findViewById(R.id.listView2);
		detailListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

		detailListViewadapter = new SubsListViewAdapter(act);
		
		detailListView.setAdapter(detailListViewadapter);

		searcheItemProgress = (ProgressBar) searchInfoView
				.findViewById(R.id.progressBar);
		reminderTv = (TextView) searchInfoView.findViewById(R.id.reminder_tv);
		reminderTv.setVisibility(View.VISIBLE);
		reminderTv.setText("选择类别");

		itemAdapter = new CategoryListViewAdapter(act);
		listView.setAdapter(itemAdapter);
		listView.setOnItemClickListener(this);

		sideBar = (MySideBar) searchInfoView.findViewById(R.id.mySideBar1);
		sideBar.setOnTouchingLetterChangedListener(this);
		sideBar.setVisibility(View.GONE);
		overlayTv = (TextView) searchInfoView.findViewById(R.id.overLay_tv);
		
	}
	
	public void startQuery(int what,String...params) {
		queryTask.doQueryTask(what,QueryType.Xml,params);
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long arg3) {
		switch (parent.getId()) {
		case R.id.listView1:
			if (selected != position) {
				switchListItemBg(view);
				itemAdapter.setSelected(position);
				
				subs = (List<LeafHandleObj>) view.getTag(R.id.tag_subs);
				initSubsListView(subs);
				
				if(view.getHeight()*subs.size() > sideBar.getHeight()){
					sideBar.setVisibility(View.VISIBLE);
				}
			}
			selected = position;
			break;
		}
	}
	
	
	private View oldView;
	private void switchListItemBg(View selectedView){
		if(oldView != null){
			oldView.setBackgroundColor(Color.TRANSPARENT);
		}
		selectedView.setBackgroundResource(R.drawable.listview_item_bg);
		oldView = selectedView;
	}
	
	/**
	 * 初始化详细列表
	 * @param objs
	 */
	public void initSubsListView(List<LeafHandleObj> objs) {
		reminderTv.setVisibility(View.GONE);
		searcheItemProgress.setVisibility(View.INVISIBLE);
		detailListViewadapter.clearDate();
		if(objs != null && objs.size() > 0){
			detailListViewadapter.setData(objs);
		}
		detailListViewadapter.notifyDataSetInvalidated();
	}

	private void sendCommand(ICommand command) {
		act.sendCommand(command);
	}

	@Override
	public void onPreExcute(int what) {
		switch(what){
		case ITEM:
			
			break;
		case DETAIL:
			searcheItemProgress.setVisibility(View.VISIBLE);
			detailListViewadapter.clearDate();
			detailListViewadapter.notifyDataSetChanged();
			reminderTv.setVisibility(View.INVISIBLE);
			break;
		}
	}
	@Override
	public void onResultBack(int what, List<? extends IHandleObj> result) {
		switch(what){
		case ITEM:
			if(result != null && result.size() > 0){
				itemAdapter.setData((List<BranchHandleObj>) result);
				itemAdapter.notifyDataSetChanged();
			}else if(result != null && result.size() == 0){
				reminderTv.setVisibility(View.VISIBLE);
				reminderTv.setText("没有相关数据！");
			}else{
				reminderTv.setVisibility(View.VISIBLE);
				reminderTv.setText("获取数据失败！");
			}
			break;
		}
	}
	/**
	 * 设置查询数据URL
	 * @param queryUrl
	 */
	public void setQueryUrl(String queryUrl) {
		this.queryUrl = queryUrl;
	}
	public String getQueryUrl() {
		return queryUrl;
	}
	
	@SuppressLint("HandlerLeak")
	private Handler overlayHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			overlayTv.setVisibility(View.GONE);
		}
	};
	
	@Override
	public void onTouchingLetterChanged(String s) {
		int position = alphaIndexer(s);
		overlayTv.setVisibility(View.VISIBLE);
		if(position >= 0){
        	overlayTv.setText(subs.get(position).getName().substring(0, 1));
			detailListView.setSelection(position);
		}else{
			overlayTv.setText(s);
		}
	}
	
	@Override
	public void onTouchFinish() {
		overlayHandler.sendEmptyMessageDelayed(0, 500);
	}
	
	/**
	 * 点击的字母在列表中对应的位置
	 * @param s
	 * @return
	 */
	private int alphaIndexer(String s) {  
        int position = -1;  
        for (int i = 0; i < subs.size(); i++) {  
            if(subs.get(i).getPinyin().toUpperCase().startsWith(s)){
            	position = i;
            	break;
            }
        }  
        return position;  
    }

	
}
