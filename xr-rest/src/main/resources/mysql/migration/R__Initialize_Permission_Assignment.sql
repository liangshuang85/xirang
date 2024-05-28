-- 全局角色
REPLACE INTO `s_permission_assignment` (`id`, `permission_code`, `subject_id`)
VALUES (1, 'CLUE:LIST', 1);
REPLACE INTO `s_permission_assignment` (`id`, `permission_code`, `subject_id`)
VALUES (2, 'FRAMEWORK_AGREEMENT:LIST', 1);
REPLACE INTO `s_permission_assignment` (`id`, `permission_code`, `subject_id`)
VALUES (3, 'PROJECT:LIST', 1);
-- 实例角色
REPLACE INTO `s_permission_assignment` (`id`, `permission_code`, `subject_id`)
VALUES (10001, 'CLUE:ASSIGN', 10001);
REPLACE INTO `s_permission_assignment` (`id`, `permission_code`, `subject_id`)
VALUES (10002, 'CLUE:EDIT', 10001);
REPLACE INTO `s_permission_assignment` (`id`, `permission_code`, `subject_id`)
VALUES (10003, 'CLUE:LIST', 10001);
REPLACE INTO `s_permission_assignment` (`id`, `permission_code`, `subject_id`)
VALUES (10004, 'CLUE:VIEW', 10001);
REPLACE INTO `s_permission_assignment` (`id`, `permission_code`, `subject_id`)
VALUES (10005, 'FRAMEWORK_AGREEMENT:ASSIGN', 10002);
REPLACE INTO `s_permission_assignment` (`id`, `permission_code`, `subject_id`)
VALUES (10006, 'FRAMEWORK_AGREEMENT:EDIT', 10002);
REPLACE INTO `s_permission_assignment` (`id`, `permission_code`, `subject_id`)
VALUES (10007, 'FRAMEWORK_AGREEMENT:LIST', 10002);
REPLACE INTO `s_permission_assignment` (`id`, `permission_code`, `subject_id`)
VALUES (10008, 'FRAMEWORK_AGREEMENT:VIEW', 10002);
REPLACE INTO `s_permission_assignment` (`id`, `permission_code`, `subject_id`)
VALUES (10009, 'PROJECT:ASSIGN', 10003);
REPLACE INTO `s_permission_assignment` (`id`, `permission_code`, `subject_id`)
VALUES (10010, 'PROJECT:EDIT', 10003);
REPLACE INTO `s_permission_assignment` (`id`, `permission_code`, `subject_id`)
VALUES (10011, 'PROJECT:LIST', 10003);
REPLACE INTO `s_permission_assignment` (`id`, `permission_code`, `subject_id`)
VALUES (10012, 'PROJECT:VIEW', 10003);
