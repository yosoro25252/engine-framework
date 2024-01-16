package com.yosoro25252.engine.framework.pojo;

import com.yosoro25252.engine.framework.processors.DAGNodeProcessor;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class Graph {

    private String graphName;

    private int timeout;

    private int size;

    private List<DAGNodeProcessor> nodeList;

    private List<DAGNodeProcessor> orderedNodeList;

}
