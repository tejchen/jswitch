package com.tejchen.switchcommon.protocol.http.form;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Setter
@Getter
public class JSwitchPushForm extends JSwitchBaseForm{

    @Valid
    @NotEmpty
    private List<JSwitchPushItem> pushConfig;

    @Override
    public String getSignValue() {
        return null;
    }
}
