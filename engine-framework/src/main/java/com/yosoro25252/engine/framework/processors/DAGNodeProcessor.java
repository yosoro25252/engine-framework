package com.yosoro25252.engine.framework.processors;


import java.util.ArrayList;
import java.util.List;

public abstract class DAGNodeProcessor implements IProcessor {

    private List<String> inputParamList = new ArrayList<>();

    private List<String> outputParamList = new ArrayList<>();

    private List<DAGNodeProcessor> upstreamNodeList = new ArrayList<>();

    private List<DAGNodeProcessor> downstreamNodeList = new ArrayList<>();

    private String threadPoolTag;

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
}
