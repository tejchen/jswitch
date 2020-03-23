package com.tejchen.switchcommon.protocol.http.form;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;


@Setter
@Getter
public class JSwitchEventForm{

    /**
     * 事件编码
     */
    @NotEmpty
    private String eventCode;

    /**
     * 事件序列
     */
    @NotEmpty
    private String eventSn;

    /**
     * 事件数据
     */
    @NotEmpty
    private String eventData;

    /**
     * 事件类型
     */
    @NotEmpty
    private String evenLevel;

    /**
     * 机器指纹
     */
    @NotEmpty
    private String token;

}
