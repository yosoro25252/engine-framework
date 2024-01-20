package com.example.impl;

import com.yosoro25252.engine.framework.pojo.Context;
import com.yosoro25252.engine.framework.processors.FlowProcessor;

public class SimpleFlowProcessor extends FlowProcessor {

    @Override
    public String getProcessorName() {
        return "SimpleFlowProcessor";
    }

    @Override
    protected void doProcess(Context context) {
        System.out.println("SimpleFlowProcessor.doProcess()");
    }

}
