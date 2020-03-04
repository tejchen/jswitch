package com.tejchen.jswitchserver.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tejchen.jswitchserver.mapper.JSwitchAppNodeEvent;
import com.tejchen.jswitchserver.mapper.JSwitchAppNodeEventMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class JSwitchAppNodeEventServiceImpl extends ServiceImpl<JSwitchAppNodeEventMapper, JSwitchAppNodeEvent> implements JSwitchAppNodeEventService {
    private static final Logger logger = LoggerFactory.getLogger(JSwitchAppNodeEventServiceImpl.class);

    @Override
    public boolean collectEvent(String nodeToken, String eventCode, String eventSn, String eventData) {
        logger.info("收到客户端事件：{}, {}, {}, {}", nodeToken, eventCode, eventSn, eventData);
        // 生成一个事件
        JSwitchAppNodeEvent event = new JSwitchAppNodeEvent();
        event.setAppNodeToken(nodeToken);
        event.setAppNodeEventDate(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        event.setAppNodeEventCode(eventCode);
        event.setAppNodeEventSn(eventSn);
        event.setAppNodeEventData(eventData);
        return save(event);
    }

    @Override
    public List<JSwitchAppNodeEvent> pickEvent(String nodeToken, String eventCode, String eventSn) {
        // 默认从昨天的开始查询，历史不查询
        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date(new Date().getTime() - 1000 * 60 * 60 *24));
        Wrapper<JSwitchAppNodeEvent> wrapper = Wrappers.<JSwitchAppNodeEvent>lambdaQuery()
                .eq(JSwitchAppNodeEvent::getAppNodeToken, nodeToken)
                .ge(JSwitchAppNodeEvent::getAppNodeEventDate, date)
                .eq(JSwitchAppNodeEvent::getAppNodeEventCode, eventCode)
                .eq(JSwitchAppNodeEvent::getAppNodeEventSn, eventCode);
        List<JSwitchAppNodeEvent> result = list(wrapper);
        return result;
    }

    @Override
    public List<JSwitchAppNodeEvent> pickEvents(List<String> nodeTokens, String eventCode, String eventSn) {
        // 默认从昨天的开始查询，历史不查询
        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date(new Date().getTime() - 1000 * 60 * 60 *24));
        Wrapper<JSwitchAppNodeEvent> wrapper = Wrappers.<JSwitchAppNodeEvent>lambdaQuery()
                .in(JSwitchAppNodeEvent::getAppNodeToken, nodeTokens)
                .ge(JSwitchAppNodeEvent::getAppNodeEventDate, date)
                .eq(JSwitchAppNodeEvent::getAppNodeEventCode, eventCode)
                .eq(JSwitchAppNodeEvent::getAppNodeEventSn, eventSn);
        List<JSwitchAppNodeEvent> result = list(wrapper);
        return result;
    }
}
