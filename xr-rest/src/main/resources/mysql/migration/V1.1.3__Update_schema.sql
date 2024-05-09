CREATE TABLE `b_change`
(
    `id`           bigint       NOT NULL COMMENT 'ID',
    `ref_id`       bigint       NOT NULL COMMENT '关联ID',
    `ref_type`     varchar(100) NOT NULL COMMENT '关联类型',
    `before`       varchar(100) NOT NULL COMMENT '变更前状态',
    `after`        varchar(100) NOT NULL COMMENT '变更后状态',
    `elapsed_days` int unsigned NOT NULL COMMENT '变更耗时',
    `operator_id`  varchar(100) NOT NULL COMMENT '操作人的飞书OpenID',
    `created_at`   timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `created_by`   bigint                DEFAULT NULL COMMENT '创建者',
    PRIMARY KEY (`id`) USING BTREE
) COMMENT ='变更记录';
