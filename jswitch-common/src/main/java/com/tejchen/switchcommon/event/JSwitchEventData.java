package com.tejchen.switchcommon.event;

import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;

@Getter
@Setter
public abstract class JSwitchEventData {

    private String operator;

    private String action;

    private String object;

    private boolean result;
}
