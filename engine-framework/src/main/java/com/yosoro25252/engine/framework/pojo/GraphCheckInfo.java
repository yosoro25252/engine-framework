package com.yosoro25252.engine.framework.pojo;

import com.yosoro25252.engine.framework.processors.DAGNodeProcessor;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author：yosoro25252
 * @date：2024/1/16
 * @desc: 图构建时检查结果
 */
@Data
@AllArgsConstructor
public class GraphCheckInfo {

    private boolean legal;

    private DAGNodeProcessor errorNode;

    private String errorParam;

    private Exception e;

}
