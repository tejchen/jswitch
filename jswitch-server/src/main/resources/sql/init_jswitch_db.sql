CREATE TABLE `jswitch_app` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `app_code` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '应用编码',
  `app_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '应用名称',
  `app_desc` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT '' COMMENT '应用描述',
  `app_owner` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT '应用owner',
  `create_user` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT '创建人',
  `last_operate_user` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT '最后操作人',
  `gmt_create` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='应用列表';

CREATE TABLE `jswitch_app_config`(
	`id` int unsigned NOT NULL AUTO_INCREMENT COMMENT '主键id',
	`app_code` varchar(50) NOT NULL DEFAULT '' COMMENT '应用编码',
	`app_config_code` varchar(50) NOT NULL DEFAULT ''  COMMENT '配置编码',
	`app_config_name` varchar(50) NOT NULL DEFAULT ''  COMMENT '配置名称',
	`app_config_content` varchar(50) NOT NULL DEFAULT ''  COMMENT '配置内容',
	`app_config_desc` varchar(1000) null default ''  COMMENT '配置描述',
	`app_config_origin` varchar(50) NOT NULL DEFAULT ''  COMMENT '配置来源',
	`create_user` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT '创建人',
    `last_operate_user` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT '最后操作人',
	`gmt_create` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
	`gmt_modified` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
	PRIMARY KEY (`id`),
    UNIQUE INDEX uk_app_config (`app_code`, `app_config_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT 'JSwitch应用配置表';

CREATE TABLE `jswitch_app_node`(
	`id` int unsigned NOT NULL AUTO_INCREMENT COMMENT '主键id',
	`app_code` varchar(50) NOT NULL DEFAULT '' COMMENT '应用编码',
	`node_ip` varchar(50) NOT NULL DEFAULT ''  COMMENT '节点IP',
	`gmt_create` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
	`node_last_heartbeat_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP ''  COMMENT '节点上次心跳时间',
	PRIMARY KEY (`id`),
    UNIQUE INDEX uk_app_node (`app_code`, `node_ip`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT 'JSwitch应用节点表';