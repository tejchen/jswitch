package com.tejchen.jswitchserver.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.util.List;


@Setter
@Getter
public class JSwitchAppConfigPushResultForm {

    @NotEmpty
    private String appCode;

    @NotNull
    private Long appVersion;

    @NotEmpty
    private List<String> appNodeTokens;

}
