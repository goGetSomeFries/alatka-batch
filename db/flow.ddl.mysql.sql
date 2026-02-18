CREATE TABLE `ALK_BATCH_FLOW_GROUP`
(
    `G_ID`        int unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
    `G_CREATE_AT` datetime     NOT NULL COMMENT '创建日期',
    `G_UPDATE_AT` datetime              COMMENT '更新日期',
    `G_KEY`       varchar(50)  NOT NULL COMMENT '关键字',
    `G_NAME`      varchar(100) NOT NULL COMMENT '名称',
    `G_ENABLED`   tinyint(1)   NOT NULL COMMENT '是否可用',
    PRIMARY KEY (`G_ID`),
    UNIQUE KEY `ALK_BATCH_FLOW_GROUP_UNIQUE` (`G_KEY`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='流程组表';

CREATE TABLE `ALK_BATCH_FLOW`
(
    `F_ID`        int unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
    `F_CREATE_AT` datetime     NOT NULL COMMENT '创建日期',
    `F_UPDATE_AT` datetime              COMMENT '更新日期',
    `F_KEY`       varchar(50)  NOT NULL COMMENT '关键字',
    `F_NAME`      varchar(100) NOT NULL COMMENT '名称',
    `F_ENABLED`   tinyint(1)   NOT NULL COMMENT '是否可用',
    `G_KEY`       varchar(50)           COMMENT '流程组关键字',
    PRIMARY KEY (`F_ID`),
    UNIQUE KEY `ALK_BATCH_FLOW_UNIQUE` (`F_KEY`, `G_KEY`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='流程表';

CREATE TABLE `ALK_BATCH_FLOW_DATA`
(
    `D_ID`          int unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
    `D_CREATE_AT`   datetime     NOT NULL COMMENT '创建日期',
    `D_UPDATE_AT`   datetime              COMMENT '更新日期',
    `D_DATA`        TEXT         NOT NULL COMMENT '内容',
    `D_PREVIOUS_ID` int unsigned          COMMENT '上一部署状态主键',
    `D_STATUS`      varchar(20)  NOT NULL COMMENT '状态',
    `F_ID`          varchar(50)  NOT NULL COMMENT '流程主键',
    PRIMARY KEY (`C_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='流程数据表';