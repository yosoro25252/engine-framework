package com.yosoro25252.engine.framework.pojo;

import com.yosoro25252.engine.framework.processors.DAGNodeProcessor;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GraphCheckInfo {

    private boolean legal;

    private DAGNodeProcessor errorNode;

    private String errorParam;

    private Exception e;

}
