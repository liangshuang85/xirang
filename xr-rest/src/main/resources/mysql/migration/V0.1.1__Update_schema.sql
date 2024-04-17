CREATE TABLE `b_attachment`
(
    `id`         bigint unsigned NOT NULL,
    `owner_type` varchar(128)             DEFAULT NULL COMMENT '属主类型',
    `owner_id`   bigint unsigned          DEFAULT NULL COMMENT '属主ID',
    `file_name`  varchar(1024)            DEFAULT NULL COMMENT '文件名',
    `file_path`  text COMMENT '文件路径',
    `file_size`  bigint unsigned          DEFAULT '0' COMMENT '文件大小，单位为字节',
    `file_url`   text COMMENT '文件URL',
    `file_type`  varchar(192)             DEFAULT 'OTHERS' COMMENT '文件类型',
    `mime_type`  varchar(128)             DEFAULT NULL COMMENT '文件MIME type',
    `checksum`   varchar(128)    NOT NULL COMMENT '文件Checksum',
    `created_at` timestamp       NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `created_by` bigint unsigned          DEFAULT '0' COMMENT '创建者',
    `updated_at` timestamp       NULL     DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `updated_by` bigint unsigned          DEFAULT '0' COMMENT '更新者',
    `deleted`    tinyint(1)      NOT NULL DEFAULT '0' COMMENT '删除标记',
    PRIMARY KEY (`id`) USING BTREE
) COMMENT ='附件';