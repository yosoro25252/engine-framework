package com.yosoro25252.engine.framework.utils;

import com.yosoro25252.engine.framework.enums.ColorEnum;
import com.yosoro25252.engine.framework.pojo.GraphCheckInfo;
import com.yosoro25252.engine.framework.processors.DAGNodeProcessor;

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
        return new GraphCheckInfo(true, null, null);
    }

    private static GraphCheckInfo doCheckCycle(DAGNodeProcessor processor, Map<DAGNodeProcessor, Integer> visitedStatusMap) {
        visitedStatusMap.put(processor, ColorEnum.GRAY.getCode());
        for (DAGNodeProcessor downstreamProcessor : processor.getDownstreamNodeList()) {
            int visitedStatus = visitedStatusMap.getOrDefault(downstreamProcessor, ColorEnum.WHITE.getCode());
            if (visitedStatus == ColorEnum.GRAY.getCode()) {
                // 已成环
                return new GraphCheckInfo(false, processor, null);
            }
            GraphCheckInfo downstreamCheckResult = doCheckCycle(downstreamProcessor, visitedStatusMap);
            if (! downstreamCheckResult.isLegal()) {
                // 已成环
                return downstreamCheckResult;
            }
        }
        visitedStatusMap.put(processor, ColorEnum.BLACK.getCode());
        return new GraphCheckInfo(true, null, null);
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

    public static List<DAGNodeProcessor> findFirstProcessor(List<DAGNodeProcessor> processorList) {
        Map<DAGNodeProcessor, Integer> inDegreeMap = new HashMap<>();
        for (DAGNodeProcessor processor : processorList) {
            for (DAGNodeProcessor downstreamProcessor : processor.getDownstreamNodeList()) {
                inDegreeMap.put(downstreamProcessor, inDegreeMap.getOrDefault(downstreamProcessor, 0) + 1);
            }
            inDegreeMap.putIfAbsent(processor, 0);
        }
        return processorList.stream().filter(processor -> inDegreeMap.get(processor) == 0).collect(Collectors.toList());
    }

}
