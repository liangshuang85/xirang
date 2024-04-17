-- MySQL dump 10.13  Distrib 8.0.31, for Linux (x86_64)
--
-- Host: localhost    Database: ywhc-xr
-- ------------------------------------------------------
-- Server version	8.0.31

/*!40101 SET @OLD_CHARACTER_SET_CLIENT = @@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS = @@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION = @@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE = @@TIME_ZONE */;
/*!40103 SET TIME_ZONE = '+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS = @@UNIQUE_CHECKS, UNIQUE_CHECKS = 0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS = @@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS = 0 */;
/*!40101 SET @OLD_SQL_MODE = @@SQL_MODE, SQL_MODE = 'NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES = @@SQL_NOTES, SQL_NOTES = 0 */;

--
-- Table structure for table `b_approval`
--

DROP TABLE IF EXISTS `b_approval`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `b_approval`
(
    `id`                bigint                                                        NOT NULL COMMENT 'ID',
    `clue_id`           bigint                                                        NOT NULL COMMENT '线索ID',
    `department_name`   varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '部门名称',
    `assignee_id`       varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '负责人的飞书OpenID',
    `approval_document` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '审批单编号或引用',
    `approval_status`   bigint                                                        NOT NULL COMMENT '审批状态',
    `creation_date`     timestamp                                                     NOT NULL COMMENT '记录创建的时间戳',
    `created_by`        bigint                                                                 DEFAULT NULL COMMENT '创建者',
    `created_at`        timestamp                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at`        timestamp                                                     NULL     DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `updated_by`        bigint                                                                 DEFAULT NULL COMMENT '更新者',
    `deleted`           tinyint                                                       NOT NULL DEFAULT '0' COMMENT '删除标记',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci
  ROW_FORMAT = DYNAMIC COMMENT ='审批记录';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `b_channel_entry`
--

DROP TABLE IF EXISTS `b_channel_entry`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `b_channel_entry`
(
    `id`                   bigint                                                       NOT NULL COMMENT 'ID',
    `clue_id`              bigint                                                       NOT NULL COMMENT '线索ID',
    `partner_name`         varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '合伙人姓名',
    `background`           text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci        NOT NULL COMMENT '工作背景',
    `social_relations`     text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci        NOT NULL COMMENT '社会关系',
    `contact_name`         varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '联系人姓名',
    `counterpart_name`     varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '对接人姓名',
    `contact_position`     varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '联系人职务',
    `counterpart_position` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '对接人职务',
    `status`               varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '线索状态',
    `created_by`           bigint                                                                DEFAULT NULL COMMENT '创建者',
    `created_at`           timestamp                                                    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at`           timestamp                                                    NULL     DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `updated_by`           bigint                                                                DEFAULT NULL COMMENT '更新者',
    `deleted`              tinyint                                                      NOT NULL DEFAULT '0' COMMENT '删除标记',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci
  ROW_FORMAT = DYNAMIC COMMENT ='渠道录入信息';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `b_clue`
--

DROP TABLE IF EXISTS `b_clue`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `b_clue`
(
    `id`          bigint                                                        NOT NULL COMMENT 'ID',
    `adcode`      bigint                                                        NOT NULL COMMENT '行政区划代码',
    `status`      varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NOT NULL COMMENT '线索状态',
    `level`       varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci   NOT NULL COMMENT '线索级别',
    `clue_code`   varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NOT NULL COMMENT '线索编号',
    `assignee_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '负责人',
    `created_by`  bigint                                                                 DEFAULT NULL COMMENT '创建者',
    `created_at`  timestamp                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at`  timestamp                                                     NULL     DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `updated_by`  bigint                                                                 DEFAULT NULL COMMENT '更新者',
    `deleted`     tinyint                                                       NOT NULL DEFAULT '0' COMMENT '删除标记',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci
  ROW_FORMAT = DYNAMIC COMMENT ='基础线索信息';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `b_framework_agreement`
--

DROP TABLE IF EXISTS `b_framework_agreement`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `b_framework_agreement`
(
    `id`          bigint                                                        NOT NULL COMMENT 'ID',
    `name`        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '框架协议项目名称',
    `code`        varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NOT NULL COMMENT '框架协议项目编码',
    `status`      varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NOT NULL COMMENT '框架协议项目状态',
    `adcode`      bigint                                                        NOT NULL COMMENT '所属行政区划代码',
    `clue_id`     bigint                                                        NOT NULL COMMENT '线索ID',
    `assignee_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '负责人的飞书OpenID',
    `created_at`  timestamp                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `created_by`  bigint                                                                 DEFAULT NULL COMMENT '创建者',
    `updated_at`  timestamp                                                     NULL     DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `updated_by`  bigint                                                                 DEFAULT NULL COMMENT '更新者',
    `deleted`     tinyint(1)                                                    NOT NULL DEFAULT '0' COMMENT '删除标记',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci
  ROW_FORMAT = DYNAMIC COMMENT ='框架协议项目';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `b_framework_agreement_channel_entry`
--

DROP TABLE IF EXISTS `b_framework_agreement_channel_entry`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `b_framework_agreement_channel_entry`
(
    `id`                     bigint                                                       NOT NULL COMMENT 'ID',
    `partner_name`           varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '渠道合伙人姓名',
    `background`             text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci        NOT NULL COMMENT '重要工作背景',
    `social_relations`       text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci        NOT NULL COMMENT '重要社会关系',
    `contact_name`           varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '关键联系人姓名',
    `counterpart_name`       varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '政府对接人姓名',
    `contact_position`       varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '关键联系人职务',
    `counterpart_position`   varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '政府对接人职务',
    `framework_agreement_id` double                                                       NOT NULL COMMENT '框架协议项目ID',
    `created_at`             timestamp                                                    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `created_by`             bigint                                                                DEFAULT NULL COMMENT '创建者',
    `updated_at`             timestamp                                                    NULL     DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `updated_by`             bigint                                                                DEFAULT NULL COMMENT '更新者',
    `deleted`                tinyint(1)                                                   NOT NULL DEFAULT '0' COMMENT '删除标记',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci
  ROW_FORMAT = DYNAMIC COMMENT ='框架协议项目渠道录入信息';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `b_framework_agreement_project`
--

DROP TABLE IF EXISTS `b_framework_agreement_project`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `b_framework_agreement_project`
(
    `id`                     bigint                                                NOT NULL COMMENT 'ID',
    `construction_scale`     double                                                NOT NULL COMMENT '建设规模',
    `wind_resource`          text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '风能资源',
    `solar_resource`         text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '光伏资源',
    `land_resource`          text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '土地资源',
    `framework_agreement_id` double                                                NOT NULL COMMENT '框架协议项目ID',
    `created_at`             timestamp                                             NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `created_by`             bigint                                                         DEFAULT NULL COMMENT '创建者',
    `updated_at`             timestamp                                             NULL     DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `updated_by`             bigint                                                         DEFAULT NULL COMMENT '更新者',
    `deleted`                tinyint(1)                                            NOT NULL DEFAULT '0' COMMENT '删除标记',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci
  ROW_FORMAT = DYNAMIC COMMENT ='框架协议项目项目信息';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `b_framework_agreement_project_funding`
--

DROP TABLE IF EXISTS `b_framework_agreement_project_funding`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `b_framework_agreement_project_funding`
(
    `id`                           bigint                                                        NOT NULL COMMENT 'ID',
    `framework_agreement_id`       double                                                        NOT NULL COMMENT '框架协议项目ID',
    `wind_power_resource`          varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '风力发电资源',
    `photovoltaic_resource`        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '光伏发电资源',
    `grid_energy_price`            double                                                        NOT NULL COMMENT '新能源并网电价',
    `storage_standards`            varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '新能源配套储能标准',
    `energy_discarded`             double                                                        NOT NULL COMMENT '风力及光伏发电弃电量',
    `civil_heating_price`          double                                                        NOT NULL COMMENT '风力及光伏发电弃电量',
    `commercial_heating_price`     double                                                        NOT NULL COMMENT '民用供暖价格',
    `heating_duration`             double                                                        NOT NULL COMMENT '年供暖时长',
    `industry_base`                varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '周边产业基础',
    `wind_operation_hours`         double                                                        NOT NULL COMMENT '风力发电时长',
    `pv_operation_hours`           double                                                        NOT NULL COMMENT '光伏发电时长',
    `industrial_electricity_price` double                                                        NOT NULL COMMENT '工商业用电综合电价',
    `civil_heating_area`           double                                                        NOT NULL COMMENT '民用供暖面积',
    `commercial_heating_area`      double                                                        NOT NULL COMMENT '工商业供暖面积',
    `created_by`                   bigint                                                                 DEFAULT NULL COMMENT '创建者',
    `created_at`                   timestamp                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at`                   timestamp                                                     NULL     DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `updated_by`                   bigint                                                                 DEFAULT NULL COMMENT '更新者',
    `deleted`                      tinyint                                                       NOT NULL DEFAULT '0' COMMENT '删除标记',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci
  ROW_FORMAT = DYNAMIC COMMENT ='框架协议项目项目收资信息';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `b_funding`
--

DROP TABLE IF EXISTS `b_funding`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `b_funding`
(
    `id`                           bigint                                                        NOT NULL COMMENT 'ID',
    `clue_id`                      bigint                                                        NOT NULL COMMENT '线索ID',
    `wind_power_resource`          varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '风力发电资源',
    `photovoltaic_resource`        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '光伏发电资源',
    `grid_energy_price`            double                                                        NOT NULL COMMENT '新能源并网电价',
    `storage_standards`            varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '新能源配套储能标准',
    `energy_discarded`             double                                                        NOT NULL COMMENT '风力及光伏发电弃电量',
    `civil_heating_price`          double                                                        NOT NULL COMMENT '风力及光伏发电弃电量',
    `commercial_heating_price`     double                                                        NOT NULL COMMENT '民用供暖价格',
    `heating_duration`             double                                                        NOT NULL COMMENT '年供暖时长',
    `industry_base`                varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '周边产业基础',
    `wind_operation_hours`         double                                                        NOT NULL COMMENT '风力发电时长',
    `pv_operation_hours`           double                                                        NOT NULL COMMENT '光伏发电时长',
    `industrial_electricity_price` double                                                        NOT NULL COMMENT '工商业用电综合电价',
    `civil_heating_area`           double                                                        NOT NULL COMMENT '民用供暖面积',
    `commercial_heating_area`      double                                                        NOT NULL COMMENT '工商业供暖面积',
    `created_by`                   bigint                                                                 DEFAULT NULL COMMENT '创建者',
    `created_at`                   timestamp                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at`                   timestamp                                                     NULL     DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `updated_by`                   bigint                                                                 DEFAULT NULL COMMENT '更新者',
    `deleted`                      tinyint                                                       NOT NULL DEFAULT '0' COMMENT '删除标记',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci
  ROW_FORMAT = DYNAMIC COMMENT ='项目收资信息';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `b_project`
--

DROP TABLE IF EXISTS `b_project`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `b_project`
(
    `id`                     bigint                                                        NOT NULL COMMENT 'ID',
    `name`                   varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NOT NULL COMMENT '项目名',
    `code`                   varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '项目编码',
    `status`                 varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NOT NULL COMMENT '项目状态',
    `adcode`                 bigint                                                        NOT NULL COMMENT '行政区划代码',
    `framework_agreement_id` bigint                                                        NOT NULL COMMENT '框架协议ID',
    `assignee_id`            varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '负责人的飞书OpenID',
    `created_at`             timestamp                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `created_by`             bigint                                                                 DEFAULT NULL COMMENT '创建者',
    `updated_at`             timestamp                                                     NULL     DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `updated_by`             bigint                                                                 DEFAULT NULL COMMENT '更新者',
    `deleted`                tinyint(1)                                                    NOT NULL DEFAULT '0' COMMENT '删除标记',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci
  ROW_FORMAT = DYNAMIC COMMENT ='项目';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `b_project_information`
--

DROP TABLE IF EXISTS `b_project_information`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `b_project_information`
(
    `id`                             bigint                                                        NOT NULL COMMENT 'ID',
    `construction_scale`             double                                                        NOT NULL COMMENT '建设规模',
    `wind_resource`                  text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci         NOT NULL COMMENT '风能资源',
    `solar_resource`                 text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci         NOT NULL COMMENT '光伏资源',
    `land_resource`                  text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci         NOT NULL COMMENT '土地资源',
    `project_id`                     double                                                        NOT NULL COMMENT '项目ID',
    `proposed_location`              varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '拟建地点',
    `construction_scale_and_content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '建设规模及内容',
    `total_land`                     double                                                        NOT NULL COMMENT '总用地',
    `total_investment`               double                                                        NOT NULL COMMENT '项目总投资',
    `funding_source`                 varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '资金来源',
    `project_start_time`             datetime                                                      NOT NULL COMMENT '项目建设周期开始时间',
    `project_end_time`               datetime                                                      NOT NULL COMMENT '项目建设周期结束时间',
    `created_at`                     timestamp                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `created_by`                     bigint                                                                 DEFAULT NULL COMMENT '创建者',
    `updated_at`                     timestamp                                                     NULL     DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `updated_by`                     bigint                                                                 DEFAULT NULL COMMENT '更新者',
    `deleted`                        tinyint(1)                                                    NOT NULL DEFAULT '0' COMMENT '删除标记',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci
  ROW_FORMAT = DYNAMIC COMMENT ='项目信息';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `b_task`
--

DROP TABLE IF EXISTS `b_task`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `b_task`
(
    `id`           bigint                                                        NOT NULL COMMENT 'ID',
    `department`   varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NOT NULL COMMENT '部门名称',
    `assignee_id`  varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '负责人的飞书OpenID',
    `type`         varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NOT NULL COMMENT '任务类型',
    `completed_at` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NOT NULL COMMENT '任务的完成时刻时间戳',
    `owner_id`     bigint                                                        NOT NULL COMMENT '关联对象ID',
    `created_at`   timestamp                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `created_by`   bigint                                                                 DEFAULT NULL COMMENT '创建者',
    `updated_at`   timestamp                                                     NULL     DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `updated_by`   bigint                                                                 DEFAULT NULL COMMENT '更新者',
    `deleted`      tinyint(1)                                                    NOT NULL DEFAULT '0' COMMENT '删除标记',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci
  ROW_FORMAT = DYNAMIC COMMENT ='任务';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `b_visit`
--

DROP TABLE IF EXISTS `b_visit`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `b_visit`
(
    `id`         bigint                                                       NOT NULL COMMENT 'ID',
    `clue_id`    bigint                                                       NOT NULL COMMENT '线索ID',
    `official`   tinyint(1)                                                   NOT NULL COMMENT '是否为正式拜访',
    `visit_date` date                                                         NOT NULL COMMENT '拜访日期',
    `type`       varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '访问类型',
    `created_by` bigint                                                                DEFAULT NULL COMMENT '创建者',
    `created_at` timestamp                                                    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` timestamp                                                    NULL     DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `updated_by` bigint                                                                DEFAULT NULL COMMENT '更新者',
    `deleted`    tinyint                                                      NOT NULL DEFAULT '0' COMMENT '删除标记',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci
  ROW_FORMAT = DYNAMIC COMMENT ='拜访记录';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `d_administrative_division`
--

DROP TABLE IF EXISTS `d_administrative_division`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `d_administrative_division`
(
    `id`         bigint       NOT NULL,
    `name`       varchar(128) NOT NULL COMMENT '行政区名称',
    `adcode`     bigint       NOT NULL COMMENT '6位数字格式的行政区划代码',
    `parent`     bigint       NOT NULL COMMENT '父级行政区划代码',
    `level`      tinyint      NOT NULL COMMENT '行政区划级别：0为国家、1为省级、2为地市、3为区县',
    `created_at` timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `created_by` bigint                DEFAULT '0' COMMENT '创建者',
    `updated_at` timestamp    NULL     DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `updated_by` bigint                DEFAULT '0' COMMENT '更新者',
    `deleted`    tinyint(1)   NOT NULL DEFAULT '0' COMMENT '删除标记',
    PRIMARY KEY (`id`) USING BTREE,
    KEY `idx_region_deleted_parent` (`deleted`, `parent`) USING BTREE,
    KEY `idx_region_adcode` (`adcode`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='行政区划';
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE = @OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE = @OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS = @OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS = @OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT = @OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS = @OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION = @OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES = @OLD_SQL_NOTES */;

-- Dump completed on 2024-04-17 13:47:11
