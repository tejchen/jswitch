package com.tejchen.jswitchserver.web.enhancer;

import com.tejchen.jswitchserver.base.JSwitchWebEvent;
import com.tejchen.jswitchserver.service.JSwitchEventService;
import com.tejchen.switchcommon.event.DefaultEventData;
import com.tejchen.switchcommon.event.EventLevel;
import com.tejchen.switchcommon.helper.SerializeHelper;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.lang.reflect.Field;
import java.lang.reflect.Parameter;
import java.util.Date;

@Component
@Aspect
@Order // 默认最不优先处理
public class WebEventAspect {
    private static final Logger logger = LoggerFactory.getLogger(WebEventAspect.class);

    @Autowired
    private JSwitchEventService eventService;

    @AfterReturning(value = "@annotation(webEvent)")
    public void returning(JoinPoint point, JSwitchWebEvent webEvent) {
        // 只处理有入参的方法
        if (point.getArgs() == null || point.getArgs().length == 0){
            return;
        }
        // 操纵人
        String userLoginId = "admin";
        // 操作人名称
        String userName = "管理员";
        // 操作动作
        String eventAction = webEvent.action();
        // 操作对象
        String eventObject = getEventObject(point, webEvent.object());
        // 操作事件
        String event = point.getTarget().getClass().getSimpleName() + "#" +((MethodSignature)point.getSignature()).getMethod().getName();
        // 事件序列号
        String eventSn = event + "_" + userLoginId + "_" + new Date().getTime();
        // 校验参数
        if (!checkRequired(userLoginId, eventObject, event, eventSn)){
            return;
        }
        // 记录事件
        DefaultEventData data = new DefaultEventData();
        data.setOperator(userLoginId);
        data.setAction(eventAction);
        data.setObject(eventObject);
        data.setResult(true);
        SerializeHelper.serializeJson(data);
        try {
            eventService.collectEvent(userLoginId, userName, event, eventSn, SerializeHelper.serializeJson(data), EventLevel.INFO);
        }catch (Exception e){
            logger.error("事件记录异常", e);
            DefaultEventData warningData = new DefaultEventData();
            warningData.setOperator("system");
            warningData.setAction("事件记录异常");
            warningData.setObject(e.getMessage());
            warningData.setResult(false);
            eventService.collectEvent("system", "system", "事件记录异常", String.valueOf(new Date().getTime()), SerializeHelper.serializeJson(warningData), EventLevel.ERROR);
        }
        return;
    }

    private boolean checkRequired(String userLoginId, String eventObject, String event, String eventSn) {
        if (userLoginId == null || eventObject == null || event == null || eventSn == null) {
            logger.info("事件记录失败, {}, {}, {}, {}", userLoginId, eventObject, event, eventSn);
            return false;
        }
        return true;
    }

    private String getEventObject(JoinPoint point , String object) {
        for (int i = 0; i < point.getArgs().length; i++) {
            Parameter parameter = ((MethodSignature)point.getSignature()).getMethod().getParameters()[i];
            RequestParam requestParam = parameter.getDeclaredAnnotation(RequestParam.class);
            if (requestParam != null){
                if (requestParam.name().equals(object) || parameter.getName().equals(object)){
                    return String.valueOf(point.getArgs()[i]);
                }
            }
            PathVariable pathVariable = parameter.getDeclaredAnnotation(PathVariable.class);
            if (pathVariable != null){
                if (pathVariable.name().equals(object)){
                    return String.valueOf(point.getArgs()[i]);
                }
            }
            for (Field field : parameter.getType().getDeclaredFields()) {
                if (field.getName().equals(object)){
                    try {
                        field.setAccessible(true);
                        return String.valueOf(field.get(point.getArgs()[i]));
                    } catch (IllegalAccessException e) {
                        logger.error("getEventObject:invalid args!", e);
                    }
                }
            }
        }

        return null;
    }
}
