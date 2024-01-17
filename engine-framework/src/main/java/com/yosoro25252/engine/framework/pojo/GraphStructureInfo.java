package com.yosoro25252.engine.framework.pojo;

import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class GraphStructureInfo {

    private List<NodeInfo> nodes;

    private List<EdgeInfo> edges;

    private List<String> graphInputParams;

    private List<String> graphOutputParams;

    @AllArgsConstructor
    public static class NodeInfo {
        private String name;
        private List<String> inputParams;
        private List<String> outputParams;
    }

    @AllArgsConstructor
    public static class EdgeInfo {
        private String source;
        private String target;
    }

}
