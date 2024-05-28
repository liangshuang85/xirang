-- 将`b_framework_agreement_channel_entry`表中的数据合并到`b_channel_entry`表

ALTER TABLE `b_channel_entry`
    DROP COLUMN `status`,
    CHANGE COLUMN `clue_id` `ref_id` bigint NOT NULL COMMENT '关联对象ID' AFTER `id`;

INSERT INTO `b_channel_entry` (`id`, `ref_id`, `partner_name`, `background`, `social_relations`, `contact_name`,
                               `counterpart_name`, `contact_position`, `counterpart_position`,
                               `created_at`, `created_by`, `updated_at`, `updated_by`, `deleted`)
SELECT `id`,
       `framework_agreement_id` AS `ref_id`,
       `partner_name`,
       `background`,
       `social_relations`,
       `contact_name`,
       `counterpart_name`,
       `contact_position`,
       `counterpart_position`,
       `created_at`,
       `created_by`,
       `updated_at`,
       `updated_by`,
       `deleted`
FROM `b_framework_agreement_channel_entry`;

DROP TABLE `b_framework_agreement_channel_entry`;
