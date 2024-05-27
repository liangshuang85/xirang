ALTER TABLE `s_permission_assignment`
    CHANGE COLUMN `role_id` `subject_id` bigint NOT NULL COMMENT '主体ID' AFTER `permission_code`;