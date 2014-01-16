package cn.yakang.controler.queryTask;

import java.util.List;

import cn.yakang.controler.entity.handleObj.IHandleObj;
import cn.yakang.controler.entity.handleObj.LeafHandleObj;


public interface IQueryTask {
	public void doQueryTask(int what,QueryType type,String...params);
	
	public void setOnResultListener(OnResultListener listener);
	
	public interface OnResultListener{
		public void onPreExcute(int what);
		public void onResultBack(int what,List<? extends IHandleObj> result);
	}
	public enum QueryType{
		Json,Xml
	}
}
