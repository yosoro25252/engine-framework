package com.yosoro25252.engine.framework.processors;

import com.yosoro25252.engine.framework.pojo.Context;

import java.util.ArrayList;
import java.util.List;

public abstract class DAGNodeProcessor implements IProcessor {

    private List<DAGNodeProcessor> upstreamNodeList = new ArrayList<>();

    private List<DAGNodeProcessor> downstreamNodeList = new ArrayList<>();

    private String threadPoolTag;

    @Override
    public String getProcessorName() {
        return null;
    }

    public List<DAGNodeProcessor> getUpstreamNodeList() {
        return upstreamNodeList;
    }

    public List<DAGNodeProcessor> getDownstreamNodeList() {
        return downstreamNodeList;
    }

    public String getThreadPoolTag() {
        return threadPoolTag;
    }

}
