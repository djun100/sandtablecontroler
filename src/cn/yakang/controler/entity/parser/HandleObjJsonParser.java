package cn.yakang.controler.entity.parser;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import cn.yakang.controler.entity.handleObj.Operation;
import cn.yakang.controler.entity.handleObj.LeafHandleObj;

public class HandleObjJsonParser implements Parser{
	public HandleObjJsonParser(){
	}
	@Override
	public List<LeafHandleObj> parse(String resource) {
		if(resource == null){
			return null;
		}
		List<LeafHandleObj> list = new ArrayList<LeafHandleObj>();
		try {
			JSONArray jsonArray = new JSONArray(resource);
			for(int i = 0 ; i < jsonArray.length() ; i++){
				JSONObject jsonObj = jsonArray.getJSONObject(i);
				int id = jsonObj.getInt("id");
				String name = jsonObj.getString("name");
				List<Operation> actions = null;
				if(jsonObj.has("actions")){
					JSONArray jsonArr2 = jsonObj.getJSONArray("actions");
					actions = new ArrayList<Operation>();
					for(int j = 0 ; j < jsonArr2.length() ; j++){
						JSONObject jsonObj2 = jsonArr2.getJSONObject(j);
						String actionName = jsonObj2.getString("name");
						String actionId = jsonObj2.getString("actionId");
						Operation action = new Operation(actionName, actionId);
						actions.add(action);
					}
				}else{
					actions = simulate();
				}
				LeafHandleObj handleObj = new LeafHandleObj(name, id+"", null, actions);
				list.add(handleObj);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;
	}
	private List<Operation> simulate(){
		List<Operation> actions = new ArrayList<Operation>();
		for(int j = 0 ; j < 3 ; j++){
			Operation action = new Operation("操作"+j, j + 9 + "");
			actions.add(action);
		}
		return actions;
	}
}
