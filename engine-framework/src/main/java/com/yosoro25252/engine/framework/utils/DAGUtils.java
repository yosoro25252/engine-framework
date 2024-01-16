package com.yosoro25252.engine.framework.utils;

import com.yosoro25252.engine.framework.enums.ColorEnum;
import com.yosoro25252.engine.framework.pojo.GraphCheckInfo;
import com.yosoro25252.engine.framework.processors.DAGNodeProcessor;
import org.apache.commons.collections.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

public class DAGUtils {

    public static GraphCheckInfo checkGraphCycle(List<DAGNodeProcessor> processorList) {
        Map<DAGNodeProcessor, Integer> visitedStatusMap = new HashMap<>();
        for (DAGNodeProcessor processor : processorList) {
            GraphCheckInfo processorCheckResult = doCheckCycle(processor, visitedStatusMap);
            if (! processorCheckResult.isLegal()) {
                return processorCheckResult;
            }
        }
        return new GraphCheckInfo(true, null, null, null);
    }

    private static GraphCheckInfo doCheckCycle(DAGNodeProcessor processor, Map<DAGNodeProcessor, Integer> visitedStatusMap) {
        visitedStatusMap.put(processor, ColorEnum.GRAY.getCode());
        for (DAGNodeProcessor downstreamProcessor : processor.getDownstreamNodeList()) {
            int visitedStatus = visitedStatusMap.getOrDefault(downstreamProcessor, ColorEnum.WHITE.getCode());
            if (visitedStatus == ColorEnum.GRAY.getCode()) {
                // 已成环
                return new GraphCheckInfo(false, processor, null, null);
            }
            GraphCheckInfo downstreamCheckResult = doCheckCycle(downstreamProcessor, visitedStatusMap);
            if (! downstreamCheckResult.isLegal()) {
                // 已成环
                return downstreamCheckResult;
            }
        }
        visitedStatusMap.put(processor, ColorEnum.BLACK.getCode());
        return new GraphCheckInfo(true, null, null, null);
    }

    public static void optimizeGraph(List<DAGNodeProcessor> processorList) {

    }

    public static void setUpstreamProcessorInfo(List<DAGNodeProcessor> processorList) {
        for (DAGNodeProcessor processor : processorList) {
            for (DAGNodeProcessor downstreamProcessor : processor.getDownstreamNodeList()) {
                downstreamProcessor.getUpstreamNodeList().add(processor);
            }
        }
    }

    public static List<DAGNodeProcessor> buildOrderedProcessorSequence(List<DAGNodeProcessor> processorList) {
        List<DAGNodeProcessor> orderedProcessorList = new ArrayList<>(processorList.size());
        Map<DAGNodeProcessor, Integer> inDegreeMap = new HashMap<>();
        processorList.forEach(processor -> inDegreeMap.put(processor, processor.getUpstreamNodeList().size()));
        Queue<DAGNodeProcessor> queue = new LinkedList<>(processorList.stream().filter(processor -> processor.getUpstreamNodeList().size() == 0).collect(Collectors.toList()));
        while (! queue.isEmpty()) {
            DAGNodeProcessor processor = queue.poll();
            for (DAGNodeProcessor downstreamProcessor : processor.getDownstreamNodeList()) {
                int newDegree = inDegreeMap.get(downstreamProcessor) - 1;
                inDegreeMap.put(downstreamProcessor, newDegree);
                if (newDegree == 0) {
                    queue.add(downstreamProcessor);
                }
            }
            orderedProcessorList.add(processor);
        }
        return orderedProcessorList;
    }

    public static GraphCheckInfo checkParamReachableAndResolveProcessorRelation(List<DAGNodeProcessor> processorList, List<String> graphInputParamList, List<String> graphOutputParamList) {
        Set<String> graphInputParamSet = new HashSet<>(graphInputParamList);
        Map<String, List<DAGNodeProcessor>> outputParamProcessorMap = new HashMap<>();
        for (DAGNodeProcessor processor : processorList) {
            for (String param : processor.getOutputParamList()) {
                List<DAGNodeProcessor> paramProcessorList = outputParamProcessorMap.getOrDefault(param, new ArrayList<>());
                paramProcessorList.add(processor);
                outputParamProcessorMap.put(param, paramProcessorList);
            }
        }
        for (DAGNodeProcessor processor : processorList) {
            for (String param : processor.getInputParamList()) {
                if (!graphInputParamSet.contains(param)) {
                    List<DAGNodeProcessor> paramProcessorList = outputParamProcessorMap.get(param);
                    if (CollectionUtils.isEmpty(paramProcessorList)) {
                        // 入参无法获取
                        return new GraphCheckInfo(false, processor, param, null);
                    }
                    for (DAGNodeProcessor paramProcessor : paramProcessorList) {
                        paramProcessor.getDownstreamNodeList().add(processor);
                    }
                }
            }
        }
        for (String param : graphOutputParamList) {
            if (! graphInputParamSet.contains(param) && CollectionUtils.isEmpty(outputParamProcessorMap.get(param))) {
                // 出参无法获取
                return new GraphCheckInfo(false, null, param, null);
            }
        }
        return new GraphCheckInfo(true, null, null, null);
    }

}
