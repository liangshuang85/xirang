CREATE TABLE `b_electricity_load`
(
    `id`                                   BIGINT PRIMARY KEY NOT NULL COMMENT 'ID',
    `ref_id`                               BIGINT             NOT NULL COMMENT '关联id',
    `name`                                 VARCHAR(255)       NULL COMMENT '用电企业名称',
    `annual_electricity_usage`             DECIMAL(19, 6)     NULL COMMENT '年用电量(kWh)',
    `electricity_load`                     DECIMAL(19, 6)     NULL COMMENT '电力负荷(kwh)',
    `main_production_time`                 text               NULL COMMENT '企业主要生产用电时间段',
    `main_production_electricity_usage`    DECIMAL(19, 6)     NULL COMMENT '主要生产时问段用电量(kwh)',
    `park_comprehensive_electricity_price` DECIMAL(19, 6)     NULL COMMENT '园区综合电价(￥/kh)',
    `created_at`                           timestamp          NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `created_by`                           bigint                      DEFAULT NULL COMMENT '创建者',
    `updated_at`                           timestamp          NULL     DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `updated_by`                           bigint                      DEFAULT NULL COMMENT '更新者',
    `deleted`                              tinyint(1)         NOT NULL DEFAULT '0' COMMENT '删除标记'
) COMMENT '用电企业负荷相关信息';

CREATE TABLE `b_oxygen_hydrogen_usage`
(
    `id`                          BIGINT PRIMARY KEY COMMENT 'ID',
    `ref_id`                      BIGINT         NOT NULL COMMENT '关联id',
    `name`                        VARCHAR(255)   NULL COMMENT '用户企业名称',
    `annual_liquid_oxygen_usage`  DECIMAL(19, 6) NULL COMMENT '年均液氧使用量(t/年)',
    `annual_green_hydrogen_usage` DECIMAL(19, 6) NULL COMMENT '年均绿氢使用量(t/年)',
    `created_at`                  timestamp      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `created_by`                  bigint                  DEFAULT NULL COMMENT '创建者',
    `updated_at`                  timestamp      NULL     DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `updated_by`                  bigint                  DEFAULT NULL COMMENT '更新者',
    `deleted`                     tinyint(1)     NOT NULL DEFAULT '0' COMMENT '删除标记'
) COMMENT '用户企业液氧/绿氢使用量';