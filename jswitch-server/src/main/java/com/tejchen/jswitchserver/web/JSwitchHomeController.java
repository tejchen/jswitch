package com.tejchen.jswitchserver.web;

import com.tejchen.jswitchserver.base.FavoriteType;
import com.tejchen.jswitchserver.helper.ResponseHelper;
import com.tejchen.jswitchserver.mapper.JSwitchApp;
import com.tejchen.jswitchserver.mapper.JSwitchEvent;
import com.tejchen.jswitchserver.mapper.JSwitchUserFavorite;
import com.tejchen.jswitchserver.model.JSwitchAppListVO;
import com.tejchen.jswitchserver.model.JSwitchAppVO;
import com.tejchen.jswitchserver.model.JSwitchEventVO;
import com.tejchen.jswitchserver.model.JSwitchHomepageVO;
import com.tejchen.jswitchserver.service.JSwitchAppConfigService;
import com.tejchen.jswitchserver.service.JSwitchAppNodeService;
import com.tejchen.jswitchserver.service.JSwitchAppService;
import com.tejchen.jswitchserver.service.JSwitchEventService;
import com.tejchen.jswitchserver.service.JSwitchUserFavoriteService;
import com.tejchen.switchcommon.event.EventLevel;
import com.tejchen.switchcommon.protocol.http.JSwitchHttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/jswitch")
public class JSwitchHomeController {

    @Autowired
    private JSwitchAppNodeService nodeService;

    @Autowired
    private JSwitchAppService appService;

    @Autowired
    private JSwitchEventService eventService;

    @Autowired
    private JSwitchAppConfigService appConfigService;

    @Autowired
    private JSwitchUserFavoriteService favoriteService;

    @RequestMapping("/home")
    public JSwitchHttpResponse home(){
        JSwitchHomepageVO vo = new JSwitchHomepageVO();
        vo.setAppCount(appService.count());
        vo.setConfigCount(appConfigService.count());
        vo.setNodeCount(nodeService.getAliveNodeCount());
        vo.setErrorCount(eventService.eventCount(7, EventLevel.ERROR));
        // 事件列表
        List<JSwitchEvent> eventList = eventService.list(7, 10);
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        List<JSwitchEventVO> eventVOList = eventList.stream().map(x->{
            JSwitchEventVO eventVO = new JSwitchEventVO();
            eventVO.setEvent(x.getEvent());
            eventVO.setEventSn(x.getEventSn());
            eventVO.setEventLevel(x.getEventLevel());
            eventVO.setGmtCreate(format.format(x.getGmtCreate()));
            eventVO.setEventMessage(x.getEventMessage());
            return eventVO;
        }).collect(Collectors.toList());
        vo.setEventList(eventVOList);
        // 用户关注
        List<JSwitchUserFavorite> favoriteList = favoriteService.list("admin", FavoriteType.APP, null);
        if (favoriteList != null && !favoriteList.isEmpty()){
            List<String> appCodeList = favoriteList.stream().map(x->x.getFavoriteObject()).collect(Collectors.toList());
            List<JSwitchApp> appList = appService.list(appCodeList);
            List<JSwitchAppVO> appVOList = new ArrayList<>();
            appList.forEach(app->{
                JSwitchAppVO appVO = new JSwitchAppVO();
                appVO.setAppCode(app.getAppCode());
                appVO.setAppName(app.getAppName());
                appVO.setAppDesc(app.getAppDesc());
                appVO.setAppOwner(app.getAppOwner());
                appVO.setAppMember(new ArrayList<String>(){{add("测试成员1");add("测试成员2");}});
                appVO.setGmtModified(app.getGmtModified());
                appVO.setLastOperator(app.getLastOperateUser());
                appVO.setIsFavorite("Y");
                appVOList.add(appVO);
            });
            vo.setFavoriteAppList(appVOList);
        }
        return ResponseHelper.success(vo);
    }

}
