package com.yosoro25252.engine.framework.processors;

import com.yosoro25252.engine.framework.flowcontrol.DAGControlService;
import com.yosoro25252.engine.framework.pojo.Context;
import com.yosoro25252.engine.framework.pojo.Graph;
import com.yosoro25252.engine.framework.services.IMonitorService;

import java.util.ArrayList;
import java.util.List;

/**
 * @author：yosoro25252
 * @date：2024/1/16
 * @desc: DAG容器组件。通过这个组件完成建图和图执行。图的具体执行逻辑由其中的DAGNodeProcessor控制
 */
public class DAGContainerProcessor extends FlowProcessor {

    private List<String> inputParamList = new ArrayList<>();

    private List<String> outputParamList = new ArrayList<>();

    private List<DAGNodeProcessor> processorList = new ArrayList<>();

    private DAGControlService controlService;

    private String graphName;

    private String buildGraphStyle;

    private int timeout;

    private Graph graph;

    public DAGContainerProcessor(List<String> inputParamList,
                                 List<String> outputParamList,
                                 List<DAGNodeProcessor> processorList,
                                 DAGControlService controlService,
                                 IMonitorService monitorService,
                                 String graphName,
                                 String buildGraphStyle,
                                 int timeout) {
        this.inputParamList = inputParamList;
        this.outputParamList = outputParamList;
        this.processorList = processorList;
        this.controlService = controlService;
        this.monitorService = monitorService;
        this.graphName = graphName;
        this.buildGraphStyle = buildGraphStyle;
        this.timeout = timeout;
    }

    private void init() {
        this.graph = controlService.buildGraph(processorList, inputParamList, outputParamList, graphName, buildGraphStyle, timeout);
    }

    @Override
    public String getProcessorName() {
        return graphName;
    }

    @Override
    protected void doProcess(Context context) {
        try {
            this.controlService.runGraph(graph, context);
        } catch (Exception e) {
            fallback(context, e);
        }
    }

    /**
     * 拓展接口 - 异常兜底方法
     * 默认抛出异常，中断流程；根据情况需要重写
     * @param context 流程上下文
     * @param e 异常
     */
    protected void fallback(Context context, Exception e) {
        throw new RuntimeException(e);
    }

}
