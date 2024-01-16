package com.yosoro25252.engine.framework.flowcontrol;

import com.yosoro25252.engine.framework.builder.ContextBuilder;
import com.yosoro25252.engine.framework.builder.ResultBuilder;
import com.yosoro25252.engine.framework.pojo.Context;
import com.yosoro25252.engine.framework.processors.IProcessor;

import java.util.List;

public class FlowControlService<S, T> {

    private List<IProcessor> processorList;

    private String serviceName;

    private ContextBuilder<S> contextBuilder;

    private ResultBuilder<T> resultBuilder;

    public FlowControlService(List<IProcessor> processorList, String serviceName, ContextBuilder<S> contextBuilder, ResultBuilder<T> resultBuilder) {
        this.processorList = processorList;
        this.serviceName = serviceName;
        this.contextBuilder = contextBuilder;
        this.resultBuilder = resultBuilder;
    }

    public T process(S request) {
        Context context = contextBuilder.buildContext(request);
        for (IProcessor processor : processorList) {
            processor.process(context);
        }
        return resultBuilder.buildResult(context);
    }

}
