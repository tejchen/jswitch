package com.tejchen.switchcommon.protocol.http;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 拉取表单
 */
@Getter
@Setter
public class JSwitchHttpBatchForm {

    @NotNull
    private String appName;

    @NotEmpty
    private List<String> keys;
}
