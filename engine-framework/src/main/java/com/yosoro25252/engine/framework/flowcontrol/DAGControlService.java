package com.yosoro25252.engine.framework.flowcontrol;

import com.yosoro25252.engine.framework.enums.BuildGraphStyleEnum;
import com.yosoro25252.engine.framework.exception.BuildGraphException;
import com.yosoro25252.engine.framework.pojo.Context;
import com.yosoro25252.engine.framework.pojo.Graph;
import com.yosoro25252.engine.framework.pojo.GraphCheckInfo;
import com.yosoro25252.engine.framework.processors.DAGNodeProcessor;
import com.yosoro25252.engine.framework.utils.DAGUtils;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class DAGControlService {

    private Map<String, ThreadPoolExecutor> threadPoolMap;

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
                throw new BuildGraphException();
            }
        }

        // 判环
        GraphCheckInfo containCycleResult = DAGUtils.checkGraphCycle(processorList);
        if (! containCycleResult.isLegal()) {
            throw new BuildGraphException();
        }

        // 配置结点依赖信息
        DAGUtils.setUpstreamProcessorInfo(processorList);

        // 序列构建
        List<DAGNodeProcessor> orderedProcessorList = DAGUtils.buildOrderedProcessorSequence(processorList);

        // 优化结点关系
        DAGUtils.optimizeGraph(orderedProcessorList, graphInputParamList);

        // 建图
        return new Graph(graphName, timeout, processorList.size(), processorList, orderedProcessorList);
    }

    public void runGraph(Graph graph, Context context) {
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
        try {
            CompletableFuture.allOf(allCompletableFuture).get(graph.getTimeout(), TimeUnit.MILLISECONDS);
        } catch (InterruptedException | TimeoutException e) {

        } catch (Exception e) {

        }
    }

}
