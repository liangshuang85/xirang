ALTER TABLE `s_permission_resource`
    MODIFY COLUMN `id` bigint NOT NULL FIRST;

ALTER TABLE `s_permission`
    MODIFY COLUMN `id` bigint NOT NULL FIRST;

ALTER TABLE `s_permission_assignment`
    MODIFY COLUMN `id` bigint NOT NULL FIRST;

ALTER TABLE `s_lark_department_member`
    MODIFY COLUMN `id` bigint NOT NULL FIRST;

ALTER TABLE `s_role`
    ADD COLUMN `basic` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否为基本角色' AFTER `built_in`;
