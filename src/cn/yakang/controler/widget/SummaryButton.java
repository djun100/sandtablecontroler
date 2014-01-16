package cn.yakang.controler.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.yakang.controler.R;

public class SummaryButton extends RelativeLayout {
	private String text;
	private String summaryText;
	private int textColor;
	private int summaryTextColor;
	private float textSize;
	private float summaryTextSize;
	private Drawable drawableRight;
	private ImageView iv;

	private TextView tv;
	private TextView summaryTv;

	public SummaryButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public SummaryButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
				R.styleable.SummaryButton,0,0);
		text = a.getString(R.styleable.SummaryButton_text);
		summaryText = a.getString(R.styleable.SummaryButton_summaryText);
		textColor = a.getColor(R.styleable.SummaryButton_textColor, 0xff000000);
		summaryTextColor = a.getColor(R.styleable.SummaryButton_summaryTextColor, 0x88000000);
		textSize = a.getDimension(R.styleable.SummaryButton_textSize, 0);
		summaryTextSize = a.getDimension(R.styleable.SummaryButton_summaryTextSize, 0);
		drawableRight = a.getDrawable(R.styleable.SummaryButton_drawableRight);

		RelativeLayout.LayoutParams lp1 = new RelativeLayout.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		lp1.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		lp1.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);

		tv = new TextView(context);
		tv.setText(text);
		tv.setTextColor(textColor);
		tv.setTextSize(TypedValue.COMPLEX_UNIT_PX,textSize);
		tv.setId(1);
		tv.setGravity(Gravity.CENTER_VERTICAL);
		this.addView(tv, lp1);

		RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		lp2.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		lp2.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);

		iv = new ImageView(context);
		iv.setId(2);
		iv.setImageDrawable(drawableRight);
		this.addView(iv,lp2);
		
		RelativeLayout.LayoutParams lp3 = new RelativeLayout.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		lp3.addRule(RelativeLayout.LEFT_OF,2);
		lp3.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);

		summaryTv = new TextView(context);
		summaryTv.setText(summaryText);
		summaryTv.setTextColor(summaryTextColor);
		summaryTv.setPadding(0, 0, 10, 0);
		summaryTv.setTextSize(TypedValue.COMPLEX_UNIT_PX,summaryTextSize);
		
		this.addView(summaryTv,lp3);

		a.recycle();
	}

	public SummaryButton(Context context) {
		super(context);
	}
	
	public void setSummaryText(CharSequence text) {
		summaryText = text.toString();
		summaryTv.setText(text);
	}

	public void setSummaryText(int resId) {
		summaryText = getResources().getString(resId);
		setSummaryText(summaryText);
	}

	public void setSummaryTextColor(int color) {
		summaryTextColor = color;
		setSummaryTextColor(color);
	}

	public void setSummaryTextSize(float size) {
		summaryTextSize = size;
		summaryTv.setTextSize(size);
	}

	public void setSummaryTextSize(int resId) {
		summaryTextSize = getResources().getDimension(resId);
		setSummaryTextSize(summaryTextSize);
	}

	public void setText(CharSequence text) {
		this.text = text.toString();
		tv.setText(this.text);
	}

	public void setText(int resId) {
		text = getResources().getString(resId);
		setText(text);
	}

	public void setTextColor(int color) {
		this.textColor = color;
		tv.setTextColor(textColor);
	}

	public void setTextSize(float size) {
		textSize = size;
		tv.setTextSize(size);
	}

	public void setTextSize(int resId) {
		textSize = getResources().getDimension(resId);
		setTextSize(textSize);
	}

	public void setDrawableRight(int resId) {
		drawableRight = getResources().getDrawable(resId);
		setDrawableRight(drawableRight);
	}

	public void setDrawableRight(Drawable drawable) {
		drawableRight = drawable;
		iv.setImageDrawable(drawable);
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
	}
}
