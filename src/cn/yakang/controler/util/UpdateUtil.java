package cn.yakang.controler.util;

import java.io.InputStream;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.content.Context;
import android.os.AsyncTask;
import cn.yakang.controler.entity.handleObj.LeafHandleObj;
import cn.yakang.controler.entity.parser.HandleObjJsonParser;
import cn.yakang.controler.util.file.FileContext;
import cn.yakang.controler.util.file.FileTool;
import cn.yakang.controler.util.net.HttpUtil;

public class UpdateUtil {
	private ExecutorService executorSer;
	private int taskNum;
	private OnUpdateListener listener;
	private Context context;
	public UpdateUtil(Context context){
		executorSer = Executors.newFixedThreadPool(5);
		this.context = context;
	}
	public void doUpdate(String...urls){
		taskNum = urls.length;
		for(int i = 0 ; i < taskNum; i++){
			new UpdateTask().executeOnExecutor(executorSer,urls[i]);
		}
	}
	
	public void setOnUpdateListener(OnUpdateListener listener){
		this.listener = listener;
	}
	
	public interface OnUpdateListener{
		public void onUpdating(int taskNum,int successNum,int failedNum);
		public void onUpdateFinish(int taskNum,int successNum,int failedNum);
	}
	
	private int successNum; //已更新数据的条数
	private int failedNum;
	class UpdateTask extends AsyncTask<String, Integer, Boolean>{
		
		@Override
		protected Boolean doInBackground(String... params) {
			InputStream in = HttpUtil.getInputStream(params[0]);
			if (in != null) {
				String fileName = params[0].substring(params[0].lastIndexOf("=")+1, params[0].length());
				FileTool.saveFile(FileContext.getXmlFilePath(context), fileName, in);
				return true;
			}
			return false;
		}
		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			if(result){
				successNum += 1;
			}else{
				failedNum += 1;
			}
			if(taskNum == (successNum + failedNum)){
				listener.onUpdateFinish(taskNum,successNum,failedNum);
				failedNum = 0;
				successNum = 0;
			}else{
				listener.onUpdating(taskNum,successNum,failedNum);
			}
		}
	}
}
