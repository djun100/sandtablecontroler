package cn.yakang.controler.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import cn.yakang.controler.MainActivity;
import cn.yakang.controler.R;
import cn.yakang.controler.entity.command.ICommand;
import cn.yakang.controler.entity.command.WithLocDynamicCommand;
import cn.yakang.controler.entity.handleObj.IHandleObj;
import cn.yakang.controler.entity.handleObj.LeafHandleObj;
import cn.yakang.controler.entity.handleObj.Operation;

public class SubsListViewAdapter extends BaseAdapter{
	private MainActivity act;
	private LayoutInflater inflater;
	private Context context;
	private List<LeafHandleObj> dateList = new ArrayList<LeafHandleObj>();
	public SubsListViewAdapter(MainActivity act){
		context = act;
		this.act = act;
		inflater = LayoutInflater.from(act);
	}
	public void setData(List<LeafHandleObj> dateList){
		this.dateList.addAll(dateList);
	}
	
	public void clearDate() {
		dateList.clear();
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
	public int getViewTypeCount() {
		return super.getViewTypeCount();
	}
	@Override
	public int getItemViewType(int position) {
		return super.getItemViewType(position);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder vh;
		if(convertView == null){
			convertView = inflater.inflate(R.layout.listview_item_two, parent,false);
			vh = new ViewHolder();
			vh.tv = (TextView) convertView.findViewById(R.id.item_name);
			vh.actionLayout = (LinearLayout) convertView.findViewById(R.id.handle_layout);
			initHandleView(vh, position);
			convertView.setTag(vh);
		}else{
			vh = (ViewHolder) convertView.getTag();
		}
		vh.tv.setText(dateList.get(position).getName());
		convertView.setTag(R.id.tag_first,dateList.get(position).getId());
		return convertView;
	}
	
	class ViewHolder{
		TextView tv;
		LinearLayout actionLayout;
	}
	
	private void sendCommand(ICommand command) {
		act.sendCommand(command);
	}
	
	private void initHandleView(ViewHolder vh,final int position) {
		List<Operation> operations = dateList.get(position).getOperations();
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0,LayoutParams.MATCH_PARENT);
		params.weight = 1;
		for(int i = 0 ; i < operations.size() ; i++){
			final Operation operation = operations.get(i);
			TextView operationView = (TextView) inflater.inflate(R.layout.dynamic_command_layout, null);
			operationView.setLayoutParams(params);
			operationView.setText(operation.getName());
			operationView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					WithLocDynamicCommand command = WithLocDynamicCommand.getInstance();
					IHandleObj obj = operation.getHandleObj();
					command.setHandleObjId(operation.getHandleObj().getId());
					command.setOperationId(operation.getId());
					if(obj instanceof LeafHandleObj){
						LeafHandleObj leafObj = (LeafHandleObj) obj;
						if(leafObj.getLocation() != null){
							command.setObjLocation(leafObj.getLocation());
						}
					}
					sendCommand(command);
				}
			});
			vh.actionLayout.addView(operationView);
		}
	}
}
