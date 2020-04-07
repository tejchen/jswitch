CREATE TABLE `jswitch_app` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `app_code` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '应用编码',
  `app_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '应用名称',
  `app_desc` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT '' COMMENT '应用描述',
  `app_owner` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT '应用owner',
  `app_sign_key` varchar(50) COLLATE utf8_bin DEFAULT NOT NULL COMMENT '秘钥',
  `app_sign_flag` varchar(10) COLLATE utf8_bin DEFAULT NOT NULL COMMENT '是否校验签名',
  `app_version` bigint(20) unsigned NOT NULL DEFAULT '1' COMMENT 'app配置版本号',
  `create_user` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT '创建人',
  `last_operate_user` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT '最后操作人',
  `gmt_create` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='应用列表';

CREATE TABLE `jswitch_app_config`(
	`id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '主键id',
	`app_code` varchar(50) NOT NULL DEFAULT '' COMMENT '应用编码',
	`app_config_code` varchar(50) NOT NULL DEFAULT ''  COMMENT '配置编码',
	`app_config_name` varchar(50) NOT NULL DEFAULT ''  COMMENT '配置名称',
	`app_config_content` varchar(50) NOT NULL DEFAULT ''  COMMENT '配置内容',
	`app_config_desc` varchar(1000) null default ''  COMMENT '配置描述',
	`app_config_type` varchar(50) null default ''  COMMENT '配置类型',
	`app_config_source` varchar(50) NOT NULL DEFAULT ''  COMMENT '配置来源',
	`create_user` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT '创建人',
    `last_operate_user` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT '最后操作人',
	`gmt_create` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
	`gmt_modified` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
	PRIMARY KEY (`id`),
    UNIQUE INDEX uk_app_config (`app_code`, `app_config_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT 'JSwitch应用配置表';

CREATE TABLE `jswitch_app_node`(
	`id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '主键id',
	`app_code` varchar(50) NOT NULL DEFAULT '' COMMENT '应用编码',
	`app_node_ip` varchar(50) NOT NULL DEFAULT ''  COMMENT '节点IP',
	`app_node_token` varchar(50) NOT NULL DEFAULT ''  COMMENT '节点标示',
	`gmt_create` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
	`last_heartbeat_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '节点上次心跳时间',
	PRIMARY KEY (`id`),
  UNIQUE INDEX uk_app_node (`app_code`, `app_node_token`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT 'JSwitch应用节点表';

CREATE TABLE `jswitch_event`(
    `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '主键id',
    `event` varchar(100) NOT NULL DEFAULT ''  COMMENT '事件',
    `event_sn` varchar(40) NOT NULL DEFAULT ''  COMMENT '事件序列号',
    `event_data` varchar(2000) NOT NULL DEFAULT ''  COMMENT '事件数据',
    `event_message` varchar(200) DEFAULT ''  COMMENT '事件消息',
    `event_operator` varchar(50) NOT NULL DEFAULT ''  COMMENT '事件操作人',
    `event_operator_name` varchar(20) NOT NULL DEFAULT ''  COMMENT '事件操作人',
    `event_level` varchar(10) NOT NULL DEFAULT ''  COMMENT '事件等级 info信息/success成功error异常的等',
    `event_date` varchar(10) DEFAULT '0000-00-00' COMMENT '事件发生日期, 用于索引',
    `gmt_create` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    INDEX idx_date_event (`event_date`, `event`, `event_sn`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT 'JSwitch事件表';

CREATE TABLE `jswitch_user`(
    `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '主键id',
    `user_id` varchar(50) NOT NULL DEFAULT ''  COMMENT '用户id',
    `user_name` varchar(50) NOT NULL DEFAULT ''  COMMENT '用户名字',
    `user_password` varchar(50) NOT NULL COMMENT '用户密码（MD5）',
    `user_level` varchar(40) NOT NULL DEFAULT ''  COMMENT '用户权限等级',
    `user_phone` varchar(40) NOT NULL DEFAULT ''  COMMENT '用户手机号',
    `gmt_create` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `gmt_modified` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`),
    INDEX idx_user (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT 'JSwitch用户表';

CREATE TABLE `jswitch_user_favorite`(
    `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '主键id',
    `user_id` varchar(50) NOT NULL COMMENT '登陆名',
    `favorite_type` varchar(50) NOT NULL COMMENT '喜好类型',
    `favorite_object` varchar(50) NOT NULL COMMENT '喜好对象',
    `gmt_create` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    UNIQUE INDEX uk_user_favorite (`user_id`, `favorite_type`, `favorite_object`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT 'JSwitch用户喜好表';