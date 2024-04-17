ALTER TABLE `b_approval`
    CHANGE COLUMN `clue_id` `ref_id` bigint NOT NULL COMMENT '关联对象ID' AFTER `id`;

ALTER TABLE `b_task`
    CHANGE COLUMN `owner_id` `ref_id` bigint NOT NULL COMMENT '关联对象ID' AFTER `completed_at`,
    ADD COLUMN `task_guid` varchar(60) NOT NULL COMMENT '飞书任务的GUID' AFTER `completed_at`;
