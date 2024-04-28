ALTER TABLE `b_clue`
    MODIFY COLUMN `status` varchar(40) NOT NULL COMMENT '线索状态' AFTER `adcode`;

ALTER TABLE `b_project_information`
    MODIFY COLUMN `project_id` bigint NOT NULL COMMENT '项目ID' AFTER `id`;
