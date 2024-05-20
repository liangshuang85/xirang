ALTER TABLE `s_role_lark_member`
    MODIFY COLUMN `member_type` varchar(100) NOT NULL COMMENT '成员类型' AFTER `member_id`;

ALTER TABLE `s_permission_resource`
    ADD COLUMN `built_in` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否内置' AFTER `description`;

ALTER TABLE `s_permission`
    ADD COLUMN `built_in` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否内置' AFTER `level`;

ALTER TABLE `s_role`
    ADD COLUMN `built_in` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否内置' AFTER `enabled`;

ALTER TABLE `s_instance_role`
    ADD COLUMN `built_in` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否内置' AFTER `enabled`;
