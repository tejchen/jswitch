package com.tejchen.switchcommon.protocol.http;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Setter
@Getter
public class JSwitchPushForm {

    @NotNull
    private String appName;

    @Valid
    @NotEmpty
    private List<JSwitchPushItem> pushConfig;

}
