package cn.yakang.controler.entity.command;

import org.json.JSONException;

public class MoveCommand extends ICommand {
	private int angle = -1;
	private static MoveCommand command;
	private MoveCommand(){
		actionId = MOVE;
	}
	
	public static MoveCommand getInstance(){
		if(command == null){
			command = new MoveCommand();
		}
		return command;
	}
	
	public void setAngle(int angle) {
		this.angle = angle;
	}
	@Override
	public String toJson() throws JSONException{
		jsonObj.put("angle", angle);
		return super.toJson();
	}

}
