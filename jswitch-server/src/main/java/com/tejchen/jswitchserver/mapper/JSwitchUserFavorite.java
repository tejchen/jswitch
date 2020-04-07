package com.tejchen.jswitchserver.mapper;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@TableName("jswitch_user_favorite")
public class JSwitchUserFavorite {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String userId;

    private String favoriteType;

    private String favoriteObject;

    private String gmtCreate;
}
