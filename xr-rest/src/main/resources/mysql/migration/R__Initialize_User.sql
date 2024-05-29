REPLACE INTO `s_user` (`id`, `username`, `password_hash`, `nickname`, `full_name`, `phone_number`, `email`, `admin`, `blocked`)
VALUES (1000, 'agent', '', 'Agent', '代理人', NULL, NULL, 0, 0);
REPLACE INTO `s_user` (`id`, `username`, `password_hash`, `nickname`, `full_name`, `phone_number`, `email`, `admin`, `blocked`)
VALUES (10000, 'admin', '{noop}1234567890', 'Administrator', '管理员', NULL, NULL, 0, 0);
