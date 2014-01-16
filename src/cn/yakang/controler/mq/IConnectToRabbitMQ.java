package cn.yakang.controler.mq;


import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.ShutdownListener;

/**
 * Base class for objects that connect to a RabbitMQ Broker
 */
public abstract class IConnectToRabbitMQ {
	protected String mServer;
	/**
	 * Exchange(消息交换机)名称
	 */
	protected final static String EXCHANGE_NAME = "exchange";
	/**
	 * Exchange(消息交换机)类型
	 */
	protected final static String EXCHANGE_TYPE = "fanout";
	protected final static String QUEUE_NAME = "controlCommand";
	protected String userName;
	protected String password;
	protected int port;
	protected Channel mChannel;
	protected Connection mConnection;
	protected ShutdownListener listener;
	/**
	 * @param server 服务器地址
	 * @param userName 用户名
	 * @param password 密码
	 * @param exchange 消息交互机名称
	 * @param exchangeType 消息交互机类型
	 */
	public IConnectToRabbitMQ(String server,int port,String userName,String password) {
		this.mServer = server;
		this.port = port;
		this.userName = userName;
		this.password = password;
	}

	public void dispose() {
		try {
			mChannel.close();
			mConnection.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 连接RabbitMQ服务器
	 * @return true连接成功,false连接失败 
	 */
	public boolean connectToRabbitMQ() {
		// already declared
		if (mChannel != null && mChannel.isOpen()){
			return true;
		}
		try {
			ConnectionFactory connectionFactory = new ConnectionFactory();
			//地址
			connectionFactory.setHost(mServer);
			//用户名密码
			connectionFactory.setUsername(password);
			connectionFactory.setPassword(userName);
			//factory.setVirtualHost("karthi");
			//端口号
			connectionFactory.setPort(port);
			
			connectionFactory.setConnectionTimeout(5000);
			mConnection = connectionFactory.newConnection();
			mChannel = mConnection.createChannel();
			mChannel.exchangeDeclare(EXCHANGE_NAME, EXCHANGE_TYPE, true);
			//消息队列
			mChannel.queueDeclare(QUEUE_NAME, false, false, false, null);
			mChannel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "");
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	/**
	 * 连接断开监听
	 * @param listener
	 */
	public void addShutdownListener(ShutdownListener listener){
		if(mConnection != null){
			mConnection.addShutdownListener(listener);
		}
	}
}