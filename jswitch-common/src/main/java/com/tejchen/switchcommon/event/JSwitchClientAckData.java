package com.tejchen.switchcommon.event;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class JSwitchClientAckData extends JSwitchEventData {

    /**
     * ack 场景编码
     */
    private String code;

    /**
     * ack 标示
     */
    private String flag;

    /**
     * ack 消息
     */
    private String message;

}
