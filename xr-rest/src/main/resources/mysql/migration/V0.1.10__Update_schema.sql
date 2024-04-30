ALTER TABLE `b_task_template`
    CHANGE COLUMN `department` `department_id` varchar(64) NOT NULL COMMENT '任务模板部门ID' AFTER `id`,
    DROP COLUMN `assignee_id`;