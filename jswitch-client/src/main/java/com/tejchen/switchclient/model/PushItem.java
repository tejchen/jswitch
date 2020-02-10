package com.tejchen.switchclient.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
class PushItem {
    private String configApp;

    private String configKey;

    private String defaultValue;
}
