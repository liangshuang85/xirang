ALTER TABLE `b_regional_measurement_data`
    CHANGE COLUMN `system_operation_cost` `operation_fee_discount` decimal(19, 6) NULL DEFAULT NULL COMMENT '系统运行费折价(￥ /kWh)' AFTER `line_loss_price`,
    ADD COLUMN `charging_subsidy_years`          int            NULL COMMENT '充电补贴年限(年)' AFTER `charging_subsidy`,
    ADD COLUMN `discharging_subsidy`             decimal(19, 6) NULL COMMENT '放电补贴(￥/kWh)' AFTER `charging_subsidy_years`,
    ADD COLUMN `discharging_subsidy_years`       int            NULL COMMENT '放电补贴年限(年)' AFTER `discharging_subsidy`,
    ADD COLUMN `income_tax_justification`        text           NULL COMMENT '所得税理由' AFTER `carbon_asset_value_added_tax`,
    ADD COLUMN `grid_connection_line_loss_price` decimal(19, 6) NULL COMMENT '上网环节线损电价(￥ /kWh)' AFTER `operation_fee_discount`,
    ADD COLUMN `market_charging_price`           decimal(19, 6) NULL COMMENT '市场充电价格(￥ /kWh)' AFTER `id`,
    ADD COLUMN `static_charging_price`           decimal(19, 6) NULL COMMENT '固定充电价格(￥ /kWh)' AFTER `id`;
