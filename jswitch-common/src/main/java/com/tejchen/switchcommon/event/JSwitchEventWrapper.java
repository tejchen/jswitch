package com.tejchen.switchcommon.event;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Setter
@Getter
@ToString
public class JSwitchEventWrapper {


    private String event;

    private String eventSn;

    private String eventLevel;

    private JSwitchEventData eventData;

    private String token;

}
