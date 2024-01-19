package com.yosoro25252.engine.framework.impl;

import com.yosoro25252.engine.framework.pojo.Context;
import com.yosoro25252.engine.framework.processors.DAGNodeProcessor;
import lombok.Setter;

public class SimpleDAGNodeProcessorV1 extends DAGNodeProcessor {

    @Setter
    private String processorName;

    @Override
    public String getProcessorName() {
        return processorName;
    }

    @Override
    protected void doProcess(Context context) {
        System.out.println(getProcessorName() + ".doProcess");
    }

}
