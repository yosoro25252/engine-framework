package com.example.impl;

import com.yosoro25252.engine.framework.pojo.Context;
import com.yosoro25252.engine.framework.processors.FlowProcessor;
import lombok.Setter;

public class SimpleFlowProcessor extends FlowProcessor {

    @Setter
    private String name;

    @Override
    public String getProcessorName() {
        return name;
    }

    @Override
    protected void doProcess(Context context) {
        System.out.println(getProcessorName() + ".doProcess()");
    }

}
