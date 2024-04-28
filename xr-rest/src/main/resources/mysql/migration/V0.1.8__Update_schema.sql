ALTER TABLE `b_clue`
    MODIFY COLUMN `level` varchar(1) NULL COMMENT '线索级别' AFTER `status`;

ALTER TABLE `b_channel_entry`
    MODIFY COLUMN `partner_name` varchar(100) NULL COMMENT '合伙人姓名' AFTER `clue_id`,
    MODIFY COLUMN `background` text NULL COMMENT '工作背景' AFTER `partner_name`,
    MODIFY COLUMN `social_relations` text NULL COMMENT '社会关系' AFTER `background`,
    MODIFY COLUMN `contact_name` varchar(100) NULL COMMENT '联系人姓名' AFTER `social_relations`,
    MODIFY COLUMN `counterpart_name` varchar(100) NULL COMMENT '对接人姓名' AFTER `contact_name`,
    MODIFY COLUMN `contact_position` varchar(200) NULL COMMENT '联系人职务' AFTER `counterpart_name`,
    MODIFY COLUMN `counterpart_position` varchar(200) NULL COMMENT '对接人职务' AFTER `contact_position`,
    MODIFY COLUMN `status` varchar(20) NULL COMMENT '线索状态' AFTER `counterpart_position`;

ALTER TABLE `b_funding`
    MODIFY COLUMN `wind_power_resource` varchar(255) NULL COMMENT '风力发电资源' AFTER `clue_id`,
    MODIFY COLUMN `photovoltaic_resource` varchar(255) NULL COMMENT '光伏发电资源' AFTER `wind_power_resource`,
    MODIFY COLUMN `grid_energy_price` double NULL COMMENT '新能源并网电价' AFTER `photovoltaic_resource`,
    MODIFY COLUMN `storage_standards` varchar(255) NULL COMMENT '新能源配套储能标准' AFTER `grid_energy_price`,
    MODIFY COLUMN `energy_discarded` double NULL COMMENT '风力及光伏发电弃电量' AFTER `storage_standards`,
    MODIFY COLUMN `civil_heating_price` double NULL COMMENT '风力及光伏发电弃电量' AFTER `energy_discarded`,
    MODIFY COLUMN `commercial_heating_price` double NULL COMMENT '民用供暖价格' AFTER `civil_heating_price`,
    MODIFY COLUMN `heating_duration` double NULL COMMENT '年供暖时长' AFTER `commercial_heating_price`,
    MODIFY COLUMN `industry_base` varchar(255) NULL COMMENT '周边产业基础' AFTER `heating_duration`,
    MODIFY COLUMN `wind_operation_hours` double NULL COMMENT '风力发电时长' AFTER `industry_base`,
    MODIFY COLUMN `pv_operation_hours` double NULL COMMENT '光伏发电时长' AFTER `wind_operation_hours`,
    MODIFY COLUMN `industrial_electricity_price` double NULL COMMENT '工商业用电综合电价' AFTER `pv_operation_hours`,
    MODIFY COLUMN `civil_heating_area` double NULL COMMENT '民用供暖面积' AFTER `industrial_electricity_price`,
    MODIFY COLUMN `commercial_heating_area` double NULL COMMENT '工商业供暖面积' AFTER `civil_heating_area`;

ALTER TABLE `b_framework_agreement_channel_entry`
    MODIFY COLUMN `partner_name` varchar(40) NULL COMMENT '渠道合伙人姓名' AFTER `id`,
    MODIFY COLUMN `background` text NULL COMMENT '重要工作背景' AFTER `partner_name`,
    MODIFY COLUMN `social_relations` text NULL COMMENT '重要社会关系' AFTER `background`,
    MODIFY COLUMN `contact_name` varchar(40) NULL COMMENT '关键联系人姓名' AFTER `social_relations`,
    MODIFY COLUMN `counterpart_name` varchar(40) NULL COMMENT '政府对接人姓名' AFTER `contact_name`,
    MODIFY COLUMN `contact_position` varchar(40) NULL COMMENT '关键联系人职务' AFTER `counterpart_name`,
    MODIFY COLUMN `counterpart_position` varchar(40) NULL COMMENT '政府对接人职务' AFTER `contact_position`;

