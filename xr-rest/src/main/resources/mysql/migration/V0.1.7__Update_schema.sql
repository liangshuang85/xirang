ALTER TABLE `b_visit`
    CHANGE COLUMN `clue_id` `ref_id` bigint NOT NULL COMMENT '拜访关联ID' AFTER `id`;