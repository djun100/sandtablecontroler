package cn.yakang.controler.entity.command;

import org.json.JSONException;


public class RotateCommand extends ICommand {
	private static RotateCommand command;
	private RotateCommand(){
		actionId = ROTATE;
	}
	
	public static RotateCommand getInstance(){
		if(command == null){
			command = new RotateCommand();
		}
		return command;
	}
	
	public void setType(RotateType type){
		try {
			jsonObj.put("type", type.name);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	public enum RotateType{
		Left("left"),Right("right"),Normal("normal");
		String name;
		private RotateType(String name){
			this.name = name;
		}
	}
}
