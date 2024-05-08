CREATE TABLE `s_user`
(
    `id`            bigint       NOT NULL,
    `username`      varchar(200) NOT NULL COMMENT '用户名',
    `password_hash` varchar(200)          DEFAULT NULL COMMENT '密码哈希',
    `nickname`      varchar(200)          DEFAULT NULL COMMENT '昵称',
    `full_name`     varchar(200)          DEFAULT NULL COMMENT '全名',
    `phone_number`  varchar(32)           DEFAULT NULL COMMENT '电话号码',
    `email`         varchar(200)          DEFAULT NULL COMMENT 'Email',
    `admin`         tinyint(1)   NOT NULL DEFAULT '0' COMMENT '是否为管理员',
    `blocked`       tinyint(1)   NOT NULL DEFAULT '0' COMMENT '封禁状态',
    `lark_open_id`  varchar(100)          DEFAULT NULL COMMENT '飞书用户的OpenID',
    `created_at`    timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `created_by`    bigint                DEFAULT NULL COMMENT '创建者',
    `updated_at`    timestamp    NULL     DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `updated_by`    bigint                DEFAULT NULL COMMENT '更新者',
    `deleted`       tinyint(1)   NOT NULL DEFAULT '0' COMMENT '删除标记',
    PRIMARY KEY (`id`) USING BTREE
) COMMENT ='用户';