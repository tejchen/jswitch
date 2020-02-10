package com.tejchen.switchclient.model;

import com.tejchen.switchcommon.ServerEnum;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SwitchContext {

    private String      serverUrl;
    private ServerEnum  server;

}
