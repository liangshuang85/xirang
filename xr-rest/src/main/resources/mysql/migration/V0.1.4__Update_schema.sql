CREATE TABLE `b_task_template`
(
    `id`          bigint          NOT NULL COMMENT '任务模板ID',
    `department`  varchar(40)     NOT NULL COMMENT '任务模板部门',
    `type`        varchar(40)     NOT NULL COMMENT '任务模板类型',
    `ref_type`    varchar(40)     NOT NULL COMMENT '任务模板关联类型',
    `assignee_id` varchar(255)    NOT NULL COMMENT '任务模板部门负责人ID',
    `created_at`  timestamp       NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `created_by`  bigint UNSIGNED NULL     DEFAULT 0 COMMENT '创建者',
    `updated_at`  timestamp       NULL     DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `updated_by`  bigint UNSIGNED NULL     DEFAULT 0 COMMENT '更新者',
    `deleted`     tinyint(1)      NOT NULL DEFAULT 0 COMMENT '删除标记',
    PRIMARY KEY (`id`) USING BTREE
) COMMENT ='任务模板';

ALTER TABLE `b_task`
    ADD COLUMN `task_template_id` bigint NOT NULL COMMENT '任务模板ID' AFTER `ref_id`;

ALTER TABLE `b_task`
    ADD COLUMN `status` varchar(40) NOT NULL COMMENT '任务状态' AFTER `task_template_id`;

ALTER TABLE b_task
    MODIFY `task_guid` varchar(60) NULL COMMENT '飞书任务的GUID';
