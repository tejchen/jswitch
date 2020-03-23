package com.tejchen.jswitchserver.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tejchen.jswitchserver.mapper.JSwitchApp;

import java.util.List;

public interface JSwitchAppService extends IService<JSwitchApp> {

    Long pushAndGet(String appCode);

    JSwitchApp get(String appCode);

    List<JSwitchApp> list(List<String> appCodeList);
}
