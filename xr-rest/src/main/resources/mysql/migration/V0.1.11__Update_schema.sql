ALTER TABLE `b_task`
    CHANGE COLUMN `assignee_id` `department_id` varchar(255) NOT NULL COMMENT '部门ID' AFTER `department`;