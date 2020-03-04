package com.tejchen.jswitchserver.mapper;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@TableName("jswitch_app")
public class JSwitchApp {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String appCode;

    private String appName;

    private String appDesc;

    private String appOwner;

    private Long appVersion;

    private String appSignKey;

    private String appSignFlag;

    private String gmtCreate;

    private String gmtModified;

    private String createUser;

    private String lastOperateUser;
}
