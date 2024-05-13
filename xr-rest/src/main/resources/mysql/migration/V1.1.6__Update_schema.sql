ALTER TABLE `b_framework_agreement`
    ADD COLUMN `framework_agreement_signed` tinyint(1) NOT NULL COMMENT '是否已签署框架协议书' AFTER `assignee_id`,
    ADD COLUMN `project_proposal_approved`  tinyint(1) NOT NULL COMMENT '是否已批复项目建议书' AFTER `assignee_id`;

CREATE TABLE `b_basic_data`
(
    `id`                 BIGINT     NOT NULL COMMENT 'ID',
    `ref_id`             BIGINT     NOT NULL COMMENT '关联ID',
    `longitude`          varchar(40)         DEFAULT NULL COMMENT '经度',
    `latitude`           varchar(40)         DEFAULT NULL COMMENT '纬度',
    `installed_capacity` DECIMAL(10, 2)      DEFAULT NULL COMMENT '光伏装机量（MW)',
    `oxygen_price`       DECIMAL(10, 2)      DEFAULT NULL COMMENT '液氧卖出（￥/t)',
    `heating_price`      DECIMAL(10, 2)      DEFAULT NULL COMMENT '供暖价格（￥/GJ)',
    `total_heating`      DECIMAL(10, 2)      DEFAULT NULL COMMENT '供暖总量（GJ)',
    `carbon_trade_price` DECIMAL(10, 2)      DEFAULT NULL COMMENT '碳交易价（￥/tCO₂)',
    `water_price`        DECIMAL(10, 2)      DEFAULT NULL COMMENT '用水（￥/t)',
    `loan_rate`          DECIMAL(10, 2)      DEFAULT NULL COMMENT '贷款利率（%)',
    `loan_years`         DECIMAL(10, 2)      DEFAULT NULL COMMENT '贷款年限（年)',
    `created_at`         timestamp  NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `created_by`         bigint              DEFAULT NULL COMMENT '创建者',
    `updated_at`         timestamp  NULL     DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `updated_by`         bigint              DEFAULT NULL COMMENT '更新者',
    `deleted`            tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除标记',
    PRIMARY KEY (`id`)
) COMMENT ='基础数据表';
