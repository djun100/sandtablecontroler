package cn.yakang.controler.util;

import cn.yakang.controler.R;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class ToastUtil {
	public static void showToast(Context context,String text){
		View view = View.inflate(context, R.layout.my_toast, null);
		TextView tv = (TextView) view.findViewById(R.id.text);
		tv.setText(text);
		Toast toast = new Toast(context);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(view);
        toast.setGravity(Gravity.BOTTOM, 0,(int)context.getResources().getDimension(R.dimen.common_topbar_high));
        toast.show();
	}
}
