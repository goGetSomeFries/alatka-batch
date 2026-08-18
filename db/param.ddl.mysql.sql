CREATE TABLE `ALK_BATCH_PARAM`
(
    `P_ID`        int unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
    `P_CREATE_BY` varchar(50)  NOT NULL COMMENT '创建人',
    `P_CREATE_AT` datetime     NOT NULL COMMENT '创建日期',
    `P_UPDATE_BY` varchar(50) COMMENT '更新人',
    `P_UPDATE_AT` datetime COMMENT '更新日期',
    `P_NAME`      varchar(100) NOT NULL COMMENT '名称',
    `P_KEY`       varchar(100) NOT NULL COMMENT '参数键',
    `P_VALUE`     varchar(100) NOT NULL COMMENT '参数值',
    `P_DESC`      varchar(200) COMMENT '描述',
    `P_TYPE`      varchar(10)  NOT NULL COMMENT '类型',
    `P_ENABLED`   tinyint(1)   NOT NULL COMMENT '是否可用',
    PRIMARY KEY (`P_ID`),
    UNIQUE KEY `ALK_BATCH_PARAM_UNIQUE` (`P_NAME`, `P_KEY`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='参数表';
