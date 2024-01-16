package com.yosoro25252.engine.framework.processors;

import com.yosoro25252.engine.framework.enums.BuildGraphStyleEnum;
import com.yosoro25252.engine.framework.flowcontrol.DAGControlService;
import com.yosoro25252.engine.framework.pojo.Context;
import com.yosoro25252.engine.framework.pojo.Graph;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class DAGContainerProcessor implements IProcessor {

    private List<String> inputParamList = new ArrayList<>();

    private List<String> outputParamList = new ArrayList<>();

    private List<DAGNodeProcessor> processorList = new ArrayList<>();

    private DAGControlService controlService;

    private String graphName;

    private String buildGraphStyle = BuildGraphStyleEnum.FROM_NEIGHBOURS.getName();

    private int timeout;

    private Graph graph;

    public DAGContainerProcessor(List<String> inputParamList,
                                 List<String> outputParamList,
                                 List<DAGNodeProcessor> processorList,
                                 DAGControlService controlService,
                                 String graphName,
                                 int timeout) {
        this.inputParamList = inputParamList;
        this.outputParamList = outputParamList;
        this.processorList = processorList;
        this.controlService = controlService;
        this.graphName = graphName;
        this.timeout = timeout;
    }

    @PostConstruct
    private void init() {
        this.graph = controlService.buildGraph(processorList, inputParamList, outputParamList, graphName, buildGraphStyle, timeout);
    }

    @Override
    public String getProcessorName() {
        return graphName;
    }

    @Override
    public void doProcess(Context context) {
        this.controlService.runGraph(graph, context);
    }

    @Override
    public void fallback(Context context) {
    }


}
