ALTER TABLE `b_clue`
    CHANGE COLUMN `hasOfficialVisit` `has_official_visit` tinyint(1) NOT NULL COMMENT '是否已经正式拜访' AFTER `assignee_id`;
