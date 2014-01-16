package cn.yakang.controler.frag;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;
import cn.yakang.controler.MainActivity;
import cn.yakang.controler.R;
import cn.yakang.controler.adapter.WaterQulGridViewAdapter;
import cn.yakang.controler.entity.CommanUrl;
import cn.yakang.controler.entity.command.ICommand;
import cn.yakang.controler.entity.handleObj.BranchHandleObj;
import cn.yakang.controler.entity.handleObj.IHandleObj;
import cn.yakang.controler.queryTask.IQueryTask;
import cn.yakang.controler.queryTask.IQueryTask.OnResultListener;
import cn.yakang.controler.queryTask.IQueryTask.QueryType;
import cn.yakang.controler.queryTask.QueryTask;

public class H1Fragment extends Fragment implements OnResultListener{
	private MainActivity act;
	private IQueryTask queryTask;
	public static final int WATER_QUL_QUERY = 21;
	private WaterQulGridViewAdapter gvAdapter;
	
	private ProgressBar loadingProgress;
	private TextView loadTv;
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		act = (MainActivity) activity;
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		queryTask = new QueryTask(act);
		queryTask.setOnResultListener(this);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.water_qul_layout2, container,false);
		initView(view);
		startQuery(WATER_QUL_QUERY, new String[]{CommanUrl.BASE,queryUrl});
		return view;
	}
	
	private void initView(View view) {
		GridView gv = (GridView) view.findViewById(R.id.gridView1);
		gvAdapter = new WaterQulGridViewAdapter(act);
		gv.setAdapter(gvAdapter);
		loadingProgress = (ProgressBar) view.findViewById(R.id.progressBar_load);
		loadTv = (TextView) view.findViewById(R.id.text_load);
	}
	
	
	private void startQuery(int what,String...params){
		queryTask.doQueryTask(what, QueryType.Xml, params);
	}
	
	@Override
	public void onPreExcute(int what) {
		switch(what){
		case WATER_QUL_QUERY:
			loadingProgress.setVisibility(View.VISIBLE);
			loadTv.setVisibility(View.VISIBLE);
			break;
		}
	}
	@Override
	public void onResultBack(int what, List<? extends IHandleObj> result) {
		switch(what){
		case WATER_QUL_QUERY:
			loadingProgress.setVisibility(View.GONE);
			if(result != null){
				loadTv.setVisibility(View.GONE);
				gvAdapter.setData((List<BranchHandleObj>) result);
				gvAdapter.notifyDataSetChanged();
			}else{
				loadTv.setText("加载数据失败！");
			}
			break;
		}
	}
	
	private String queryUrl;
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
	
	private void sendCommand(ICommand command) {
		act.sendCommand(command);
	}
}
