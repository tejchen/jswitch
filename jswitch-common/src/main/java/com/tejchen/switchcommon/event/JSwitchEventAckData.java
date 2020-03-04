package com.tejchen.switchcommon.event;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class JSwitchEventAckData extends JSwitchEventData {

    /**
     * ack 场景编码
     */
    private String code;

    /**
     * ack 对应的标示
     */
    private String flag;

}
