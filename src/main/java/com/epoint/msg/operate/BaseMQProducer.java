package com.epoint.msg.operate;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import com.epoint.core.utils.json.JsonUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.msg.operate.MQSetting.MQExchangeMode;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * 生产者
 * 
 * @author ZHZHH
 */
public class BaseMQProducer {

	private MQSetting setting = null;

	private MQExchangeMode exchangeMode = null;
	private Channel channel = null;
	private Connection connection = null;

	public BaseMQProducer() {
		this("");
	}

	/**
	 * 构造匹配不同的生产端参数
	 * 
	 * @param pref
	 */
	public BaseMQProducer(String pref) {
		setting = new MQSetting(pref);
	}

	/**
	 * 连接
	 * 
	 * @return
	 * @throws Exception
	 */
	public Connection createConnection() throws Exception {

		ConnectionFactory factory = new ConnectionFactory();

		if (!StringUtil.isBlank(setting.getVhName())) {
			factory.setVirtualHost(setting.getVhName());
		} else {
			throw new Exception("invalid setting.getVhName().");
		}
		if (StringUtil.isNotBlank(setting.getUserName())) {
			factory.setUsername(setting.getUserName());
		} else {
			throw new Exception("invalid setting.getUserName().");
		}
		if (StringUtil.isNotBlank(setting.getPassWord())) {
			factory.setPassword(setting.getPassWord());
		} else {
			throw new Exception("invalid passWord.");
		}

		// 支持多节点
		if (setting.getAddrArr() != null) {
			connection = factory.newConnection(setting.getAddrArr());
		} else {
			// ZHZHH 这里肯定走不到
			factory.setHost(setting.getHost());

			if (setting.getPort() > -1) {
				factory.setPort(setting.getPort());
			} else {
				throw new Exception("invalid port.");
			}

			connection = factory.newConnection();
		}

		return connection;
	}

	/**
	 * 根据配置信息进行调整exchange规则
	 * 
	 * @return
	 * @throws Exception
	 */
	public MQExchangeMode InitExchangMode() throws Exception {
		if (setting.getExchangeName().toUpperCase().endsWith("." + MQExchangeMode.DIRECT.toString())) {
			return MQExchangeMode.DIRECT;
		} else if (setting.getExchangeName().toUpperCase().endsWith("." + MQExchangeMode.FANOUT.toString())) {
			return MQExchangeMode.FANOUT;
		} else if (setting.getExchangeName().toUpperCase().endsWith("." + MQExchangeMode.TOPIC.toString())) {
			return MQExchangeMode.TOPIC;
		} else {
			throw new Exception("invalid exchangeName.it should be endwith exchangeMode. e.g. ****.topic");
		}
	}

	/**
	 * @return
	 * @throws Exception
	 * 
	 * 
	 */
	public Channel createChannel() throws Exception {
		return createChannel(setting.getQueueName());
	}

	/**
	 * 创建Channel，使用TOPIC模式
	 * 
	 * @return
	 * @throws Exception
	 */
	public Channel createChannel(String queueName) throws Exception {
		if (channel == null) {
			Connection connection = createConnection();

			// 配置信息中获取
			exchangeMode = InitExchangMode();

			channel = connection.createChannel();

			// channel.exchangeDeclare(setting.getExchangeName(),
			// exchangeMode.toString().toLowerCase(), true);
			// channel.queueDeclare(queueName, true, false, false, null);
			// channel.queueBind(queueName, setting.getExchangeName(),
			// queueName);

			if (exchangeMode == MQExchangeMode.TOPIC) {
				channel.exchangeDeclare(setting.getExchangeName(), exchangeMode.toString().toLowerCase(), true);
			} else {
				channel.exchangeDeclare(setting.getExchangeName(), exchangeMode.toString().toLowerCase(), true);
				channel.queueDeclare(queueName, true, false, false, null);
				channel.queueBind(queueName, setting.getExchangeName(), queueName);
			}
		}

		return channel;
	}

	public Boolean publishMessage(MqMessage msg) throws Exception {
		return publishMessage(setting.getQueueName(), msg);
	}

	/**
	 * 发送消息，同时附带上消息的唯一标示符
	 * 
	 * @param msg
	 * @throws IOException
	 */
	public Boolean publishMessage(String queueName, MqMessage msg) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		msg.setSendDate(sdf.format(new Date()));
		createChannel(queueName);
		if (exchangeMode == MQExchangeMode.DIRECT) {
			channel.basicPublish(setting.getExchangeName(), queueName,
					new AMQP.BasicProperties.Builder().contentType("text/plain").deliveryMode(2).priority(1)
							.correlationId(msg.getCorrelationId()).build(),
					JsonUtil.objectToJson(msg).getBytes("UTF-8"));
			return true;
		} else {
			channel.basicPublish(setting.getExchangeName(), setting.getRoutingKey(),
					new AMQP.BasicProperties.Builder().contentType("text/plain").deliveryMode(2).priority(1)
							.correlationId(msg.getCorrelationId()).build(),
					JsonUtil.objectToJson(msg).getBytes("UTF-8"));

			return true;
		}
	}

	/**
	 * 释放连接
	 * 
	 * @throws IOException
	 */
	public void free() throws Exception {
		if (channel != null && channel.isOpen()) {
			channel.close();
		}

		if (connection != null && connection.isOpen()) {
			connection.close();
		}
	}
}
