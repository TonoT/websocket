package com.epoint.msg.operate;

import java.text.SimpleDateFormat;

import com.epoint.core.utils.config.ConfigUtil;

public class MsgCommon {
	// 通用时间转类型，精确到毫秒
	public static SimpleDateFormat sdfTimeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
	// 队列名称
	public static String queueName = ConfigUtil.getConfigValue("ztb", "BigData.queueName");
}
