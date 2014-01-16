package cn.yakang.controler.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.TextView;
import cn.yakang.controler.R;
import cn.yakang.controler.entity.handleObj.Operation;

public class WaterQulGridViewItemAdapter extends BaseAdapter{
	private Context context;
	private List<Operation> operations = new ArrayList<Operation>();
	private float textSize;
	public WaterQulGridViewItemAdapter(Context context){
		this.context = context;
		textSize = context.getResources().getDimension(R.dimen.text_size_two);
	}
	
	public void setData(List<Operation> operations ) {
		this.operations = operations;
	}
	@Override
	public int getCount() {
		return operations.size();
	}

	@Override
	public Object getItem(int position) {
		return operations.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		TextView tv = null;
		if(convertView == null){
			LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			tv = new TextView(context);
			tv.setBackgroundResource(R.drawable.textview_bg);
			tv.setLayoutParams(params);
			tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
			tv.setGravity(Gravity.CENTER);
			tv.setPadding(0, 10, 0, 10);
		}else{
			tv = (TextView) convertView;
		}
		tv.setText(operations.get(position).getName());
		tv.setTag(R.id.tag_operationId,operations.get(position).getId());
		tv.setTag(R.id.tag_handleObj, operations.get(position).getHandleObj());
		return tv;
	}

}
