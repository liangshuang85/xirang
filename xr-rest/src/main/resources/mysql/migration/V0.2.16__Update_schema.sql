ALTER TABLE `s_instance_role`
    ADD COLUMN `ref_type` varchar(100) NOT NULL COMMENT '关联对象类型' AFTER `description`,
    ADD COLUMN `assignee` tinyint(1)   NOT NULL DEFAULT 0 COMMENT '是否为负责人' AFTER `ref_type`;
