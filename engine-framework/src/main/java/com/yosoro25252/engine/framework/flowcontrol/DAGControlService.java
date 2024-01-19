package com.yosoro25252.engine.framework.flowcontrol;

import com.yosoro25252.engine.framework.enums.BuildGraphStyleEnum;
import com.yosoro25252.engine.framework.pojo.Context;
import com.yosoro25252.engine.framework.pojo.Graph;
import com.yosoro25252.engine.framework.pojo.GraphCheckInfo;
import com.yosoro25252.engine.framework.pojo.GraphStructureInfo;
import com.yosoro25252.engine.framework.processors.DAGNodeProcessor;
import com.yosoro25252.engine.framework.utils.DAGUtils;
import com.yosoro25252.engine.framework.utils.GsonUtils;
import lombok.extern.slf4j.Slf4j;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * @author：yosoro25252
 * @date：2024/1/16
 * @desc: DAG组件调度服务
 */

@Slf4j
public class DAGControlService {

    private Map<String, ThreadPoolExecutor> threadPoolMap;


    public DAGControlService(Map<String, ThreadPoolExecutor> threadPoolMap) {
        this.threadPoolMap = threadPoolMap;
    }

    public Graph buildGraph(List<DAGNodeProcessor> processorList,
                            List<String> graphInputParamList,
                            List<String> graphOutputParamList,
                            String graphName,
                            String buildStyle,
                            int timeout) {
        // 根据出入参构建结点依赖信息
        if (BuildGraphStyleEnum.FROM_PARAM.getName().equals(buildStyle)) {
            GraphCheckInfo paramReachable = DAGUtils.checkParamReachableAndResolveProcessorRelation(processorList, graphInputParamList, graphOutputParamList);
            if (! paramReachable.isLegal()) {
                log.error("建图失败: 参数 = {} 不可达", paramReachable.getErrorParam());
                throw new RuntimeException("建图失败: 参数" + paramReachable.getErrorParam() + "不可达");
            }
        }

        // 判环
        GraphCheckInfo containCycleResult = DAGUtils.checkGraphCycle(processorList);
        if (! containCycleResult.isLegal()) {
            log.error("建图失败: 结点 = {} 处有环", containCycleResult.getErrorNode().getProcessorName());
            throw new RuntimeException("建图失败: 结点" + containCycleResult.getErrorNode().getProcessorName() + "处有环");
        }

        // 配置结点依赖信息
        DAGUtils.setUpstreamProcessorInfo(processorList);

        // 序列构建
        List<DAGNodeProcessor> orderedProcessorList = DAGUtils.buildOrderedProcessorSequence(processorList);

        // 优化结点关系 - 让图结构更清晰
        DAGUtils.optimizeGraph(orderedProcessorList, graphInputParamList);

        // 图可视化
        GraphStructureInfo graphStructureInfo = DAGUtils.getGraphStructureInfo(processorList, graphInputParamList, graphOutputParamList);
        log.info("建图成功: graphName = {}, graphStructureInfo = {}", graphName, GsonUtils.getJsonStringFromObject(graphStructureInfo));

        // 建图
        return new Graph(graphName, timeout, processorList.size(), processorList, orderedProcessorList);
    }

    public void runGraph(Graph graph, Context context) throws ExecutionException, InterruptedException, TimeoutException {
        Map<DAGNodeProcessor, CompletableFuture<Void>> completableFutureMap = new HashMap<>();
        CompletableFuture<Void>[] allCompletableFuture = new CompletableFuture[graph.getSize()];
        for (int i = 0; i < graph.getSize(); i ++) {
            DAGNodeProcessor processor = graph.getOrderedNodeList().get(i);
            CompletableFuture<Void>[] upstreamCompletableFuture = new CompletableFuture[processor.getUpstreamNodeList().size()];
            for (int j = 0; j < processor.getUpstreamNodeList().size(); j ++) {
                upstreamCompletableFuture[j] = completableFutureMap.get(processor.getUpstreamNodeList().get(j));
            }
            CompletableFuture<Void> processorCompletableFuture = CompletableFuture.allOf(upstreamCompletableFuture)
                    .thenRunAsync(() -> processor.process(context), threadPoolMap.get(processor.getThreadPoolTag()));
            completableFutureMap.put(processor, processorCompletableFuture);
            allCompletableFuture[i] = processorCompletableFuture;
        }
        CompletableFuture.allOf(allCompletableFuture).get(graph.getTimeout(), TimeUnit.MILLISECONDS);
    }

}
