package com.epoint.msg.operate;

import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.string.StringUtil;
import com.rabbitmq.client.Address;

/**
 * MQ配置信息实体类
 * 
 * @author zhangpl
 * @version 2016年7月12日
 */
public class MQSetting {
	enum MQExchangeMode {
		TOPIC, FANOUT, DIRECT;
	}

	public static final String MQDefaultType = "Default";

	public MQSetting() {
		this("");
	}

	/**
	 * 支持不同类型的业务采用不同的配置. 配置前缀. 例如竞价系统配置JingJia.MQAddress，则type为JingJia
	 * 
	 * @param type
	 */
	public MQSetting(String type) {
		if (StringUtil.isBlank(type) || MQDefaultType.equals(type)) {
			type = "";
		} else {
			type += ".";
		}

		// 不推荐使用URL方式，因为HOSTS不支持
		// MQAddress采用uri的格式：amqp://user:pass@host:10000/vhost，参考http://www.rabbitmq.com/uri-spec.html
		String uri = ConfigUtil.getConfigValue("ztb", type + "MQAddress");
		if (StringUtil.isNotBlank(uri)) {
			this.uri = uri;
		} else {
			// 其实不用了
			this.host = ConfigUtil.getConfigValue("ztb", type + "host");
			// log.info(ConfigUtil.getConfigValue("ztb", type + "port"));
			this.port = Integer.valueOf(ConfigUtil.getConfigValue("ztb", type + "port").toString().trim());
			this.userName = ConfigUtil.getConfigValue("ztb", type + "userName");
			this.passWord = ConfigUtil.getConfigValue("ztb", type + "passWord");
			this.vhName = ConfigUtil.getConfigValue("ztb", type + "vhName");

			// TODO:为了以后支持多个节点，需要修改我们的A,B 为 IP:端口，IP2:端口2
			// 支持多节点
			if (this.host.contains(",")) {
				try {
					String[] arrSplit = this.host.split(",");
					this.addrArr = new Address[arrSplit.length];
					for (int i = 0; i < arrSplit.length; i++) {

						String[] arrSplit_Sub = arrSplit[i].split(":");

						if (arrSplit_Sub.length == 1) {
							Address ad = new Address(arrSplit[i], this.port);
							this.addrArr[i] = ad;
						} else {
							Address ad = new Address(arrSplit_Sub[0], Integer.valueOf(arrSplit_Sub[1]));
							this.addrArr[i] = ad;
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				this.addrArr = new Address[1];
				this.addrArr[0] = new Address(ConfigUtil.getConfigValue("ztb", type + "host"),
						Integer.valueOf(ConfigUtil.getConfigValue("ztb", type + "port")));
			}
		}
		this.queueName = ConfigUtil.getConfigValue("ztb", type + "queueName");
		this.exchangeName = ConfigUtil.getConfigValue("ztb", type + "exchangeName");
		this.routingKey = ConfigUtil.getConfigValue("ztb", type + "routingKey");
		String prefetchCount = ConfigUtil.getConfigValue("ztb", type + "prefetchCount");
		if (StringUtil.isNotBlank(prefetchCount)) {
			this.prefetchCount = Integer.parseInt(prefetchCount);
		}
		this.useSSL = ConfigUtil.getConfigValue("ztb", type + "usessl");
	}

	private String uri = "";

	// 主机地址
	private String host = "";

	// 如果主机是多个集群模式，用数组装载
	private Address[] addrArr = null;

	// 登录用户名
	private String userName = "";

	// 登录的密码
	private String passWord = "";

	// 虚拟主机名称
	private String vhName = "";

	// 端口
	private int port = -1;

	// 交换机名称
	private String exchangeName = "";

	// 交换机路由规则
	private String routingKey = "";

	// 队列 名称
	private String queueName = "";

	// 是否启用SSL协议
	private String useSSL = "0";

	// 是否启用断电自动重连
	private Boolean blnSupport = true;

	// 默认5分钟断线重连
	private double disreconnectionminuts = 5d;

	// 每次消费一条数据
	private int prefetchCount = 1;

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public Address[] getAddrArr() {
		return addrArr;
	}

	public void setAddrArr(Address[] addrArr) {
		this.addrArr = addrArr;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassWord() {
		return passWord;
	}

	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}

	public String getVhName() {
		return vhName;
	}

	public void setVhName(String vhName) {
		this.vhName = vhName;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getExchangeName() {
		return exchangeName;
	}

	public void setExchangeName(String exchangeName) {
		this.exchangeName = exchangeName;
	}

	public String getRoutingKey() {
		if (StringUtil.isBlank(this.routingKey)) {
			return this.queueName;
		}
		return routingKey;
	}

	public void setRoutingKey(String routingKey) {
		this.routingKey = routingKey;
	}

	public String getQueueName() {
		return queueName;
	}

	public void setQueueName(String queueName) {
		this.queueName = queueName;
	}

	public String getUseSSL() {
		return useSSL;
	}

	public void setUseSSL(String useSSL) {
		this.useSSL = useSSL;
	}

	/**
	 * 是否启用断电自动重连
	 * 
	 * @return
	 */
	public Boolean getBlnSupport() {
		return blnSupport;
	}

	public void setBlnSupport(Boolean blnSupport) {
		this.blnSupport = blnSupport;
	}

	public double getDisreconnectionminuts() {
		return disreconnectionminuts;
	}

	public void setDisreconnectionminuts(double disreconnectionminuts) {
		this.disreconnectionminuts = disreconnectionminuts;
	}

	public int getPrefetchCount() {
		return prefetchCount;
	}

	public void setPrefetchCount(int prefetchCount) {
		this.prefetchCount = prefetchCount;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}
}
