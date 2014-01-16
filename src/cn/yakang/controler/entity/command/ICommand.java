package cn.yakang.controler.entity.command;

import org.json.JSONException;
import org.json.JSONObject;

public abstract class ICommand {
	/**
	 * 移动
	 */
	protected static final String MOVE = "move";
	/**
	 * 视角调整
	 */
	protected static final String VIEW_ANGLE = "viewAngle";
	/**
	 * 缩放
	 */
	protected static final String ZOOM = "zoom";
	/**
	 * 旋转
	 */
	protected static final String ROTATE = "rotate";
	/**
	 * 指北
	 */
	protected static final String NORTH = "north";
	protected static final String RESET = "reset";
	protected JSONObject jsonObj = new JSONObject();
	protected String actionId;
	public String toJson() throws JSONException{
		jsonObj.put("cmd", actionId);
		return jsonObj.toString();
	}
}
