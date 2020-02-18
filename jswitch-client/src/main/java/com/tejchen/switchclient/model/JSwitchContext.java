package com.tejchen.switchclient.model;

import com.tejchen.switchcommon.JSwitchServer;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JSwitchContext {

    private String          serverUrl;
    private JSwitchServer   server;

}
