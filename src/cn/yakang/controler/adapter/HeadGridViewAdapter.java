package cn.yakang.controler.adapter;

import java.util.ArrayList;
import java.util.List;

import com.rabbitmq.client.GetResponse;

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

public class HeadGridViewAdapter extends BaseAdapter {
	private List<Operation> list = new ArrayList<Operation>();
	private Context context;
	public HeadGridViewAdapter(Context context){
		this.context = context;
	}
	public void setData(List<Operation> operations){
		this.list = operations;
	}
	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		return list.get(arg0);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		String name = list.get(position).getName();
		TextView tv = null;
		if(convertView == null){
			LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			tv = new TextView(context); 
			tv.setLayoutParams(params);
			tv.setBackgroundResource(R.drawable.searchinfo_gridview_item_bg);
			tv.setGravity(Gravity.CENTER);
			tv.setPadding(10, 10, 10, 10);
			tv.setTextSize(TypedValue.COMPLEX_UNIT_PX,context.getResources().getDimension(R.dimen.text_size_two));
		}else{
			tv = (TextView) convertView;
		}
		tv.setText(name);
		tv.setTag(R.id.tag_operationId,list.get(position).getId());
		tv.setTag(R.id.tag_handleObj,list.get(position).getHandleObj());
		return tv;
	}

}
