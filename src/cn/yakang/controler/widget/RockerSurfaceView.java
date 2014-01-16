package cn.yakang.controler.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import cn.yakang.controler.R;
import cn.yakang.controler.util.MathUtils;

public class RockerSurfaceView extends SurfaceView implements Callback, Runnable {
	private SurfaceHolder mHolder;
	private boolean isStop = false;
	private Thread mThread;
	private Point mRockerPosition; // 摇杆位置
	private Point mCtrlPoint; // 摇杆起始位置
	private RudderListener listener = null; // 事件回调接口
	public static final int ACTION_RUDDER = 1; // 1：摇杆事件
	
	private Bitmap wheelBitmap;
	private Bitmap rockerBitmap;
	private int height,width;
	private int wheelBitmapHeight;
	private int rockerBitmapHeight;
	/**
	 * 落点是否有效
	 */
	private boolean isEffective;

	public RockerSurfaceView(Context context) {
		super(context);
		init();
	}

	public RockerSurfaceView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public RockerSurfaceView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	private void init() {
		setKeepScreenOn(true);
		mHolder = getHolder();
		mHolder.addCallback(this);
		setFocusable(true);
		setFocusableInTouchMode(true);
		//setZOrderOnTop(true);
		mHolder.setFormat(PixelFormat.TRANSPARENT); // 设置背景透明
	}

	// 设置回调接口
	public void setRudderListener(RudderListener rockerListener) {
		listener = rockerListener;
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		BitmapDrawable drawable = (BitmapDrawable) getResources().getDrawable(R.drawable.joystick_halo);
		wheelBitmap = drawable.getBitmap();
		BitmapDrawable drawable2 = (BitmapDrawable) getResources().getDrawable(R.drawable.joystick_manuel);
		rockerBitmap = drawable2.getBitmap();
		wheelBitmapHeight = wheelBitmap.getHeight();
		rockerBitmapHeight = rockerBitmap.getHeight();
		System.out.println("wheelBitmapHeight:" + wheelBitmapHeight);
		System.out.println("rockerBitmapHeight:" + rockerBitmapHeight);
		height = this.getHeight();
		width = this.getWidth();
		
		mCtrlPoint = new Point(width - wheelBitmapHeight/2 - 40, height - wheelBitmapHeight/2 - 40);
		mRockerPosition = new Point(mCtrlPoint);
		isStop = false;
		mThread = new Thread(this);
		mThread.start();
	}
	
	
	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		isStop = true;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			Log.d("onTouchEvent", "actionDown");
			// 如果屏幕接触点不在摇杆挥动范围内,则不处理
			isEffective = isPointInCircle(event.getX(),event.getY());
			if(isEffective){
				mRockerPosition.set((int) event.getX(), (int) event.getY());
			}
		}
		if (event.getAction() == MotionEvent.ACTION_MOVE) {
			if(!isEffective){
				return true;
			}
			if (isPointInCircle(event.getX(),event.getY())) {
				// 如果手指在摇杆活动范围内，则摇杆处于手指触摸位置
				mRockerPosition.set((int) event.getX(), (int) event.getY());
			} else {
				// 设置摇杆位置，使其处于手指触摸方向的 摇杆活动范围边缘
				mRockerPosition = MathUtils.getBorderPoint(mCtrlPoint,
						new Point((int) event.getX(), (int) event.getY()),
						wheelBitmapHeight/2);
			}
		}
		if (isEffective && listener != null) {
			float radian = MathUtils.getRadian(mCtrlPoint, new Point(
					(int) event.getX(), (int) event.getY()));
			float offset = MathUtils.getLength(event.getX(), event.getY(), mCtrlPoint.x, mCtrlPoint.y);
			listener.onSteeringWheelChanged(ACTION_RUDDER,
					this.getAngleCouvert(radian),offset > wheelBitmapHeight ? wheelBitmapHeight : offset);
		}
		// 如果手指离开屏幕，则摇杆返回初始位置
		if (isEffective && event.getAction() == MotionEvent.ACTION_UP) {
			mRockerPosition = new Point(mCtrlPoint);
			if (listener != null) {
				listener.onSteeringWheelChanged(ACTION_RUDDER,
						-1,0);
			}
		}
		return true;
	}

	@Override
	public void run() {
		Canvas canvas = null;
		while (!isStop) {
			try {
				canvas = mHolder.lockCanvas();
				canvas.drawColor(Color.TRANSPARENT, Mode.CLEAR);// 清除屏幕
				canvas.drawColor(0xffffffff);
				canvas.drawBitmap(wheelBitmap, mCtrlPoint.x-wheelBitmapHeight/2, mCtrlPoint.y-wheelBitmapHeight/2, null);
				canvas.drawBitmap(rockerBitmap,mRockerPosition.x - rockerBitmapHeight/2,mRockerPosition.y-rockerBitmapHeight/2,null);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (canvas != null) {
					mHolder.unlockCanvasAndPost(canvas);
				}
			}
			try {
				Thread.sleep(30);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	// 获取摇杆偏移角度 0-360°
	private int getAngleCouvert(float radian) {
		int tmp = (int) Math.round(radian / Math.PI * 180);
		if (tmp < 0) {
			return -tmp;
		} else {
			return 180 + (180 - tmp);
		}
	}
	
	private boolean isPointInCircle(float downX,float downY){
		int len = MathUtils.getLength(mCtrlPoint.x, mCtrlPoint.y, downX,
				downY);
		return len <= wheelBitmapHeight/2;
	}

	// 回调接口
	public interface RudderListener {
		void onSteeringWheelChanged(int action, int angle,float offset);
	}
}