# 基于Spring Batch实现的可视化流程编排库

为 Spring Batch 提供可视化流程编排能力。开发者只需实现底层 Step、Flow 等逻辑并注册为 Spring Bean，通过拖拽流程图即可完成
Job 中 Step Flow、Split 并行、条件决策的串联配置，自动构建完整 Job，并支持动态加载；让复杂的批处理作业配置像画流程图一样直观。

### 功能概述

- 可视化拖拽配置 Step 间的顺序、分支、并行、决策关系
- 自动解析流程图并动态组装为 Spring Batch Job，支持在线刷新
- 开发者仅需实现 Step/Flow 等业务 Bean，与流程编排解耦

### 项目结构

| 模块                      | 描述                                                  |
|-------------------------|-----------------------------------------------------|
| alatka-batch-flow       | 流程核心模块，包括job解析，加载等功能                                |
| alatka-batch-flow-admin | 后台管理端，提供job流程维护、设计等功能                               |
| alatka-batch-infra      | 提供基础功能                                              |
| alatka-batch-example    | 示例模块，用于演示 alatka-batch-flow、alatka-batch-flow-admin |

### 版本对应关系

| alatka-batch | alatka-dependencies | alatka |
|--------------|---------------------|--------|
| 0.3.0        | 1.72.0              | 1.72.0 |
| 0.2.0        | 1.71.0              | 1.71.0 |
| 0.1.0        | 1.70.0              | 1.70.0 |

`alatka-batch`、`alatka-dependencies`、`alatka`
相关制品已上传至阿里云仓库，如需下载可进行如下配置：[ :point_right: maven相关配置](https://gitee.com/asuka2001/alatka-batch/wikis/%E5%85%AB%E3%80%81maven%E7%9B%B8%E5%85%B3%E9%85%8D%E7%BD%AE)

### [ :point_right: 访问wiki查看更多教程](https://gitee.com/asuka2001/alatka-batch/wikis)

### github地址

项目同步更新在github；如有需要， :point_right: [点击我访问](https://github.com/goGetSomeFries/alatka-batch)

### 感谢支持

如果觉得好用，欢迎推荐给身边同事同学朋友；也欢迎各位的issues和star，问题会及时回复，再次感谢大家的支持！