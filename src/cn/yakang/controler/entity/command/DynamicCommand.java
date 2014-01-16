package cn.yakang.controler.entity.command;


import org.json.JSONException;

public class DynamicCommand extends ICommand {
	private static DynamicCommand command;
	public DynamicCommand(){
	}
	public static DynamicCommand getInstance(){
		if(command == null){
			command = new DynamicCommand();
		}
		return command;
	}
	
	public void setOperationId(String operationId) {
		this.actionId = operationId;
	}
	
	public void setHandleObjId(String objId){
		try {
			jsonObj.put("objId", objId);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
