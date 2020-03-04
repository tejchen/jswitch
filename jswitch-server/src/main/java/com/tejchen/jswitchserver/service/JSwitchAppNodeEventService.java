package com.tejchen.jswitchserver.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tejchen.jswitchserver.mapper.JSwitchAppNode;
import com.tejchen.jswitchserver.mapper.JSwitchAppNodeEvent;

import java.util.List;

public interface JSwitchAppNodeEventService extends IService<JSwitchAppNodeEvent> {

    /**
     * 收集事件
     * @param nodeToken
     * @param eventCode
     * @param eventSn
     * @param eventData
     * @return
     */
    boolean collectEvent(String nodeToken, String eventCode, String  eventSn, String eventData);

    /**
     * 获取事件
     * @param nodeToken
     * @param eventCode
     * @param eventSn
     * @return
     */
    List<JSwitchAppNodeEvent> pickEvent(String nodeToken, String eventCode, String eventSn);

    /**
     * 获取事件
     * @param nodeTokens
     * @param eventCode
     * @param eventSn
     * @return
     */
    List<JSwitchAppNodeEvent> pickEvents(List<String> nodeTokens, String eventCode, String eventSn);
}
