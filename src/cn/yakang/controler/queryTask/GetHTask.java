package cn.yakang.controler.queryTask;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import cn.yakang.controler.entity.handleObj.IHandleObj;
import cn.yakang.controler.entity.parser.BranchObjXmlParser;
import cn.yakang.controler.queryTask.IQueryTask.QueryType;
import cn.yakang.controler.util.SimulateTool;
import cn.yakang.controler.util.file.FileContext;
import cn.yakang.controler.util.file.FileTool;

public class GetHTask{
	private HResultListener listener;
	private MyAsynTask task;
	private Context context;
	public GetHTask(Context context){
		this.context = context;
	}
	public void doQueryTask(String...params) {
		task = new MyAsynTask();
		// 最多开启5个线程，超过5个等待
		task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,params);
	}
	
	public interface HResultListener{
		public void onHResult(Map<String,Object> result);
	}
	
	public void setOnResultListener(HResultListener listener) {
		this.listener = listener;
	}
	/**
	 * 查询信息异步任务
	 * @author asiacom104
	 */
	class MyAsynTask extends AsyncTask<String, Void, Map<String,Object>> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected Map<String,Object> doInBackground(String... params) {
			String baseUrl = params[0];
			String fileName = params[1];
			Map<String,Object> result;
			if(!FileTool.isFileExist(FileContext.getXmlFilePath(context), fileName)){
				//InputStream in = HttpUtil.getInputStream(baseUrl + fileName);
				InputStream in = SimulateTool.getInputStream(context, fileName);
				FileTool.saveFile(FileContext.getXmlFilePath(context), fileName, in);
				result = getResult(fileName);
			}else{
				result = getResult(fileName);
			}
			result.put("url", params[1]);
			result.put("position", params[2]);
			return result;
		}

		@Override
		protected void onPostExecute(Map<String,Object> result) {
			super.onPostExecute(result);
			if(listener != null ){
				listener.onHResult(result);
			}
		}
		private Map<String,Object> getResult(String fileName){
			Map<String,Object> map = new HashMap<String, Object>();
			boolean hasSubs = new BranchObjXmlParser(context).hasSubs(fileName);
			map.put("hasSubs", hasSubs);
			return map;
		}
	}
	
}
