package cn.yakang.controler.util;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.RadioGroup.LayoutParams;
import cn.yakang.controler.R;

public class PopupWindowUtil {
	public static PopupWindow makePopupWindow(Context cx,View contentView) {
		int width = cx.getResources().getDisplayMetrics().heightPixels*2/5;
		PopupWindow popupWindow = new PopupWindow(cx);
	    //设置PopupWindow的内容view
		popupWindow.setFocusable(true);
		Drawable drawable = cx.getResources().getDrawable(R.drawable.bg_title);
		popupWindow.setBackgroundDrawable(drawable);
		popupWindow.setContentView(contentView);
		popupWindow.setHeight(width);
		popupWindow.setWidth(LayoutParams.MATCH_PARENT);
	    //设置PopupWindow外部区域是否可触摸
		popupWindow.setOutsideTouchable(true);
		//popupWindow.setAnimationStyle(0);
	    return popupWindow;
	}
}
