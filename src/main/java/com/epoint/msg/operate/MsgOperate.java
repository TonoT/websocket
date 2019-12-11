package com.epoint.msg.operate;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import com.epoint.core.utils.string.StringUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class MsgOperate extends HttpServlet {

	private static final long serialVersionUID = 3681825410716897665L;
	private static Logger log = LogManager.getLogger(MsgOperate.class);

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 获取队列名称
		String queueName = MsgCommon.queueName;
		JSONObject jb = new JSONObject();
		String status = "1";
		// 获取跳转的节点属性值
		String userStr = readJSONString(request);
		JSONObject jsonObj = JSONObject.fromObject(userStr);
		String href = jsonObj.getString("path");
		String sessionId = request.getRequestedSessionId();
		if (href.indexOf("?") > 0) {
			href = href + "&&sid=" + sessionId;
		} else {
			href = href + "?sid=" + sessionId;
		}
		// 当前时间精确到毫秒
		String time = MsgCommon.sdfTimeStamp.format(new Date());
		// 需要传递的数据
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("actiontype", MsgEnum.ActionType.OpenUrl.getValue()); // 动作类型，打开网页
		map.put("actionargs", href); // 跳转地址
		if (StringUtil.isBlank(href)) {
			return;
		}
		JSONArray json = JSONArray.fromObject(map);
		String jsonContent = String.valueOf(json);
		MqMessage msg = new MqMessage();
		// 消息的类目-种类
		msg.setCategory("001-" + queueName);
		// 消息的唯一标示符
		msg.setCorrelationId(UUID.randomUUID().toString());
		msg.setSendDate(time);
		msg.setContent(jsonContent);
		BaseMQProducer pro = new BaseMQProducer("BigData");
		try {
			pro.publishMessage(queueName, msg);
			pro.free();
		} catch (Exception e) {
			status = "0";
			log.error("插入消息队列" + queueName + "错误!", e);
		}
		jb.put("status", status);
		response.getWriter().write(jb.toString());
	}

	public String readJSONString(HttpServletRequest request) {
		StringBuffer json = new StringBuffer();
		String line = null;
		try {
			BufferedReader reader = request.getReader();
			while ((line = reader.readLine()) != null) {
				json.append(line);
			}
		} catch (Exception e) {
			log.error("读取json数据错误!", e);
		}
		return String.valueOf(json);
	}
}
