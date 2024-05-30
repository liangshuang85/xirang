-- 将`b_funding`表中的数据合并到`b_basic_data`表
INSERT INTO `b_basic_data`(`id`, `wind_power_resource`, `photovoltaic_resource`, `wind_operation_hours`,
                           `pv_operation_hours`, `grid_energy_price`, `energy_discarded`, `civil_heat_prices`,
                           `civil_heat_area`, `industry_heat_prices`, `industry_heat_area`, `heat_duration_hours`,
                           `ref_id`)
SELECT `id`,
       `wind_power_resource`,
       `photovoltaic_resource`,
       `wind_operation_hours`,
       `grid_energy_price`,
       `pv_operation_hours`,
       `energy_discarded`,
       `civil_heating_price`      AS `civil_heat_prices`,
       `civil_heating_area`       AS `civil_heat_area`,
       `commercial_heating_price` AS `industry_heat_prices`,
       `commercial_heating_area`  AS `industry_heat_area`,
       `heating_duration`         AS `heat_duration_hours`,
       `clue_id`                  AS `ref_id`
FROM `b_funding`;

-- 将`b_framework_agreement_project_funding`表中的数据合并到`b_basic_data`表
INSERT INTO `b_basic_data`(`id`, `wind_power_resource`, `photovoltaic_resource`, `wind_operation_hours`,
                           `pv_operation_hours`, `energy_discarded`, `civil_heat_prices`, `civil_heat_area`,
                           `industry_heat_prices`, `industry_heat_area`, `heat_duration_hours`,
                           `ref_id`)
SELECT `id`,
       `wind_power_resource`,
       `photovoltaic_resource`,
       `wind_operation_hours`,
       `pv_operation_hours`,
       `energy_discarded`,
       `civil_heating_price`      AS `civil_heat_prices`,
       `civil_heating_area`       AS `civil_heat_area`,
       `commercial_heating_price` AS `industry_heat_prices`,
       `commercial_heating_area`  AS `industry_heat_area`,
       `heating_duration`         AS `heat_duration_hours`,
       `framework_agreement_id`   AS `ref_id`
FROM `b_framework_agreement_project_funding`;

DROP TABLE `b_funding`;
DROP TABLE `b_framework_agreement_project_funding`;