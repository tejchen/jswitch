package com.tejchen.jswitchserver.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tejchen.jswitchserver.base.BizResult;
import com.tejchen.jswitchserver.base.ServerBizException;
import com.tejchen.jswitchserver.mapper.JSwitchEvent;
import com.tejchen.jswitchserver.mapper.JSwitchEventMapper;
import com.tejchen.jswitchserver.mapper.JSwitchUser;
import com.tejchen.jswitchserver.mapper.JSwitchUserMapper;
import com.tejchen.switchcommon.event.DefaultEventData;
import com.tejchen.switchcommon.helper.SerializeHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class JSwitchUserServiceImpl extends ServiceImpl<JSwitchUserMapper, JSwitchUser> implements JSwitchUserService {
    private static final Logger logger = LoggerFactory.getLogger(JSwitchUserServiceImpl.class);

}
