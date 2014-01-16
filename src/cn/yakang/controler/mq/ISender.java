package cn.yakang.controler.mq;


import cn.yakang.controler.entity.command.ICommand;

public interface ISender {
	public void send(ICommand command);
	public void destory();
	public void connectToServer();
	public void setConnectionListener(ConnectionListener listener);
	public interface ConnectionListener{
		/**
		 * 连接断开
		 */
		public void onConnectionBreak();
		/**
		 * 连接结果
		 * @param login
		 */
		public void onConnectionResult(boolean login);
	};
}
