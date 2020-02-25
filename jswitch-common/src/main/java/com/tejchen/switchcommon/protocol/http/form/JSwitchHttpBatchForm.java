package com.tejchen.switchcommon.protocol.http.form;

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
public class JSwitchHttpBatchForm extends JSwitchBaseForm {

    @NotEmpty
    private List<String> keys;

    @Override
    public String getSignValue() {
        return null;
    }
}
