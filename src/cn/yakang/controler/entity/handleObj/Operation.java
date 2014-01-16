package cn.yakang.controler.entity.handleObj;



public class Operation implements Cloneable{
	private String name;
	private String id;
	private IHandleObj handleObj;
	public Operation(){
		
	}
	
	public Operation(String name, String id) {
		this.name = name;
		this.id = id;
	}
	
	public Operation(String name, String id,IHandleObj handleObj) {
		this.name = name;
		this.id = id;
		this.handleObj = handleObj;
	}
	public String getName() {
		return name;
	}
	public String getId() {
		return id;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public void setHandleObj(IHandleObj handleObj) {
		this.handleObj = handleObj;
	}
	
	public IHandleObj getHandleObj() {
		return handleObj;
	}
	
	public Operation clone(){
		Operation o = null;
		try {
			o = (Operation) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return o;
	}

	@Override
	public String toString() {
		return "Operation [name=" + name + ", id=" + id + "]";
	}
	
	
}
