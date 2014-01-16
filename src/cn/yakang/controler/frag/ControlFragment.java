package cn.yakang.controler.frag;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import cn.yakang.controler.MainActivity;
import cn.yakang.controler.R;
import cn.yakang.controler.entity.command.ICommand;
import cn.yakang.controler.entity.command.MoveCommand;
import cn.yakang.controler.entity.command.NorthCommand;
import cn.yakang.controler.entity.command.ResetCommand;
import cn.yakang.controler.entity.command.RotateCommand;
import cn.yakang.controler.entity.command.RotateCommand.RotateType;
import cn.yakang.controler.entity.command.ViewAngleCommand;
import cn.yakang.controler.entity.command.ZoomCommand;
import cn.yakang.controler.widget.RockerSurfaceView;
import cn.yakang.controler.widget.RockerSurfaceView.RudderListener;
import cn.yakang.controler.widget.rangeBar.RangeBar;
import cn.yakang.controler.widget.rangeBar.RangeBar.OnRangeBarChangeListener;

public class ControlFragment extends Fragment implements RudderListener,OnRangeBarChangeListener,OnClickListener{
	private MainActivity act;
	private int rangeBarIndex = 3;
	private int zoomRateIndex = 5;
	private int vgRangeBarTickCount,zmRangeBarTickCount;
	private MoveCommand command;
	private TextView angleTv,zoomRate;
	private Button playBtn;
	private String playStr,pauseStr;
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		act = (MainActivity) activity;
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		vgRangeBarTickCount = getResources().getInteger(R.integer.viewAngleTickCount);
		zmRangeBarTickCount = getResources().getInteger(R.integer.zoomTickCount);
		playStr = getResources().getString(R.string.play);
		pauseStr = getResources().getString(R.string.pause);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.control_layout, container,false);
		command = MoveCommand.getInstance();
		initView(view);
		return view;
	}
	private void initView(View view) {
		angleTv = (TextView) view.findViewById(R.id.view_angle_tv);
		zoomRate = (TextView) view.findViewById(R.id.zoom_rate_tv);
		
		RangeBar angleSeekBar = (RangeBar) view.findViewById(R.id.view_angle_seekbar);
		angleSeekBar.setThumbIndices(0, rangeBarIndex);
		angleSeekBar.setOnRangeBarChangeListener(this);
		angleTv.setText(rangeBarIndex*15 + "度");
		
		RangeBar zoomRangeBar = (RangeBar) view.findViewById(R.id.zoom_rangeBar);
		zoomRangeBar.setThumbIndices(0, zoomRateIndex);
		zoomRangeBar.setOnRangeBarChangeListener(this);
		RockerSurfaceView rocker = (RockerSurfaceView) view.findViewById(R.id.rocker);
		rocker.setRudderListener(this);
		zoomRate.setText(zoomRateIndex + 1 + "级");
		
		view.findViewById(R.id.right_btn).setOnClickListener(this);
		view.findViewById(R.id.left_btn).setOnClickListener(this);
		playBtn = (Button) view.findViewById(R.id.play_btn);
		playBtn.setOnClickListener(this);
		
		view.findViewById(R.id.north_arrow).setOnClickListener(this);
		view.findViewById(R.id.resetBtn).setOnClickListener(this);
		
	}
	public interface OnViewAngleChangedListener{
		void onViewAngleChanged(int viewAngle);
	}
	
	@Override
	public void onSteeringWheelChanged(int action, int angle, float offset) {
		command.setAngle(angle);
		this.sendCommand(command);
	}
	
	private void sendCommand(ICommand command) {
		act.sendCommand(command);
	}
	@Override
	public void onIndexChangeListener(RangeBar rangeBar, int index) {
		
	}
	@Override
	public void onIndexChangeEnd(RangeBar rangeBar, int index) {
		switch(rangeBar.getId()){
		case R.id.view_angle_seekbar:
			int viewAngle = index*15;
			angleTv.setText(index*15 + "度");
			ViewAngleCommand command = ViewAngleCommand.getInstance();
			command.setAngle(viewAngle);
			sendCommand(command);
			break;
		case R.id.zoom_rangeBar:
			zoomRate.setText(index + 1 + "级");
			ZoomCommand zoomCommand = ZoomCommand.getInstance();
			zoomCommand.setLevel(index);
			sendCommand(zoomCommand);
			break;
		}
	}
	
	private boolean play;
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.left_btn:
			sendRotateCommand(RotateType.Left);
			break;
		case R.id.right_btn:
			sendRotateCommand(RotateType.Right);
			break;
		case R.id.play_btn:
			togglePlayBtn();
			sendRotateCommand(RotateType.Normal);
			break;
		case R.id.north_arrow:
			NorthCommand command = NorthCommand.getInstance();
			sendCommand(command);
			break;
		case R.id.resetBtn:
			ResetCommand resetCommand = ResetCommand.getInstance();
			sendCommand(resetCommand);
			break;
		}
	}
	
	private void togglePlayBtn() {
		if(!play){
			play = true;
			playBtn.setText(pauseStr);
		}else{
			play = false;
			playBtn.setText(playStr);
		}
	}
	
	private void sendRotateCommand(RotateType type) {
		RotateCommand command = RotateCommand.getInstance();
		command.setType(type);
		sendCommand(command);
	}
}
