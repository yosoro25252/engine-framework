package com.yosoro25252.engine.framework.impl;

import com.yosoro25252.engine.framework.pojo.Context;
import com.yosoro25252.engine.framework.processors.DAGNodeProcessor;

public class SimpleDAGNodeProcessorV2 extends DAGNodeProcessor {
    @Override
    public String getProcessorName() {
        return "SimpleDAGNodeProcessorV2";
    }

    @Override
    protected void doProcess(Context context) {
        System.out.println("SimpleDAGNodeProcessorV2.doProcess");
    }

}
