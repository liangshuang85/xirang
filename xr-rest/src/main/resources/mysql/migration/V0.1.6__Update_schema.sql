ALTER TABLE `b_approval`
    DROP COLUMN `assignee_id`,
    DROP COLUMN `approval_document`,
    ADD COLUMN `approval_code`        varchar(100) NOT NULL COMMENT '审批定义Code' AFTER `ref_id`,
    ADD COLUMN `type`                 varchar(100) NOT NULL COMMENT '审批类型' AFTER `approval_code`,
    ADD COLUMN `approval_instance_id` varchar(100) NULL COMMENT '审批实例Code' AFTER `type`,
    ADD COLUMN `ref_type`             varchar(40)  NOT NULL COMMENT '审批关联类型' AFTER `approval_instance_id`,
    MODIFY COLUMN `approval_status` varchar(40) NOT NULL COMMENT '审批状态' AFTER `ref_id`,
    DROP COLUMN `creation_date`;

ALTER TABLE b_approval_template
    ADD COLUMN `approval_code` varchar(40) NOT NULL COMMENT '审批定义Code' AFTER `id`,
    DROP COLUMN `assignee_ids`;