package com.tejchen.switchcommon.event;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Setter
@Getter
@ToString
public class JSwitchEventWrapper {

    private String eventCode;

    private String eventSn;

    private String token;

    private JSwitchEventData eventData;
}
