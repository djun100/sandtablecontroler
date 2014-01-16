package cn.yakang.controler.queryTask;

import java.io.InputStream;
import java.util.List;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import cn.yakang.controler.entity.CommanUrl;
import cn.yakang.controler.entity.handleObj.IHandleObj;
import cn.yakang.controler.entity.parser.BranchObjXmlParser;
import cn.yakang.controler.entity.parser.LeafObjXmlParser;
import cn.yakang.controler.entity.parser.ModuleXmlParser;
import cn.yakang.controler.util.SimulateTool;
import cn.yakang.controler.util.file.FileContext;
import cn.yakang.controler.util.file.FileTool;
import cn.yakang.controler.util.net.HttpUtil;

public class QueryTask implements IQueryTask{
	private int what;
	private OnResultListener listener;
	private MyAsynTask task;
	private Context context;
	private QueryType type;
	public QueryTask(Context context){
		this.context = context;
	}
	public void doQueryTask(int what,QueryType type,String...params) {
		this.what = what;
		this.type = type;
		task = new MyAsynTask();
		// 最多开启5个线程，超过5个等待
		task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,params);
	}
	
	public void setOnResultListener(OnResultListener listener) {
		this.listener = listener;
	}
	/**
	 * 查询信息异步任务
	 * @author asiacom104
	 */
	class MyAsynTask extends AsyncTask<String, Void, List<? extends IHandleObj>> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			if(listener != null){
				listener.onPreExcute(what);
			}
		}

		@Override
		protected List<? extends IHandleObj> doInBackground(String... params) {
			if(type == QueryType.Json){
				return null;
			}else{
				String baseUrl = params[0];
				String fileName = params[1];
				List<? extends IHandleObj> result = getResult(fileName);
				if(result == null){
					//InputStream in = HttpUtil.getInputStream(baseUrl + fileName);
					InputStream in = SimulateTool.getInputStream(context, fileName);
					FileTool.saveFile(FileContext.getXmlFilePath(context), fileName, in);
					result = getResult(fileName);
				}
				return result;
			}
		}

		@Override
		protected void onPostExecute(List<? extends IHandleObj> result) {
			super.onPostExecute(result);
			if(listener != null ){
				listener.onResultBack(what, result);
			}
		}
		private List<? extends IHandleObj> getResult(String fileName){
			if(CommanUrl.MODULE.equals(fileName)){
				return new ModuleXmlParser(context).parser(fileName);
			}else{
				return new BranchObjXmlParser(context).parser(fileName);
			}
		}
	}
	
}
