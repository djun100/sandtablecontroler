package cn.yakang.controler.entity.command;

import org.json.JSONException;

public class ViewAngleCommand extends ICommand {
	private static ViewAngleCommand command;
	private int viewAngle;
	private ViewAngleCommand(){
		this.actionId = VIEW_ANGLE;
	}
	
	public static ViewAngleCommand getInstance(){
		if(command == null){
			command = new ViewAngleCommand();
		}
		return command;
	}
	
	public void setAngle(int viewAngle){
		this.viewAngle = viewAngle;
	}
	@Override
	public String toJson() throws JSONException {
		jsonObj.put("viewAngle", viewAngle);
		return super.toJson();
	}
}
