package cn.yakang.controler.entity.handleObj;

import java.util.ArrayList;
import java.util.List;

import cn.yakang.controler.util.PinyinUtil;

public abstract class IHandleObj implements Comparable<IHandleObj>{
	private String name;
	private String id;
	private String parentId;
	private String pinyin;
	private List<Operation> operations = new ArrayList<Operation>();
	public IHandleObj(){
		
	}
	public IHandleObj(String name,String id,String parentId,List<Operation> operations){
		this.name = name;
		this.id = id;
		this.operations = operations;
		this.parentId = parentId;
		this.pinyin = PinyinUtil.spell(name);
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.pinyin = PinyinUtil.spell(name);
		this.name = name;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public List<Operation> getOperations() {
		return operations;
	}
	public void setOperations(List<Operation> operations) {
		if(operations != null){
			copy(operations);
		}
	}
	
	public String getPinyin() {
		return pinyin;
	}
	
	@Override
	public int compareTo(IHandleObj another) {
		return this.pinyin.compareTo(another.getPinyin());
	}
	
	private void copy(List<Operation> operations){
		for(Operation o : operations){
			Operation operation = o.clone();
			operation.setHandleObj(this);
			this.operations.add(operation);
		}
	}
	
	@Override
	public String toString() {
		return "IHandleObj [name=" + name + ", id=" + id + ", parentId="
				+ parentId + ", operations=" + operations + "]";
	}
	
}
