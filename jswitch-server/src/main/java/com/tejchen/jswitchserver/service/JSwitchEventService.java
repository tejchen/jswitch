package com.tejchen.jswitchserver.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tejchen.jswitchserver.mapper.JSwitchEvent;

import java.util.List;

public interface JSwitchEventService extends IService<JSwitchEvent> {

    /**
     * 收集事件
     * @param eventOperator
     * @param eventOperatorName
     * @param event
     * @param eventSn
     * @param eventData
     * @return
     */
    boolean collectEvent(String eventOperator,
                         String eventOperatorName,
                         String event,
                         String eventSn,
                         String eventData,
                         String eventLevel);

    /**
     * 收集事件
     * @param eventOperator
     * @param eventOperatorName
     * @param event
     * @param eventSn
     * @param eventData
     * @param eventMessage
     * @return
    */
    boolean collectEvent(String eventOperator,
                         String eventOperatorName,
                         String event,
                         String eventSn,
                         String eventData,
                         String eventLevel,
                         String eventMessage);

    /**
     * 获取事件
     * @param eventOperator
     * @param event
     * @param eventSn
     * @return
     */
    List<JSwitchEvent> pickEvent(String eventOperator, String event, String eventSn);

    /**
     * 获取事件
     * @param eventOperators
     * @param event
     * @param eventSn
     * @return
     */
    List<JSwitchEvent> pickEvents(List<String> eventOperators, String event, String eventSn);

    /**
     * 事件总数
     * @param duration
     * @param eventLevel
     * @return
     */
    Integer eventCount(Integer duration, String eventLevel);

    /**
     * 事件列表
     * @param duration
     * @return
     */
    List<JSwitchEvent> list(Integer duration, Integer size);
}
