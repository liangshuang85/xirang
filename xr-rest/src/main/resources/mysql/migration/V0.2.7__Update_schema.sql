ALTER TABLE `b_task_template`
    DROP COLUMN `department_id`,
    ADD COLUMN `instance_role_id` bigint NOT NULL COMMENT '实例角色ID' AFTER `id`;

ALTER TABLE `b_task`
    DROP COLUMN `department`,
    DROP COLUMN `department_id`,
    ADD COLUMN `instance_role_id` bigint NOT NULL COMMENT '实例角色ID' AFTER `id`;
