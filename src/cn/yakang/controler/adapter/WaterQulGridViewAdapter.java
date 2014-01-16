package cn.yakang.controler.adapter;

import java.util.ArrayList;
import java.util.List;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;
import cn.yakang.controler.MainActivity;
import cn.yakang.controler.R;
import cn.yakang.controler.entity.command.DynamicCommand;
import cn.yakang.controler.entity.command.ICommand;
import cn.yakang.controler.entity.handleObj.BranchHandleObj;
import cn.yakang.controler.entity.handleObj.IHandleObj;
import cn.yakang.controler.entity.handleObj.LeafHandleObj;
import cn.yakang.controler.entity.handleObj.Operation;

public class WaterQulGridViewAdapter extends BaseAdapter implements OnItemClickListener{
	private MainActivity act;
	private List<BranchHandleObj> datas = new ArrayList<BranchHandleObj>();
	private float textSize;
	private WaterQulGridViewItemAdapter adapter;
	public WaterQulGridViewAdapter(MainActivity act){
		this.act = act;
		textSize = act.getResources().getDimension(R.dimen.text_size_one);
	}
	public void setData(List<BranchHandleObj> objs){
		datas = objs;
	}
	
	@Override
	public int getCount() {
		return datas.size();
	}

	@Override
	public Object getItem(int arg0) {
		return datas.get(arg0);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder vh;
		if(convertView == null){
			vh = new ViewHolder();
			convertView = LayoutInflater.from(act).inflate(R.layout.water_qul_gridview_item,parent,false);
			vh.tv = (TextView) convertView.findViewById(R.id.textView1);
			vh.gv = (GridView) convertView.findViewById(R.id.gridView1);
			vh.gv.setOnItemClickListener(this);
			convertView.setTag(vh);
		}else{
			vh = (ViewHolder) convertView.getTag();
		}
		vh.tv.setText(datas.get(position).getName());
		vh.tv.setTextSize(TypedValue.COMPLEX_UNIT_PX,textSize);
		List<Operation> operations = datas.get(position).getOperations();
		adapter = new WaterQulGridViewItemAdapter(act);
		adapter.setData(operations);
		vh.gv.setAdapter(adapter);
		return convertView;
	}
	private class ViewHolder{
		TextView tv;
		GridView gv;
	}
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int arg2, long arg3) {
		String operationId = (String) view.getTag(R.id.tag_operationId);
		IHandleObj obj = (IHandleObj) view.getTag(R.id.tag_handleObj);
		DynamicCommand command = DynamicCommand.getInstance();
		command.setHandleObjId(obj.getId());
		command.setOperationId(operationId);
		this.sendCommand(command);
	}
	
	private void sendCommand(ICommand command){
		act.sendCommand(command);
	}
}
