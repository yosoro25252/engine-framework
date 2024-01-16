package com.yosoro25252.engine.framework.processors;

import com.yosoro25252.engine.framework.flowcontrol.DAGControlService;
import com.yosoro25252.engine.framework.pojo.Context;
import com.yosoro25252.engine.framework.pojo.Graph;

import javax.annotation.PostConstruct;
import java.util.List;

public class DAGContainerProcessor implements IProcessor {

    private List<DAGNodeProcessor> processorList;

    private DAGControlService controlService;

    private String graphName;

    private int timeout;

    private Graph graph;

    public DAGContainerProcessor(List<DAGNodeProcessor> processorList, DAGControlService controlService, String graphName, int timeout) {
        this.processorList = processorList;
        this.controlService = controlService;
        this.graphName = graphName;
        this.timeout = timeout;
    }

    @PostConstruct
    private void init() {
        this.graph = controlService.buildGraph(processorList, graphName, "", timeout);
    }

    @Override
    public String getProcessorName() {
        return graphName;
    }

    @Override
    public void doProcess(Context context) {

    }

    @Override
    public void fallback(Context context) {

    }


}
