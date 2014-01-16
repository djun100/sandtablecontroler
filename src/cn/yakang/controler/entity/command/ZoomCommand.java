package cn.yakang.controler.entity.command;

import org.json.JSONException;



public class ZoomCommand extends ICommand {
	private static ZoomCommand command;
	private ZoomCommand(){
		actionId = ZOOM;
	}
	
	public static ZoomCommand getInstance(){
		if(command == null){
			command = new ZoomCommand();
		}
		return command;
	}
	
	public void setLevel(int level) {
		try {
			jsonObj.put("zoomLevel", level);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
}
