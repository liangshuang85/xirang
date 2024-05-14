CREATE TABLE `b_regional_measurement_data`
(
    `id`                           BIGINT PRIMARY KEY NOT NULL COMMENT 'id',
    `adcode`                       bigint             NOT NULL COMMENT '6位数字格式的行政区划代码',
    `charging_price`               DECIMAL(19, 6)     NULL COMMENT '充电价格(￥/kwh)',
    `charging_subsidy`             DECIMAL(19, 6)     NULL COMMENT '充电补贴(￥/kWh)',
    `discharging_price`            DECIMAL(19, 6)     NULL COMMENT '放电价格(￥/kwh)',
    `leasing_price`                DECIMAL(19, 6)     NULL COMMENT '租赁价格(￥/kwh·年)',
    `carbon_emission_factor`       DECIMAL(19, 6)     NULL COMMENT '碳排放因子(tCO:/MWh)',
    `vat`                          DECIMAL(19, 6)     NULL COMMENT '增值税%',
    `urban_construction_tax`       DECIMAL(19, 6)     NULL COMMENT '城建税%',
    `education_fee`                DECIMAL(19, 6)     NULL COMMENT '教育费%',
    `income_tax`                   DECIMAL(19, 6)     NULL COMMENT '所得税%',
    `carbon_asset_value_added_tax` DECIMAL(19, 6)     NULL COMMENT '碳资产增值税%',
    `top_price`                    DECIMAL(19, 6)     NULL COMMENT '尖峰电价(￥ /kwh)',
    `peak_price`                   DECIMAL(19, 6)     NULL COMMENT '高峰电价(￥ /kwh)',
    `normal_price`                 DECIMAL(19, 6)     NULL COMMENT '平段电价(￥/kwh)',
    `valley_price`                 DECIMAL(19, 6)     NULL COMMENT '低谷电价(￥/kWh)',
    `deep_price`                   DECIMAL(19, 6)     NULL COMMENT '深谷电价(￥/kwh)',
    `line_loss_price`              DECIMAL(19, 6)     NULL COMMENT '线损电价(￥/kWh)',
    `system_operation_cost`        DECIMAL(19, 6)     NULL COMMENT '系统运行费用(￥ /kWh)',
    `created_at`                   timestamp          NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `created_by`                   bigint                      DEFAULT NULL COMMENT '创建者',
    `updated_at`                   timestamp          NULL     DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `updated_by`                   bigint                      DEFAULT NULL COMMENT '更新者',
    `deleted`                      tinyint(1)         NOT NULL DEFAULT '0' COMMENT '删除标记'
) COMMENT = '区域测算数据表';

CREATE TABLE `b_measurement_data_time`
(
    `id`         BIGINT PRIMARY KEY NOT NULL COMMENT 'ID',
    `ref_id`     BIGINT             NOT NULL COMMENT '关联的区域测算数据表id',
    `month`      INT                NOT NULL COMMENT '月份',
    `top`        text               NOT NULL COMMENT '尖峰时段',
    `peak`       text               NOT NULL COMMENT '高峰时段',
    `normal`     text               NOT NULL COMMENT '平段时段',
    `valley`     text               NOT NULL COMMENT '低谷时段',
    `deep`       text               NOT NULL COMMENT '深谷时段',
    `created_at` timestamp          NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `created_by` bigint                      DEFAULT NULL COMMENT '创建者',
    `updated_at` timestamp          NULL     DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `updated_by` bigint                      DEFAULT NULL COMMENT '更新者',
    `deleted`    tinyint(1)         NOT NULL DEFAULT '0' COMMENT '删除标记'
) COMMENT = '测算数据时段表';

CREATE TABLE `b_monthly_electricity_price`
(
    `id`           BIGINT PRIMARY KEY NOT NULL COMMENT 'ID',
    `ref_id`       BIGINT             NOT NULL COMMENT '关联的区域测算数据表id',
    `month`        INT                NOT NULL COMMENT '月份',
    `top_price`    DECIMAL(19, 6)     NOT NULL COMMENT '尖峰电价(￥ /kwh)',
    `peak_price`   DECIMAL(19, 6)     NOT NULL COMMENT '高峰电价(￥ /kwh)',
    `normal_price` DECIMAL(19, 6)     NOT NULL COMMENT '平段电价(￥/kwh)',
    `valley_price` DECIMAL(19, 6)     NOT NULL COMMENT '低谷电价(￥/kWh)',
    `deep_price`   DECIMAL(19, 6)     NOT NULL COMMENT '深谷电价(￥/kwh)',
    `created_at`   timestamp          NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `created_by`   bigint                      DEFAULT NULL COMMENT '创建者',
    `updated_at`   timestamp          NULL     DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `updated_by`   bigint                      DEFAULT NULL COMMENT '更新者',
    `deleted`      tinyint(1)         NOT NULL DEFAULT '0' COMMENT '删除标记'
) COMMENT = '月度电价表';
