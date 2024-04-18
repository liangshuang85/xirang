ALTER TABLE `b_framework_agreement_channel_entry`
    MODIFY COLUMN `framework_agreement_id` bigint NOT NULL COMMENT '框架协议项目ID' AFTER `counterpart_position`;

ALTER TABLE `b_framework_agreement_project`
    MODIFY COLUMN `framework_agreement_id` bigint NOT NULL COMMENT '框架协议项目ID' AFTER `land_resource`;

ALTER TABLE `b_framework_agreement_project_funding`
    MODIFY COLUMN `framework_agreement_id` bigint NOT NULL COMMENT '框架协议项目ID' AFTER `id`;
