package com.epoint.msg.operate;

import java.io.Serializable;

/**
 * 消息比较适用于不同系统内部的数据交换
 * 
 * @author ZHZHH
 */
public class MqMessage implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8814946281933787915L;

	/*
	 * 消息的类目-种类
	 */
	private String category;

	/*
	 * 消息的唯一标示符
	 */
	private String correlationId;//

	/*
	 * 消息内容
	 */
	private Object content;

	/*
	 * xml解析入库结果
	 */
	private boolean result;

	/*
	 * 错误码，预留
	 */
	private String errorcode;

	/*
	 * 发送时间
	 */
	private String sendDate;

	public String getCorrelationId() {
		return correlationId;
	}

	public void setCorrelationId(String correlationId) {
		this.correlationId = correlationId;
	}

	public Object getContent() {
		return content;
	}

	public void setContent(Object content) {
		this.content = content;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public boolean isResult() {
		return result;
	}

	public void setResult(boolean result) {
		this.result = result;
	}

	public String getErrorcode() {
		return errorcode;
	}

	public void setErrorcode(String errorcode) {
		this.errorcode = errorcode;
	}

	public String getSendDate() {
		return sendDate;
	}

	public void setSendDate(String sendDate) {
		this.sendDate = sendDate;
	}
}
