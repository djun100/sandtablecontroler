package cn.yakang.controler.entity.handleObj;

import java.util.ArrayList;
import java.util.List;

public class BranchHandleObj extends IHandleObj {
	private List<LeafHandleObj> subs = new ArrayList<LeafHandleObj>();
	public BranchHandleObj(){
		
	}
	public BranchHandleObj(String name, String id,
			List<Operation> operations,List<LeafHandleObj> subs) {
		super(name, id, null, operations);
		this.subs = subs;
	}
	public void setSubs(List<LeafHandleObj> subs) {
		this.subs = subs;
	}
	public List<LeafHandleObj> getSubs() {
		return subs;
	}
	@Override
	public String toString() {
		return "BranchHandleObj [subs=" + subs + ", name=" + getName() + ", id=" + getId() + ", operations=" + getOperations() + "]";
	}
	
}
