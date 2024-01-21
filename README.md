## Java DAG编排框架

### 一、DAG编排介绍

DAG编排是对执行逻辑的抽象。

我们可以把一个执行逻辑（e.g. 接口请求处理）看作是一个流程，而执行逻辑中的每一步（e.g. 查A表、查B表、写C表），是一个子流程。我们可以发现，在一个大流程中，不是每个子流程都是相互依赖的、需要遵照特定的顺序执行。它们中，有的可能有依赖，有的可能无依赖。
我们可以按照依赖关系，把子流程画作结点，子流程依赖关系画作结点间的有向边（B依赖A => A -> B），这样我们就能得到一张有向无环图（DAG）。
这样，每个流程，就被我们变作子流程构成的DAG。只要我们按照依赖关系来执行所有子流程，大流程就被执行完成了。

通过配置子流程和其依赖关系，来完成大流程的开发，就是DAG编排。

在实践上，我们把每个子流程抽象一个组件，通过配置组件和依赖关系的方式，就可以将一个流程声明出来。再搭配上组件调度引擎，能够按序自动执行所有组件，一个大流程就开发完成了。

### 二、框架介绍

我们设计了一个简单的支持DAG编排的流程框架，它使用CompletableFuture完成DAG组件的异步编排。
同时，我们也提供了DAG执行引擎，完成流程配置和调度执行的工作。
并且，我们进一步扩展了框架，使框架涵盖了从接受参数到结果返回的全流程。

我们的项目的目的是：
- 开发提效：对组件（子流程）进行高效复用，且框架抽取了部分公用逻辑，降低开发成本
- 功能解耦：对每个子流程单独封装成组件，每个流程按需引入所需组件，降低耦合度
- 性能优化：按照依赖关系、自动完成组件的调度执行，避免无意义的串行增加耗时

我们把组件分为串行组件和DAG组件。
考虑到在一些业务中，DAG只是其中一个阶段、完成一部分任务，串行流程用得相对更多。
我们将跑DAG的部分设计成了一个特殊的串行组件DAGContainerProcessor，DAG组件需要在其中调度运行。
因此，框架的主体是串行执行的，但是其中配置的DAG流程中的DAG组件，会以DAG方式运行。
这种方式兼顾了只用DAG流程和串行、DAG混用的情形。
如果只用DAG，则配置一个DAGContainerProcessor的串行组件、并把DAG组件挂在其中即可；
如果只用串行，则配置串行组件、不用DAGContainerProcessor和DAG组件即可；
如果串行和DAG混用，则把需要DAG执行的部分用DAGContainerProcessor包起来、其他用串行组件即可。

### 三、框架使用

#### 1、串行组件配置

配置除DAG流程组件（DAGContainerProcessor）外的所有串行组件。每个串行组件应该时一个FlowProcessor的子类。
FlowProcessor继承自BaseProcessor，它会自动添加组件执行和耗时埋点，因此不需要在业务逻辑中重复埋点。
配置串行组件时需要配置其monitorService属性，用于埋点上报，我们在第五小节进行介绍。

当新建一个串行组件时，需要实现如下方法：

- getProcessorName：给组件起个名
- doProcess：组件的业务逻辑

详细配置可参照example模块/resource/flow-processors.xml示例。

#### 2、串行流程配置

配置所有的串行流程。每个串行流程应该是FlowContainer的实例。FlowContainer包含该串行流程需要的全部信息，依靠FlowControlService完成流程调度。

当配置一个串行流程时，我们需要配置如下内容：

- processorList
- serviceName：给流程起个名
- contextBuilder：流程上下文构造器，是一个ContextBuilder接口的实现。完成由请求入参到流程上下文的转换。可以在实现接口时，加入参数校验逻辑，参数问题后抛出异常，FlowControlService会捕获ContextBuilder的异常并作相应处理
- responseBuilder：返回结果构造器，是一个ResponseBuilder接口的实现。完成由流程上下文到请求结果的转换。接口定义了四个方法，分别对于不同情况下的返回结果构造（运行成功、参数错误、需要业务特殊处理的错误、其他错误）
- monitorService：埋点上报服务（第五小节介绍）
- flowControlService：串行流程控制服务（第五小节介绍）

详细配置可参照example模块/resource/flows.xml示例。


#### 3、DAG结点配置

配置所有的DAG组件。每个DAG组件应该是DAGNodeProcessor的实例。DAGNodeProcessor继承自BaseProcessor，因此也包含了组件执行和耗时的埋点。
和串行组件相同，新建一个DAG组件时，需要实现getProcessorName和doProcess方法。

DAG组件需要配置如下内容：
- inputParamList：结点入参（按出入参建图时必填）
- outputParamList：结点出参（按出入参建图时必填）
- downstreamNodeList：下游结点（按结点关系建图时必填）
- threadPoolTag：线程池标签（第五小节介绍）
- monitorService：埋点上报服务（第五小节介绍）

需要注意，因为目前DAG建图可以选择两种方式，因此结点inputParamList、outputParamList和downstreamNodeList可根据需要自由配置。
但是，如果建图方式是按参数，就只会用结点配置中的inputParamList和outputParamList；同理，如果建图方式是按结点关系，就只会用结点配置的downstreamNodeList信息。

详细配置可参照example模块/resource/dag-processors.xml示例。

#### 4、DAG流程配置

配置所有的DAG流程。每个图流程应该是DAGContainerProcessor的实例（注意它实际也是一个串行组件）。DAGContainerProcessor包含一个DAG的全部信息，由DAGControlService完成DAG流程调度。

当配置一个DAG流程时，我们要配置这些内容：

- inputParamList：图入参（由出入参建图时必填）
- outputParamList：图出参（由出入参建图必填）
- processorList：该DAG包含的所有结点，每个结点是一个DAGNodeProcessor的子类
- controlService：DAG流程控制服务（第五小节介绍）
- monitorService：埋点上报服务（第五小节介绍）
- graphName：给图起个名
- buildGraphStyle：建图方式，填from_param（由出入参建图）或from_neighbours（由结点关系建图）
- timeout：图流程执行超时时间，单位ms

详细配置可参照example模块/resource/graphs.xml示例。

#### 5、依赖服务配置

配置编排框架依赖的服务。具体而言，这一步我们需要配置如下几个服务：

- IMonitorService接口的实现，用于埋点上报，需要根据实际选用埋点方式进行逻辑实现
- DAGControlService的实例，用于DAG流程的调度控制，需要配置埋点上报服务和线程池
- FlowControlService的实例，用于串行流程的调度控制，需要配置埋点上报服务和线程池

注意在配置DAGControlService实例时，我们需要设置其threadPoolMap参数。它是一个线程池map：key是String类型，为线程池的tag；value是ThreadPoolExecutor类型。
这里线程池tag和DAG组件的threadPoolTag是联动的：在跑DAG流程时，框架会把结点根据其threadPoolTag放到DAGControlService.threadPoolMap的不同线程池中。
在并发实践上，我们通常把IO操作和CPU操作放在不同的线程池（它们参数配置也不相同）。
配置threadPoolMap时，我们也可以参照这种常规方式，配一个CPU线程池和一个IO线程池，并把DAG组件根据其业务逻辑，进行CPU/IO的区分，打上对于标签。

详细配置可参照example模块/resource/services.xml示例。