ALTER TABLE `b_framework_agreement_project`
    MODIFY COLUMN `construction_scale` double NULL COMMENT '建设规模' AFTER `id`,
    MODIFY COLUMN `wind_resource` text NULL COMMENT '风能资源' AFTER `construction_scale`,
    MODIFY COLUMN `solar_resource` text NULL COMMENT '光伏资源' AFTER `wind_resource`,
    MODIFY COLUMN `land_resource` text NULL COMMENT '土地资源' AFTER `solar_resource`;

ALTER TABLE `b_framework_agreement_project_funding`
    MODIFY COLUMN `wind_power_resource` varchar(255) NULL COMMENT '风力发电资源' AFTER `framework_agreement_id`,
    MODIFY COLUMN `photovoltaic_resource` varchar(255) NULL COMMENT '光伏发电资源' AFTER `wind_power_resource`,
    MODIFY COLUMN `grid_energy_price` double NULL COMMENT '新能源并网电价' AFTER `photovoltaic_resource`,
    MODIFY COLUMN `storage_standards` varchar(255) NULL COMMENT '新能源配套储能标准' AFTER `grid_energy_price`,
    MODIFY COLUMN `energy_discarded` double NULL COMMENT '风力及光伏发电弃电量' AFTER `storage_standards`,
    MODIFY COLUMN `civil_heating_price` double NULL COMMENT '风力及光伏发电弃电量' AFTER `energy_discarded`,
    MODIFY COLUMN `commercial_heating_price` double NULL COMMENT '民用供暖价格' AFTER `civil_heating_price`,
    MODIFY COLUMN `heating_duration` double NULL COMMENT '年供暖时长' AFTER `commercial_heating_price`,
    MODIFY COLUMN `industry_base` varchar(255) NULL COMMENT '周边产业基础' AFTER `heating_duration`,
    MODIFY COLUMN `wind_operation_hours` double NULL COMMENT '风力发电时长' AFTER `industry_base`,
    MODIFY COLUMN `pv_operation_hours` double NULL COMMENT '光伏发电时长' AFTER `wind_operation_hours`,
    MODIFY COLUMN `industrial_electricity_price` double NULL COMMENT '工商业用电综合电价' AFTER `pv_operation_hours`,
    MODIFY COLUMN `civil_heating_area` double NULL COMMENT '民用供暖面积' AFTER `industrial_electricity_price`,
    MODIFY COLUMN `commercial_heating_area` double NULL COMMENT '工商业供暖面积' AFTER `civil_heating_area`;

ALTER TABLE `b_project_information`
    MODIFY COLUMN `project_id` double NOT NULL COMMENT '项目ID' AFTER `id`,
    MODIFY COLUMN `construction_scale` double NULL COMMENT '建设规模' AFTER `project_id`,
    MODIFY COLUMN `wind_resource` text NULL COMMENT '风能资源' AFTER `construction_scale`,
    MODIFY COLUMN `solar_resource` text NULL COMMENT '光伏资源' AFTER `wind_resource`,
    MODIFY COLUMN `land_resource` text NULL COMMENT '土地资源' AFTER `solar_resource`,
    MODIFY COLUMN `proposed_location` varchar(255) NULL COMMENT '拟建地点' AFTER `land_resource`,
    MODIFY COLUMN `construction_scale_and_content` varchar(255) NULL COMMENT '建设规模及内容' AFTER `proposed_location`,
    MODIFY COLUMN `total_land` double NULL COMMENT '总用地' AFTER `construction_scale_and_content`,
    MODIFY COLUMN `total_investment` double NULL COMMENT '项目总投资' AFTER `total_land`,
    MODIFY COLUMN `funding_source` varchar(255) NULL COMMENT '资金来源' AFTER `total_investment`,
    MODIFY COLUMN `project_start_time` datetime NULL COMMENT '项目建设周期开始时间' AFTER `funding_source`,
    MODIFY COLUMN `project_end_time` datetime NULL COMMENT '项目建设周期结束时间' AFTER `project_start_time`;
