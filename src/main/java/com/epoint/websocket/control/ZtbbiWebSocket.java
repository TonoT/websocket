package com.epoint.websocket.control;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Logger;
import org.atmosphere.config.service.Disconnect;
import org.atmosphere.config.service.Heartbeat;
import org.atmosphere.config.service.ManagedService;
import org.atmosphere.config.service.Message;
import org.atmosphere.config.service.Ready;
import org.atmosphere.cpr.AtmosphereResource;
import org.atmosphere.cpr.AtmosphereResourceEvent;
import org.atmosphere.cpr.Broadcaster;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.epoint.core.utils.string.StringUtil;

@ManagedService(path = "/websocket/ztbbi", atmosphereConfig = {
        "org.atmosphere.cpr.CometSupport.maxInactiveActivity=120000" })
public class ZtbbiWebSocket
{

    transient private static Logger logger = Logger.getLogger("com.epoint.ztbbi.control.action.ZtbbiWebSocket");

    @Inject
    private AtmosphereResource r;

    @Inject
    @Named("/websocket/ztbbi")
    private static Broadcaster broadcaster;

    /**
     * 打开连接时触发
     */
    @Ready
    public void onReady() {
        String BiaoDiGuid = r.getRequest().getHeader("biaoDiGuid");
        String DanWeiGuid = r.getRequest().getHeader("danWeiGuid");
        logger.debug("user connected :BiaoDiGuid:" + BiaoDiGuid + ",DanWeiGuid:" + DanWeiGuid + ",uuid:"
                + r.getRequest().uuid());
    }

    /**
     * 关闭连接时触发
     */
    @Disconnect
    public void onDisconnect(AtmosphereResourceEvent event) {
        String BiaoDiGuid = r.getRequest().getHeader("biaoDiGuid");
        String DanWeiGuid = r.getRequest().getHeader("danWeiGuid");
        if (event.isCancelled()) {// 非正常关闭时触发
            logger.debug(
                    "用户意外关闭：BiaoDiGuid:" + BiaoDiGuid + ",DanWeiGuid:" + DanWeiGuid + ",uuid:" + r.getRequest().uuid());
        }
        else if (event.isClosedByClient()) {// 关闭连接时触发
            logger.debug(
                    "用户退出：BiaoDiGuid:" + BiaoDiGuid + ",DanWeiGuid:" + DanWeiGuid + ",uuid:" + r.getRequest().uuid());
        }
    }

    /**
     * 接收到消息时触发
     */
    @Message
    public void onMessage(String data) {
        logger.debug("onMessage:" + data + ",uuid:" + r.uuid());
        if (StringUtil.isBlank(data))
            return;
        // 解析
        if (data.startsWith("{")) {
            JSONObject json = JSON.parseObject(data);
            if ("Enter".equals(json.get("Type"))) {// 新用户进入
                String BiaoDiGuid = r.getRequest().getHeader("biaoDiGuid");
                String DanWeiGuid = r.getRequest().getHeader("danWeiGuid");
                logger.debug("新用户加入：BiaoDiGuid:" + BiaoDiGuid + ",DanWeiGuid:" + DanWeiGuid + ",uuid:" + r.uuid());

                // 回复成功，这个只需要单发客户端，过滤器中通过uuid过滤
                JSONObject msg = new JSONObject();
                msg.put("flag", 0);
                msg.put("t", System.currentTimeMillis());
                msg.put("uuid", r.uuid());
                Broadcast(msg);
            }
            else if ("Check".equals(json.get("Type"))) {
                // 这个只需要单发客户端，过滤器中通过uuid过滤
                JSONObject msg = new JSONObject();
                msg.put("flag", 99);
                msg.put("q", json.get("postQueue"));
                msg.put("t", System.currentTimeMillis());
                msg.put("uuid", r.uuid());
                Broadcast(msg);
            }
        }
    }

    /**
     * 心跳方法，定时触发，web.xml可以修改频率
     */
    @Heartbeat
    public void onHeartbeat(AtmosphereResourceEvent event) {
        logger.debug("Heartbeat send by {" + event.getResource().uuid() + "}");
    }

    /**
     * 给客户端广播消息
     * 
     * @param signalRMessage
     *            必须是SignalRMessage对象的json串
     */
    public static void BroadMsg(String signalRMessage) {
        Broadcast(signalRMessage);
    }

    /**
     * 给客户端广播消息
     * 
     * @param BiaoDiGuid
     * @param DanWeiGuid
     * @param msgContent
     */
    private static void Broadcast(Object msgContent) {

        if (broadcaster == null) {
            // 说明没有客户端连接
            logger.info("broadcaster is null");
            return;
        }
        if (msgContent instanceof String) {
            broadcaster.broadcast(msgContent);
            // broadcaster.broadcast(JSONObject.parseObject((String)
            // msgContent));
        }
        else if (msgContent instanceof JSONObject || msgContent instanceof JSONArray) {
            // broadcaster.broadcast(msgContent);
            broadcaster.broadcast(JSON.toJSONString(msgContent));
        }
        else {
            // broadcaster.broadcast(JSON.toJSON(msgContent));
            broadcaster.broadcast(JSON.toJSONString(msgContent));
        }
    }

}
