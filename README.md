# 基于DDD的应用脚手架

应用架构的核心要素就是如何把代码组织起来，处理模块(module)， 组件， 包， 类之间
的关系。


## 分层明细说明
```yaml
--root
    --application: 应用层是程序的入口，整合和组合domain提供的能力。
        --rpc: JSF provider对外提供的接口实现
        --controller: springMVC提供的controller
        --listener: MQ消息监听器
        --task: 调度任务
        --translate: 将内部的BO映射为外部的VO/Entity
        --model: VO对象
    --adapter: 适配器层（依赖domain层）
       --rpc: JSF consumer，外部服务
       --mq: 消息队列sender模块
       --translate: 将外部数据结构映射为内部的DTO/BO
    --domain: 领域层
       --service: 领域服务可以按照自己情况灵活设计
       --facotry: 工厂
       --query/command: CRQS模式
       --event: 事件
       --model: 对象和实体
       --translate: 对象实体映射转换
       --facade: 针对adpater和infrastructre层的接口定义
    --infrastructure: (依赖domain层)
       --repository: 持久化层，包括db模型，sql读写等
       --cache: Redis缓存读写
       --producer: MQ消息生成，即发送MQ消息。
       --config: 配置信息，例如ducc配置、数据库、缓存配置等
       --translate: 将存储层的数据结构PO映射为内部的BO
       --utils: 工具集合
       --common: 公共层
        --exception: 异常主要分为业务异常和系统异常。系统异常需要研发处理。业务异常需要具备监控能力。
        --utils: 工具类
        --enums: 枚举类
        --constant: 全局公共常量池
    --worker: 异步服务启动
    --client: jsf SDK
```

## 架构层级说明
### Application层
应用层是DDD中的顶层，负责协调和组织领域对象的交互。它接收来自用户界面或外部系统的请求，并将其转发给领域层进行处理。应用层负责定义应用的用例（Use Cases），处理事务边界和协调领域对象的操作。它不包含业务逻辑，而是将请求转化为领域对象的操作。应用层还可以包含获取输入，组装上下文，参数校验，异常定义，发送事件通知等。<br>
### Domain层
主要是封装了核心业务逻辑，并通过领域服务（Domain Service）和领域对象（Domain Entity）的方法对App层提供业务实体和业务逻辑计算。领域是应用的核心，不依赖任何其他层次。同时领域层会有一个facade层，当领域服务对外部有调用依赖时，通过定义facade接口实现控制反转。<br>
### Adapter层
负责与外部系统进行或者服务进行适配和集成，包括通信，数据缓存，接口适配等功能。<br>
此外强调， RPC consumer调用放在适配器层。适配器层专注于与外部系统的集成和适配，将外部系统的接口和数据格式转换为应用程序可以理解和处理的形式。将RPC调用放在适配器层可以更好地将与外部系统相关的技术细节与应用程序的业务逻辑和领域对象进行解耦，提高应用程序的可扩展性和可维护性。<br>
对于所有出站适配层，都需要通过实现facade接口实现控制反转。<br>
### 基础设施层(Infrastructure)
负责提供支持应用程序运行的基础设施，包括与具体技术相关的实现。基础设施层通常包括与数据库、消息队列、缓存、外部服务等进行交互的代码，以及一些通用的工具类和配置，也包括filter等实现。<br>
基础设施层和适配器层之间的关系是：<br>
基础设施层提供了与具体技术相关的实现，例如数据库访问、消息队列连接、缓存操作等。适配器层可以使用基础设施层提供的功能来与外部系统进行交互。<br>
适配器层通过适配器模式或类似的机制，将外部系统的接口和数据格式转换为应用程序可以理解和处理的形式。适配器层还负责将应用程序的请求转发给基础设施层进行具体的操作。<br>
基础设施层和适配器层一起工作，使得应用程序能够与外部系统进行集成，并且将与外部系统相关的技术细节与应用程序的业务逻辑和领域对象进行解耦。这样可以实现应用程序的可扩展性、可维护性和可测试性。<br>
对于一些无复杂逻辑的，也可以直接让上游掉基础设施层，不必一定通过Adapter层。<br>

### 异步服务
我们提倡异步任务单独拆分出来，作为独立服务进行部署，例如异步任务和异步消息处理等。 worker module可以单独启动。

###

### 小结
本框架是结合了DDD思想和六边形架构思想，经过小组内兄弟们的的反馈意见，整理出符合大家开发习惯，同时有有利于模块解耦及协同开发的脚手架。该脚手架不会限制大家能力和发挥。<br>
如果你精通DDD，你可以在domain层采用标准的充血模型和子域拆分模式编写你的代码； 如果你精通MVC，该框架也可以简化为大家熟悉的MVC开发模式。对于model的处理，也可灵活应对，在不影响整体代码架构的情况下，允许不过度设计及对象多度封装，鼓励敏捷迭代和定期重构。<br>
但有一个核心思想希望大家谨记：***我们尽量保证我们的代码开发符合开闭原则，能够通过增加类和方法的方式实现新功能迭代，尽量就要避免频繁修改某个方法或者某个类。***<br>
最后也给大家一个DDD应用架构的介绍文档。文档链接：https://joyspace.jd.com/pages/9L8iYWtUJkG8EwnVhvKz

## maven私服脚手架下载脚本

```sh
mvn archetype:generate \
            -DarchetypeGroupId=com.jd.magnus \
            -DarchetypeArtifactId=magnus-multi-ddd-archetype \
            -DarchetypeVersion=1.0.0-SNAPSHOT \
            -DinteractiveMode=false \
            -DarchetypeCatalog=remote \
            -Dversion=1.0.0-SNAPSHOT \
            -DgroupId=com.jdl.sps \
            -DartifactId=bff-demo1
```

## client module SDK打包推送远端仓库建议和说明
文档标题：项目版本管理和自动化发版推仓流程方法<br>
文档链接：https://joyspace.jd.com/pages/9jYvX2tJFZJiBpYfQTvd

## 代码评审规范说明和建议
文档标题：2 代码评审规范<br>
文档链接：https://joyspace.jd.com/pages/iPKIOoBDEMGhGKBtrW9t

## git分支管理和流水线配置说明和建议
文档标题：Git分支管理及代码评审流程梳理<br>
文档链接：https://joyspace.jd.com/pages/nVhGe0PuYGEOfB4wN0fw

## 本工程spring配置文件管理和加载说明
文档标题：Spring工程打包统一镜像技术方案<br>
文档链接：https://joyspace.jd.com/pages/pxfEOGTHwhvGMoCDkLju

# 联系我们
* 作者ERP: zhaoyongping8

## 开源协议
F开源采用内部开源协议，详情[LICENSE](https://joyspace.jd.com/page/QLHIIOzLazLROBMjsyG0)


