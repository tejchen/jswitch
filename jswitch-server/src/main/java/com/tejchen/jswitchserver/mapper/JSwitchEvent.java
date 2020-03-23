package com.tejchen.jswitchserver.mapper;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@TableName("jswitch_event")
public class JSwitchEvent {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String event;

    private String eventSn;

    private String eventData;

    private String eventMessage;

    private String eventOperator;

    private String eventOperatorName;

    private String eventLevel;

    private String EventDate;

    private Date gmtCreate;

}
