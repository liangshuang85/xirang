CREATE TABLE `b_approval_template`
(
    `id`           bigint      NOT NULL COMMENT 'ID',
    `department`   varchar(40) NOT NULL COMMENT '审批模板部门',
    `type`         varchar(40) NOT NULL COMMENT '审批模板类型',
    `ref_type`     varchar(40) NOT NULL COMMENT '审批模板关联类型',
    `assignee_ids` text        NOT NULL COMMENT '审批模板部门负责人ID列表',
    `created_at`   timestamp   NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `created_by`   bigint unsigned      DEFAULT '0' COMMENT '创建者',
    `updated_at`   timestamp   NULL     DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `updated_by`   bigint unsigned      DEFAULT '0' COMMENT '更新者',
    `deleted`      tinyint(1)  NOT NULL DEFAULT '0' COMMENT '删除标记',
    PRIMARY KEY (`id`) USING BTREE
) COMMENT ='审批模板';
