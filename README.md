# 基于 Spring Batch 实现的轻量级增强框架

`alatka-batch` 是一套基于 Spring Batch 构建的开箱即用批处理生态框架。它通过 **`alatka-batch-flow`** 提供可视化拖拽编排能力，将底层的
Step、Flow、Split 与 Decider 自动构建为可动态加载的 Job，让复杂批处理配置直观如画图；结合 **`alatka-batch-param`**
实现参数校验与动态维护；同时借助 **`alatka-batch-monitor`** 打造全链路可视化监控，实现低侵入、高可视化的现代批处理运维体验。

### 功能概述

- **alatka-batch-flow & alatka-batch-flow-admin**
    - 可视化拖拽配置 Step 间的顺序、分支、并行、决策关系
    - 自动解析流程图并动态组装为 Spring Batch Job，支持在线刷新
    - 开发者仅需实现 Step/Flow 等业务 Bean，与流程编排解耦
- **alatka-batch-monitor & alatka-batch-monitor-admin**
    - 跑批结果监控，支持子 Job（JobStep）嵌套查询
    - 高亮展示 Job/Step 的运行状态、执行节点信息
    - 异常堆栈查看与数据读写/跳过/重试指标统计
- **alatka-batch-param & alatka-batch-param-admin**
    - 提供动态参数的定义、默认值注入
    - 支持全局参数与 Job 级参数

### 项目结构

| 模块                         | 描述                                                |
|----------------------------|---------------------------------------------------|
| alatka-batch-monitor       | Spring Batch 跑批结果监控相关插件                           |
| alatka-batch-monitor-admin | 后台管理端，跑批结果监控查询                                    |
| alatka-batch-param         | Spring Batch Job参数动态生成                            |
| alatka-batch-param-admin   | 后台管理端，Job参数维护功能                                   |
| alatka-batch-flow          | 流程核心模块，包括job解析，加载等功能                              |
| alatka-batch-flow-admin    | 后台管理端，提供job流程维护、设计等功能                             |
| alatka-batch-infra         | 提供基础功能                                            |
| alatka-batch-example       | 示例模块，用于演示 alatka-batch-xxx、alatka-batch-xxx-admin |
| alatka-batch-bundle        | 集成 alatka-batch-xxx，简化依赖配置                        |
| alatka-batch-admin-bundle  | 集成 alatka-batch-xxx-admin，简化依赖配置                  |

`alatka-batch`、`alatka-dependencies`、`alatka`
相关制品已上传至阿里云仓库，如需下载可进行如下配置：[ :point_right: maven相关配置](https://gitee.com/asuka2001/alatka-batch/wikis/%E5%85%AB%E3%80%81maven%E7%9B%B8%E5%85%B3%E9%85%8D%E7%BD%AE)

### 版本对应关系

| alatka-batch | alatka-dependencies       | alatka                    |
|--------------|---------------------------|---------------------------|
| 0.7.0-jdk17  | 1.79.0-jdk17              | 1.79.0-jdk17              |
| 0.6.0-jdk17  | 1.78.0-jdk17              | 1.78.0-jdk17              |
| 0.5.0-jdk17  | 1.76.0-jdk17-1.77.0-jdk17 | 1.76.0-jdk17-1.77.0-jdk17 |
| 0.4.0-jdk17  | 1.75.0-jdk17              | 1.75.0-jdk17              |
| 0.3.0-jdk17  | 1.72.0-jdk17-1.74.0-jdk17 | 1.72.0-jdk17-1.74.0-jdk17 |
| 0.2.0-jdk17  | 1.71.0-jdk17              | 1.71.0-jdk17              |
| 0.1.0-jdk17  | 1.70.0-jdk17              | 1.70.0-jdk17              |

### [ :point_right: 访问wiki查看更多教程](https://gitee.com/asuka2001/alatka-batch/wikis)

### github地址

项目同步更新在github；如有需要， :point_right: [点击我访问](https://github.com/goGetSomeFries/alatka-batch)

### 感谢支持

如果觉得好用，欢迎推荐给身边同事同学朋友；也欢迎各位的issues和star，问题会及时回复，再次感谢大家的支持！