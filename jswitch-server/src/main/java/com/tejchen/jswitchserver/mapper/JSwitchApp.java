package com.tejchen.jswitchserver.mapper;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@TableName("jswitch_app")
public class JSwitchApp {

    private Long id;

    private String appCode;

    private String appName;

    private String appDesc;

    private String owner;

    private String gmtCreate;

    private String gmtModified;

}
