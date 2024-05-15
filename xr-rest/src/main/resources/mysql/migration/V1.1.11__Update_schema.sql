CREATE TABLE `s_permission_resource`
(
    `id`          bigint       NOT NULL AUTO_INCREMENT,
    `name`        varchar(200) NOT NULL COMMENT '名称',
    `code`        varchar(200) NOT NULL COMMENT '编码',
    `description` varchar(200)          DEFAULT NULL COMMENT '描述',
    `created_by`  bigint                DEFAULT NULL COMMENT '创建者',
    `created_at`  timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at`  timestamp    NULL     DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `updated_by`  bigint                DEFAULT NULL COMMENT '更新者',
    `deleted`     tinyint(1)   NOT NULL DEFAULT '0' COMMENT '删除标记',
    PRIMARY KEY (`id`),
    KEY `uk-s_resource-code` (`code`)
) COMMENT ='权限资源';

CREATE TABLE `s_permission`
(
    `id`            bigint       NOT NULL AUTO_INCREMENT,
    `name`          varchar(200) NOT NULL COMMENT '名称',
    `resource_code` varchar(200) NOT NULL COMMENT '资源编码',
    `code`          varchar(200) NOT NULL COMMENT '操作编码',
    `description`   varchar(200) NOT NULL DEFAULT '' COMMENT '描述',
    `level`         varchar(100) NOT NULL DEFAULT '' COMMENT '级别',
    `created_by`    bigint                DEFAULT NULL COMMENT '创建者',
    `created_at`    timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at`    timestamp    NULL     DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `updated_by`    bigint                DEFAULT NULL COMMENT '更新者',
    `deleted`       tinyint(1)   NOT NULL DEFAULT '0' COMMENT '删除标记',
    PRIMARY KEY (`id`)
) COMMENT ='权限';

CREATE TABLE `s_permission_assignment`
(
    `id`              bigint       NOT NULL AUTO_INCREMENT,
    `permission_code` varchar(200) NOT NULL COMMENT '权限编码',
    `role_id`         bigint       NOT NULL COMMENT '角色ID',
    `created_by`      bigint                DEFAULT NULL COMMENT '创建者',
    `created_at`      timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at`      timestamp    NULL     DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `updated_by`      bigint                DEFAULT NULL COMMENT '更新者',
    `deleted`         tinyint(1)   NOT NULL DEFAULT '0' COMMENT '删除标记',
    PRIMARY KEY (`id`)
) COMMENT ='权限分配';
