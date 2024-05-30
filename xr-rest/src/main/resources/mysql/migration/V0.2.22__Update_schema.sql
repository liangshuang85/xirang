ALTER TABLE `b_approval`
    ADD COLUMN `end_time`       timestamp NULL COMMENT '审批结束时间' AFTER `department_name`,
    ADD COLUMN `start_time`     timestamp NULL COMMENT '审批发起时间' AFTER `department_name`,
    ADD COLUMN `instance_tasks` text      NULL COMMENT '审批任务ID' AFTER `department_name`,
    ADD COLUMN `members`        text      NULL COMMENT '审批成员的ID' AFTER `department_name`;

ALTER TABLE `b_task`
    ADD COLUMN `end_time`   timestamp     NULL COMMENT '任务结束时间' AFTER `status`,
    ADD COLUMN `start_time` timestamp     NULL COMMENT '任务发起时间' AFTER `status`,
    ADD COLUMN `summary`    varchar(200)  NULL COMMENT '任务名称' AFTER `status`,
    ADD COLUMN `url`        varchar(4096) NULL COMMENT '任务链接' AFTER `status`,
    ADD COLUMN `members`    text          NULL COMMENT '任务成员的ID' AFTER `status`;