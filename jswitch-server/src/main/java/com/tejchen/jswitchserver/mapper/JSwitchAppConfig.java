package com.tejchen.jswitchserver.mapper;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@TableName("jswitch_app_config")
public class JSwitchAppConfig {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String appCode;

    private String appConfigCode;

    private String appConfigName;

    private String appConfigContent;

    private String appConfigDesc;

    private String appConfigSource;

    private String gmtCreate;

    private String gmtModified;

    private String createUser;

    private String lastOperateUser;
}
