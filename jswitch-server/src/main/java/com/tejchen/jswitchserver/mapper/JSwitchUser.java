package com.tejchen.jswitchserver.mapper;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@TableName("jswitch_user")
public class JSwitchUser {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String userId;

    private String userName;

    private String userPassword;

    private String userLevel;

    private String userPhone;

    private String gmtCreate;

    private String gmtModified;
}
