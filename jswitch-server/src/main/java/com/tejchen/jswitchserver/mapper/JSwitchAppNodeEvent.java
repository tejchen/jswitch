package com.tejchen.jswitchserver.mapper;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@TableName("jswitch_app_node_event")
public class JSwitchAppNodeEvent {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String appNodeToken;

    private String appNodeEventCode;

    private String appNodeEventDate;

    private String appNodeEventSn;

    private String appNodeEventData;

    private Date gmtCreate;

}
