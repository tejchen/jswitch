package com.tejchen.jswitchserver.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tejchen.jswitchserver.base.BizResult;
import com.tejchen.jswitchserver.base.ServerBizException;
import com.tejchen.jswitchserver.mapper.JSwitchEvent;
import com.tejchen.jswitchserver.mapper.JSwitchEventMapper;
import com.tejchen.switchcommon.event.DefaultEventData;
import com.tejchen.switchcommon.event.JSwitchEventData;
import com.tejchen.switchcommon.helper.SerializeHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class JSwitchEventServiceImpl extends ServiceImpl<JSwitchEventMapper, JSwitchEvent> implements JSwitchEventService {
    private static final Logger logger = LoggerFactory.getLogger(JSwitchEventServiceImpl.class);

    private static final int dayDuration = 1000 * 60 * 60 * 24;

    @Override
    public boolean collectEvent(String eventOperator,
                                String eventOperatorName,
                                String event,
                                String eventSn,
                                String eventData,
                                String eventLevel) {
        logger.info("收到客户端事件：{}, {}, {}, {}", eventOperator, event, eventSn, eventData);
        // 生成一个事件
        JSwitchEvent record = new JSwitchEvent();
        record.setEvent(event);
        record.setEventSn(eventSn);
        record.setEventData(eventData);
        record.setEventLevel(eventLevel);
        record.setEventMessage(defaultMessage(eventOperatorName, eventData));
        record.setEventOperator(eventOperator);
        record.setEventOperatorName(eventOperatorName);
        record.setEventDate(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        return save(record);
    }

    private String defaultMessage(String eventOperatorName, String eventData){
        DefaultEventData data = SerializeHelper.deserializeJson(eventData, DefaultEventData.class);
        return String.format("%s %s %s", eventOperatorName, data.getAction(), data.getObject());
    }

    @Override
    public boolean collectEvent(String eventOperator,
                                String eventOperatorName,
                                String event,
                                String eventSn,
                                String eventData,
                                String eventLevel,
                                String eventMessage) {
        logger.info("收到客户端事件：{}, {}, {}, {}", eventOperator, event, eventSn, eventData);
        // 生成一个事件
        JSwitchEvent record = new JSwitchEvent();
        record.setEvent(event);
        record.setEventSn(eventSn);
        record.setEventData(eventData);
        record.setEventLevel(eventLevel);
        record.setEventMessage(eventMessage);
        record.setEventOperator(eventOperator);
        record.setEventOperatorName(eventOperatorName);
        record.setEventDate(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        return save(record);
    }

    @Override
    public List<JSwitchEvent> pickEvent(String operator, String event, String eventSn) {
        // 默认从昨天的开始查询，历史不查询
        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date(new Date().getTime() - dayDuration));
        Wrapper<JSwitchEvent> wrapper = Wrappers.<JSwitchEvent>lambdaQuery()
                .eq(JSwitchEvent::getEvent, event)
                .eq(JSwitchEvent::getEventSn, eventSn)
                .ge(JSwitchEvent::getEventDate, date)
                .eq(JSwitchEvent::getEventOperator, operator);
        List<JSwitchEvent> result = list(wrapper);
        return result;
    }

    @Override
    public List<JSwitchEvent> pickEvents(List<String> operators, String event, String eventSn) {
        // 默认从昨天的开始查询，历史不查询
        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date(new Date().getTime() - 1000 * 60 * 60 *24));
        Wrapper<JSwitchEvent> wrapper = Wrappers.<JSwitchEvent>lambdaQuery()
                .eq(JSwitchEvent::getEvent, event)
                .eq(JSwitchEvent::getEventSn, eventSn)
                .ge(JSwitchEvent::getEventDate, date)
                .in(JSwitchEvent::getEventOperator, operators);
        List<JSwitchEvent> result = list(wrapper);
        return result;
    }

    @Override
    public Integer eventCount(Integer duration, String eventLevel) {
        if (duration == null || eventLevel == null){
            logger.warn("字段不能为空");
            return 0;
        }
        Date startTime = new Date(new Date().getTime() - dayDuration * duration);
        String date = new SimpleDateFormat("yyyy-MM-dd").format(startTime);
        int count = count(Wrappers.<JSwitchEvent>lambdaQuery()
                .gt(JSwitchEvent::getEventDate, date)
                .eq(JSwitchEvent::getEventLevel, eventLevel));
        return count;
    }

    @Override
    public List<JSwitchEvent> list(Integer duration, Integer size) {
        if (duration == null){
            logger.error("字段不能为空");
            ServerBizException.throwException(BizResult.PARAMETER_CHECK_FAIL);
        }
        Date startTime = new Date(new Date().getTime() - dayDuration * duration);
        String date = new SimpleDateFormat("yyyy-MM-dd").format(startTime);
        List<JSwitchEvent> resultList = list(Wrappers.<JSwitchEvent>lambdaQuery()
                .gt(JSwitchEvent::getEventDate, date)
                .orderByDesc(JSwitchEvent::getGmtCreate)
                .last("limit " + size));
        return resultList;
    }
}
