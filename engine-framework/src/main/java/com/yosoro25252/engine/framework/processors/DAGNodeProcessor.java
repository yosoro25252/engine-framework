package com.yosoro25252.engine.framework.processors;


import com.yosoro25252.engine.framework.pojo.Context;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

import static com.yosoro25252.engine.framework.constants.MonitorConstants.*;

/**
 * @author：yosoro25252
 * @date：2024/1/16
 * @desc: DAG结点组件
 */

@Slf4j
public abstract class DAGNodeProcessor extends BaseProcessor {

    private List<String> inputParamList = new ArrayList<>();

    private List<String> outputParamList = new ArrayList<>();

    private List<DAGNodeProcessor> upstreamNodeList = new ArrayList<>();

    private List<DAGNodeProcessor> downstreamNodeList = new ArrayList<>();

    private String threadPoolTag;

    private String processorNameWithSuccess = getProcessorName() + "-Success";

    private String processorNameWithException = getProcessorName() + "-Exception";

    public void setUpstreamNodeList(List<DAGNodeProcessor> upstreamNodeList) {
        this.upstreamNodeList = upstreamNodeList;
    }

    public List<DAGNodeProcessor> getUpstreamNodeList() {
        return upstreamNodeList;
    }

    public void setDownstreamNodeList(List<DAGNodeProcessor> downstreamNodeList) {
        this.downstreamNodeList = downstreamNodeList;
    }

    public List<DAGNodeProcessor> getDownstreamNodeList() {
        return downstreamNodeList;
    }

    public void setThreadPoolTag(String threadPoolTag) {
        this.threadPoolTag = threadPoolTag;
    }

    public String getThreadPoolTag() {
        return threadPoolTag;
    }

    public void setInputParamList(List<String> inputParamList) {
        this.inputParamList = inputParamList;
    }

    public List<String> getInputParamList() {
        return inputParamList;
    }

    public void setOutputParamList(List<String> outputParamList) {
        this.outputParamList = outputParamList;
    }

    public List<String> getOutputParamList() {
        return outputParamList;
    }

    @Override
    public void process(Context context) {
        long t = System.currentTimeMillis();
        try {
            doProcess(context);
            long timeCost = System.currentTimeMillis() - t;
            monitorService.logEvent(DAG_PROCESSOR_PROCESS, getProcessorName(), true);
            monitorService.logCost(DAG_PROCESSOR_PROCESS, processorNameWithSuccess, timeCost);
        } catch (Exception e) {
            long timeCost = System.currentTimeMillis() - t;
            monitorService.logEvent(DAG_PROCESSOR_PROCESS, getProcessorName(), false);
            monitorService.logCost(DAG_PROCESSOR_PROCESS, processorNameWithException, timeCost);
            monitorService.logException(DAG_PROCESSOR_PROCESS_ERROR, e);
            log.error("DAG结点失败: context = {}, processorName = {}, e = ", context, getProcessorName(), e);
            fallback(context, e);
        }
    }

    protected abstract void doProcess(Context context);

    protected void fallback(Context context, Exception e) {
        throw new RuntimeException(e);
    }

}
