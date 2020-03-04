package com.tejchen.switchcommon.helper;

import com.tejchen.switchcommon.event.AckFLag;
import com.tejchen.switchcommon.event.JSwitchEvent;
import com.tejchen.switchcommon.event.JSwitchEventAckData;
import com.tejchen.switchcommon.event.JSwitchEventWrapper;

public class EventHelper {

    /**
     * 成功处理推送
     * @param version
     * @return
     */
    public static JSwitchEventWrapper acceptedPush(String appCode, String version){
        JSwitchEventWrapper event = new JSwitchEventWrapper();
        event.setEventCode(JSwitchEvent.ACCEPT_ACK.getCode());
        event.setToken(NodeHelper.generateToken());
        event.setEventSn(appCode+"_"+version);
        event.setEventData(new JSwitchEventAckData(){{
            setCode(JSwitchEvent.ACCEPT_ACK.getCode());
            setFlag(AckFLag.SUCCESS);
        }});
        return event;
    }

    /**
     * 失败处理推送
     * @param version
     * @return
     */
    public static JSwitchEventWrapper unacceptedPush(String appCode, String version){
        JSwitchEventWrapper event = new JSwitchEventWrapper();
        event.setEventCode(JSwitchEvent.ACCEPT_ACK.getCode());
        event.setToken(NodeHelper.generateToken());
        event.setEventSn(appCode+"_"+version);
        event.setEventData(new JSwitchEventAckData(){{
            setCode(JSwitchEvent.ACCEPT_ACK.getCode());
            setFlag(AckFLag.FAIL);
        }});
        return event;
    }
}
