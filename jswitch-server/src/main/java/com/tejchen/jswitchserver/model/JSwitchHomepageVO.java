package com.tejchen.jswitchserver.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class JSwitchHomepageVO {

    private long nodeCount;

    private long appCount;

    private long configCount;

    private long errorCount;

    private List<JSwitchEventVO> eventList;

    private List<JSwitchAppVO> favoriteAppList;
}
