package com.tejchen.switchcommon.helper;

import com.tejchen.switchcommon.event.*;

public class EventHelper {

    /**
     * 成功处理推送
     * @param version
     * @return
     */
    public static JSwitchEventWrapper acceptedPush(String appCode, String version){
        JSwitchEventWrapper event = new JSwitchEventWrapper();
        event.setEvent(JSwitchClientEvent.ACCEPT_ACK.getCode());
        event.setToken(NodeHelper.generateToken());
        event.setEventSn(appCode+"_"+version);
        event.setEventLevel(EventLevel.SUCCESS);
        event.setEventData(new JSwitchClientAckData(){{
            setOperator(NodeHelper.generateToken());
            setAction("接收配置推送");
            setObject(appCode);
            setResult(true);
            setCode(JSwitchClientEvent.ACCEPT_ACK.getCode());
            setFlag(AckFLag.SUCCESS);
        }});
        return event;
    }

    /**
     * 失败处理推送
     * @param version
     * @return
     */
    public static JSwitchEventWrapper unacceptedPush(String appCode, String version, String message){
        JSwitchEventWrapper event = new JSwitchEventWrapper();
        event.setEvent(JSwitchClientEvent.ACCEPT_ACK.getCode());
        event.setToken(NodeHelper.generateToken());
        event.setEventSn(appCode+"_"+version);
        event.setEventLevel(EventLevel.ERROR);
        event.setEventData(new JSwitchClientAckData(){{
            setOperator(NodeHelper.generateToken());
            setAction("接收配置推送");
            setObject(appCode);
            setResult(false);
            setCode(JSwitchClientEvent.ACCEPT_ACK.getCode());
            setFlag(AckFLag.FAIL);
            setMessage(message);
        }});
        return event;
    }
}
