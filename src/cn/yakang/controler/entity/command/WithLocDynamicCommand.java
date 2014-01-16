package cn.yakang.controler.entity.command;

import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

public class WithLocDynamicCommand extends DynamicCommand {
	private static WithLocDynamicCommand command;
	private WithLocDynamicCommand(){
	}
	public static WithLocDynamicCommand getInstance(){
		if(command == null){
			command = new WithLocDynamicCommand();
		}
		return command;
	}
	
	public void setObjLocation(Map<String,String> location){
		JSONObject obj = null;
		if(location != null){
			obj = new JSONObject(location);
			try {
				jsonObj.put("location", obj);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}
}
