package com.tejchen.jswitchserver.mapper;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@TableName("jswitch_app_node")
public class JSwitchAppNode {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String appCode;

    private String appNodeIp;

    private String appNodeToken;

    private String gmtCreate;

    private Date lastHeartbeatTime;

}
