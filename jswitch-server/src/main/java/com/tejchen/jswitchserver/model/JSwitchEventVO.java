package com.tejchen.jswitchserver.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JSwitchEventVO {

    private String gmtCreate;

    private String event;

    private String eventSn;

    private String eventLevel;

    private String eventMessage;
}
