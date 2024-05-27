CREATE TABLE `s_application`
(
    `id`              bigint       NOT NULL COMMENT 'ID',
    `application_sid` varchar(200) NOT NULL COMMENT '应用SID',
    `name`            varchar(100) NOT NULL COMMENT '名称',
    `description`     varchar(200)          DEFAULT NULL COMMENT '描述',
    `public_key`      text COMMENT '公钥',
    `algorithm`       varchar(20) COMMENT '密钥对生成算法',
    `enabled`         tinyint(1)   NOT NULL DEFAULT '1' COMMENT '启用状态',
    `created_by`      bigint                DEFAULT NULL COMMENT '创建者',
    `created_at`      timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at`      timestamp    NULL     DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `updated_by`      bigint                DEFAULT NULL COMMENT '更新者',
    `deleted`         tinyint      NOT NULL DEFAULT '0' COMMENT '删除标记',
    PRIMARY KEY (`id`) USING BTREE
) COMMENT ='应用';