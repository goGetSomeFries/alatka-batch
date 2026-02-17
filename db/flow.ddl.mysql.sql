CREATE TABLE `ALK_BATCH_FLOW_GROUP`
(
    `G_ID`        int unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
    `G_CREATE_AT` datetime     NOT NULL COMMENT '创建日期',
    `G_UPDATE_AT` datetime              COMMENT '更新日期',
    `G_KEY`       varchar(50)  NOT NULL COMMENT '关键字',
    `G_NAME`      varchar(100) NOT NULL COMMENT '名称',
    `G_ENABLED`   tinyint(1)   NOT NULL COMMENT '是否可用',
    PRIMARY KEY (`FG_ID`),
    UNIQUE KEY `ALK_BATCH_FLOW_GROUP_UNIQUE` (`G_KEY`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='流程组表';