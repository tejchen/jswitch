package com.tejchen.jswitchserver.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tejchen.jswitchserver.mapper.JSwitchAppNode;

import java.util.List;

public interface JSwitchAppNodeService extends IService<JSwitchAppNode> {

    List<JSwitchAppNode> getAliveNodeList(String appCode);

    List<JSwitchAppNode> getAliveNodeList(List<String> appCodes);
}
