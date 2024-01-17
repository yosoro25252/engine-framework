package com.yosoro25252.engine.framework.pojo;

import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class GraphStructureInfo {

    private List<NodeInfo> nodes;

    private List<EdgeInfo> edges;

    @AllArgsConstructor
    public static class NodeInfo {
        private String name;
    }

    @AllArgsConstructor
    public static class EdgeInfo {
        private String source;
        private String target;
    }

}
