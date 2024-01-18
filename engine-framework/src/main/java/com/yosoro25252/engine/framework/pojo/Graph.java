package com.yosoro25252.engine.framework.pojo;

import com.yosoro25252.engine.framework.processors.DAGNodeProcessor;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

/**
 * @author：yosoro25252
 * @date：2024/1/16
 * @desc: 图配置信息
 */
@Getter
@AllArgsConstructor
public class Graph {

    private String graphName;

    private int timeout;

    private int size;

    private List<DAGNodeProcessor> nodeList;

    private List<DAGNodeProcessor> orderedNodeList;

}
