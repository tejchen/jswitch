package com.tejchen.switchcommon.protocol.http;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class JSwitchHttpPage<T> {

    private Long pageNo;

    private Long pageSize;

    private Long total;

    private Long totalPage;

    private List<T> dataList;

}
