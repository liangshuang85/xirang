ALTER TABLE `s_lark_department`
    MODIFY COLUMN `leader_user_id` varchar(100) NULL COMMENT '部门主管在飞书中的用户ID' AFTER `name`;
