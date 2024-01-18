package com.yosoro25252.engine.framework.flowcontrol;

import com.yosoro25252.engine.framework.builder.ContextBuilder;
import com.yosoro25252.engine.framework.builder.ResultBuilder;
import com.yosoro25252.engine.framework.processors.BaseProcessor;
import com.yosoro25252.engine.framework.services.IMonitorService;

import java.util.List;

/**
 * @author：yosoro25252
 * @date：2024/1/19 4:09
 * @desc:
 */
public class FlowContainer<S, T> {

    private List<BaseProcessor> processorList;

    private String serviceName;

    private String serviceNameWithSuccess;

    private String serviceNameWithBizException;

    private String serviceNameWithRuntimeException;

    private ContextBuilder<S> contextBuilder;

    private ResultBuilder<S, T> resultBuilder;

    private IMonitorService monitorService;

    private FlowControlService flowControlService;

    public FlowContainer(List<BaseProcessor> processorList,
                         String serviceName,
                         ContextBuilder<S> contextBuilder,
                         ResultBuilder<S, T> resultBuilder,
                         IMonitorService monitorService,
                         FlowControlService flowControlService) {
        this.processorList = processorList;
        this.serviceName = serviceName;
        this.contextBuilder = contextBuilder;
        this.resultBuilder = resultBuilder;
        this.monitorService = monitorService;
        this.serviceNameWithSuccess = serviceName + "-Success";
        this.serviceNameWithBizException = serviceName + "-BizException";
        this.serviceNameWithRuntimeException = serviceName + "-RuntimeException";
        this.flowControlService = flowControlService;
    }

    public T process(S request) {
        return flowControlService.process(request, processorList, serviceName, serviceNameWithSuccess, serviceNameWithBizException, serviceNameWithRuntimeException, contextBuilder, resultBuilder);
    }

}
