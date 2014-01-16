package cn.yakang.controler.entity.handleObj;

import java.util.List;
import java.util.Map;


public class LeafHandleObj extends IHandleObj{
	private Map<String,String> location;
	public LeafHandleObj() {
	}
	public LeafHandleObj(String name, String id, String belong,
			List<Operation> operations) {
		super(name, id, belong, operations);
	}
	
	public LeafHandleObj(String name, String id, String belong,
			List<Operation> operations,Map<String,String> location) {
		super(name, id, belong, operations);
		this.location = location;
	}
	
	public void setLocation(Map<String, String> location) {
		this.location = location;
	}
	public Map<String, String> getLocation() {
		return location;
	}
	
	@Override
	public String toString() {
		return "LeafHandleObj [name=" + getName() + ", id=" + getId() + ", operations=" + getOperations() + "]";
	}
}
