SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for s_instance_role
-- ----------------------------
CREATE TABLE `s_instance_role`
(
    `id`          bigint       NOT NULL,
    `name`        varchar(64)  NOT NULL COMMENT '角色名',
    `description` varchar(128) NULL     DEFAULT NULL COMMENT '角色描述',
    `enabled`     tinyint(1)   NOT NULL DEFAULT 1 COMMENT '启用状态',
    `created_by`  bigint       NULL     DEFAULT NULL COMMENT '创建者',
    `created_at`  timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at`  timestamp    NULL     DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `updated_by`  bigint       NULL     DEFAULT NULL COMMENT '更新者',
    `deleted`     tinyint      NOT NULL DEFAULT 0 COMMENT '删除标记',
    PRIMARY KEY (`id`) USING BTREE
) COMMENT = '实例角色';

-- ----------------------------
-- Table structure for s_instance_role_lark_member
-- ----------------------------
CREATE TABLE `s_instance_role_lark_member`
(
    `id`               bigint       NOT NULL,
    `instance_role_id` bigint       NOT NULL COMMENT '实例角色ID',
    `ref_id`           bigint       NOT NULL COMMENT '关联对象ID',
    `ref_type`         varchar(100) NOT NULL COMMENT '关联对象类型',
    `member_id`        varchar(100) NOT NULL COMMENT '成员在飞书中的ID',
    `member_type`      varchar(100) NOT NULL COMMENT '成员类型',
    `created_by`       bigint       NULL     DEFAULT NULL COMMENT '创建者',
    `created_at`       timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at`       timestamp    NULL     DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `updated_by`       bigint       NULL     DEFAULT NULL COMMENT '更新者',
    `deleted`          tinyint      NOT NULL DEFAULT 0 COMMENT '删除标记',
    PRIMARY KEY (`id`) USING BTREE
) COMMENT = '角色成员';

-- ----------------------------
-- Table structure for s_lark_department
-- ----------------------------
CREATE TABLE `s_lark_department`
(
    `department_id`        varchar(100) NOT NULL COMMENT '部门在飞书中的ID',
    `parent_department_id` varchar(100) NOT NULL COMMENT '父部门在飞书中的ID',
    `name`                 varchar(100) NOT NULL COMMENT '部门在飞书中的名称',
    `leader_user_id`       varchar(100) NOT NULL COMMENT '部门主管在飞书中的用户ID',
    PRIMARY KEY (`department_id`) USING BTREE
) COMMENT = '部门';

-- ----------------------------
-- Table structure for s_lark_department_member
-- ----------------------------
CREATE TABLE `s_lark_department_member`
(
    `id`               bigint       NOT NULL AUTO_INCREMENT,
    `department_id`    varchar(100) NOT NULL COMMENT '部门在飞书中的ID',
    `employee_open_id` varchar(100) NOT NULL COMMENT '员工在飞书中的OpenId',
    PRIMARY KEY (`id`) USING BTREE
) COMMENT = '部门人员';

-- ----------------------------
-- Table structure for s_lark_employee
-- ----------------------------
CREATE TABLE `s_lark_employee`
(
    `open_id` varchar(100) NOT NULL COMMENT '员工在飞书中的用户OpenId',
    `name`    varchar(100) NOT NULL COMMENT '员工在飞书中姓名',
    `avatar`  text         NULL COMMENT '员工在飞书中的用户头像信息',
    PRIMARY KEY (`open_id`) USING BTREE
) COMMENT = '员工';

-- ----------------------------
-- Table structure for s_role
-- ----------------------------
CREATE TABLE `s_role`
(
    `id`          bigint       NOT NULL,
    `name`        varchar(64)  NOT NULL COMMENT '角色名',
    `description` varchar(128) NULL     DEFAULT NULL COMMENT '角色描述',
    `enabled`     tinyint(1)   NOT NULL DEFAULT 1 COMMENT '启用状态',
    `created_by`  bigint       NULL     DEFAULT NULL COMMENT '创建者',
    `created_at`  timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at`  timestamp    NULL     DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `updated_by`  bigint       NULL     DEFAULT NULL COMMENT '更新者',
    `deleted`     tinyint      NOT NULL DEFAULT 0 COMMENT '删除标记',
    PRIMARY KEY (`id`) USING BTREE
) COMMENT = '角色';

-- ----------------------------
-- Table structure for s_role_lark_member
-- ----------------------------
CREATE TABLE `s_role_lark_member`
(
    `id`          bigint       NOT NULL,
    `role_id`     bigint       NOT NULL COMMENT '角色ID',
    `member_id`   varchar(100) NOT NULL COMMENT '成员在飞书中的ID',
    `member_type` varchar(100) NOT NULL DEFAULT 'USER' COMMENT '成员类型',
    `created_by`  bigint       NULL     DEFAULT NULL COMMENT '创建者',
    `created_at`  timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at`  timestamp    NULL     DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `updated_by`  bigint       NULL     DEFAULT NULL COMMENT '更新者',
    `deleted`     tinyint      NOT NULL DEFAULT 0 COMMENT '删除标记',
    PRIMARY KEY (`id`) USING BTREE
) COMMENT = '角色成员';

SET FOREIGN_KEY_CHECKS = 1;
