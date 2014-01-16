package cn.yakang.controler.mq;

import org.json.JSONException;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import cn.yakang.controler.entity.command.ICommand;

import com.rabbitmq.client.ShutdownListener;
import com.rabbitmq.client.ShutdownSignalException;

public class RabbitMQSender extends IConnectToRabbitMQ implements ISender{

	private ConnectionListener listener;
	private SendTask task;
	private boolean exit = false;
	public RabbitMQSender(String server,int port,String userName, String password) {
		super(server, port,userName, password);
	}
	
	@Override
	public void send(ICommand command) {
		try {
			task = new SendTask();
			task.execute(command.toJson());
		} catch (JSONException e) {
			Log.e("send", "JSONException");
		}
	}
	
	@Override
	public void destory() {
		this.dispose();
		exit = true;
	}

	@Override
	public void connectToServer() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				boolean b = RabbitMQSender.this.connectToRabbitMQ();
				Message msg = handler.obtainMessage();
				msg.obj = b;
				handler.sendMessage(msg);
			}
		}).start();
	}
	
	@Override
	public void setConnectionListener(final ConnectionListener listener) {
		this.listener = listener;
		super.addShutdownListener(new ShutdownListener() {
			@Override
			public void shutdownCompleted(ShutdownSignalException arg0) {
				listener.onConnectionBreak();
			}
		});
	}
	
	class SendTask extends AsyncTask<String, Void, Boolean>{

		@Override
		protected Boolean doInBackground(String... params) {
			boolean flag = false;
			Log.d("SendTask", "doInBackground");
			if(exit){
				return flag;
			}
			if(RabbitMQSender.this.connectToRabbitMQ()){
				flag = true;
				String tempstr = "";
				for (int i = 0; i < params.length; i++){
					tempstr += params[i];
				}
				try {
					//发送
					Log.d("SendTask", tempstr);
					RabbitMQSender.this.mChannel.basicPublish(RabbitMQSender.EXCHANGE_NAME, "", null,
							tempstr.getBytes());
				} catch (Exception e) {
				}
			}else{
				Log.d("SendTask", "loginFailed");
			}
			return flag;
		}
		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			if(listener != null){
				listener.onConnectionResult(result);
			}
		}
		@Override
		protected void onCancelled() {
			super.onCancelled();
			Log.d("SendTask", "cancel");
		}
	}
	private Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			boolean result = (Boolean) msg.obj;
			if(listener != null){
				listener.onConnectionResult(result);
			}
		}
	};
}
