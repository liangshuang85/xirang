ALTER TABLE `b_task`
    ADD COLUMN `tasklist_guid` varchar(40) NOT NULL COMMENT '任务清单GUID' AFTER `task_guid`,
    ADD COLUMN `section_guid` varchar(40) NOT NULL COMMENT '任务清单分组GUID' AFTER `tasklist_guid`;
