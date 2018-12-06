CREATE DATABASE IF NOT EXISTS `peppa`;

USE `peppa`;

CREATE TABLE `config` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `config_type` tinyint(4) NOT NULL DEFAULT '0' COMMENT '配置文件/配置项',
  `key` varchar(255) NOT NULL DEFAULT '' COMMENT '配置文件名/配置项Key名',
  `value` text NOT NULL COMMENT '0 配置文件：文件的内容，1 配置项：配置值',
  `app_id` bigint(20) NOT NULL COMMENT 'appid',
  `version` varchar(255) NOT NULL DEFAULT 'DEFAULT_VERSION' COMMENT '版本',
  `env_id` bigint(20) NOT NULL COMMENT 'envid',
  `operator` varchar(32) DEFAULT NULL COMMENT '操作人',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `modify_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='系统配置表';