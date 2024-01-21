package com.yosoro25252.engine.framework.flowcontrol;

import com.yosoro25252.engine.framework.builder.ContextBuilder;
import com.yosoro25252.engine.framework.builder.ResponseBuilder;
import com.yosoro25252.engine.framework.processors.BaseProcessor;
import com.yosoro25252.engine.framework.services.IMonitorService;

import java.util.List;

/**
 * @author：yosoro25252
 * @date：2024/1/19 4:09
 * @desc: 串行流程容器
 */
public class FlowContainer<S, T> {

    private List<BaseProcessor> processorList;

    private String serviceName;

    private String serviceNameWithSuccess;

    private String serviceNameWithBizException;

    private String serviceNameWithRuntimeException;

    private ContextBuilder<S> contextBuilder;

    private ResponseBuilder<S, T> responseBuilder;

    private IMonitorService monitorService;

    private FlowControlService flowControlService;

    public FlowContainer(List<BaseProcessor> processorList,
                         String serviceName,
                         ContextBuilder<S> contextBuilder,
                         ResponseBuilder<S, T> responseBuilder,
                         IMonitorService monitorService,
                         FlowControlService flowControlService) {
        this.processorList = processorList;
        this.serviceName = serviceName;
        this.contextBuilder = contextBuilder;
        this.responseBuilder = responseBuilder;
        this.monitorService = monitorService;
        this.serviceNameWithSuccess = serviceName + "-Success";
        this.serviceNameWithBizException = serviceName + "-BizException";
        this.serviceNameWithRuntimeException = serviceName + "-RuntimeException";
        this.flowControlService = flowControlService;
    }

    public T process(S request) {
        return flowControlService.process(request, processorList, serviceName, serviceNameWithSuccess, serviceNameWithBizException, serviceNameWithRuntimeException, contextBuilder, responseBuilder);
    }

}
