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

CREATE TABLE `jswitch_app_node_event`(
    `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '主键id',
    `app_node_token` varchar(50) NOT NULL DEFAULT ''  COMMENT '节点标示',
    `app_node_event_code` varchar(50) NOT NULL DEFAULT ''  COMMENT '节点事件',
    `app_node_event_date` varchar(10) DEFAULT '0000-00-00' COMMENT '节点事件发生日期',
    `app_node_event_sn` varchar(40) NOT NULL DEFAULT ''  COMMENT '节点事件序列号',
    `app_node_event_data` varchar(200) NOT NULL DEFAULT ''  COMMENT '节点数据',
    `gmt_create` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    INDEX idx_app_node (`app_node_token`, `app_node_event_date`, `app_node_event_sn`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT 'JSwitch应用节点事件表';