package com.tejchen.jswitchserver.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tejchen.jswitchserver.mapper.JSwitchAppNode;
import com.tejchen.jswitchserver.mapper.JSwitchAppNodeMapper;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class JSwitchAppNodeServiceImpl extends ServiceImpl<JSwitchAppNodeMapper, JSwitchAppNode> implements JSwitchAppNodeService {

    // 20s内 还在心跳的，认为是存活的
    private Integer duration = 1000 * 20;

    @Override
    public List<JSwitchAppNode> getAliveNodeList(String appCode) {
        String aliveTime = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date(new Date().getTime()-duration));
        List<JSwitchAppNode> nodes = list(Wrappers.<JSwitchAppNode>lambdaQuery()
                .eq(JSwitchAppNode::getAppCode, appCode)
                .gt(JSwitchAppNode::getLastHeartbeatTime, aliveTime)
        );
        return nodes;
    }

    @Override
    public List<JSwitchAppNode> getAliveNodeList(List<String> appCodes) {
        String aliveTime = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date(new Date().getTime()-duration));
        List<JSwitchAppNode> nodes = list(Wrappers.<JSwitchAppNode>lambdaQuery()
                .in(JSwitchAppNode::getAppCode, appCodes)
                .gt(JSwitchAppNode::getLastHeartbeatTime, aliveTime)
        );
        return nodes;
    }
}
