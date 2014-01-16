package cn.yakang.controler.adapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;
import cn.yakang.controler.MainActivity;
import cn.yakang.controler.R;
import cn.yakang.controler.entity.command.DynamicCommand;
import cn.yakang.controler.entity.command.ICommand;
import cn.yakang.controler.entity.handleObj.BranchHandleObj;
import cn.yakang.controler.entity.handleObj.IHandleObj;
import cn.yakang.controler.entity.handleObj.LeafHandleObj;
import cn.yakang.controler.entity.handleObj.Operation;
import cn.yakang.controler.widget.NoScrollGridView;

public class CategoryListViewAdapter extends BaseAdapter implements OnItemClickListener{
	private MainActivity act;
	private List<BranchHandleObj> dateList = new ArrayList<BranchHandleObj>();
	private int selectedPosition = -1;
	public CategoryListViewAdapter(MainActivity act){
		this.act = act;
	}
	public void setData(List<BranchHandleObj> dateList){
		this.dateList = dateList;
	}
	@Override
	public int getCount() {
		return dateList.size();
	}

	@Override
	public Object getItem(int position) {
		return dateList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		TextView tv = null;
		if(convertView == null){
			convertView = LayoutInflater.from(act).inflate(R.layout.expandable_list_item, parent,false);
		}
		BranchHandleObj obj = dateList.get(position);
		
		tv = (TextView) convertView.findViewById(R.id.text);
		tv.setText(obj.getName());
		
		convertView.setTag(dateList.get(position).getId());
		
		List<LeafHandleObj> subs = obj.getSubs();
		Collections.sort(subs); //排序
		convertView.setTag(R.id.tag_subs,subs);
		
		if(selectedPosition == position){
			convertView.setBackgroundResource(R.drawable.listview_item_bg);
		}else{
			convertView.setBackgroundColor(Color.WHITE);
		}
		initExpand(convertView, obj.getOperations());
		return convertView;
	}
	public void setSelected(int position){
		selectedPosition = position;
	}
	private void initExpand(View parent,List<Operation> operations) {
		NoScrollGridView gv = (NoScrollGridView) parent.findViewById(R.id.expandable);
		HeadGridViewAdapter adapter = new HeadGridViewAdapter(act);
		adapter.setData(operations);
		gv.setAdapter(adapter);
		gv.setOnItemClickListener(this);
	}
	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int arg2, long arg3) {
		String operationId = (String) view.getTag(R.id.tag_operationId);
		IHandleObj obj = (IHandleObj) view.getTag(R.id.tag_handleObj);
		
		DynamicCommand command = DynamicCommand.getInstance();
		command.setOperationId(operationId);
		command.setHandleObjId(obj.getId());
		this.sendCommand(command);
	}
	private void sendCommand(ICommand command) {
		act.sendCommand(command);
	}
}
