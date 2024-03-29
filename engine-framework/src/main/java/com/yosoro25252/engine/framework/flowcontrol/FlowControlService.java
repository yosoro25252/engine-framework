package com.yosoro25252.engine.framework.flowcontrol;

import com.yosoro25252.engine.framework.builder.ContextBuilder;
import com.yosoro25252.engine.framework.builder.ResponseBuilder;
import com.yosoro25252.engine.framework.exception.BizException;
import com.yosoro25252.engine.framework.pojo.Context;
import com.yosoro25252.engine.framework.processors.BaseProcessor;
import com.yosoro25252.engine.framework.processors.IProcessor;
import com.yosoro25252.engine.framework.services.IMonitorService;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static com.yosoro25252.engine.framework.constants.MonitorConstants.*;

/**
 * @author：yosoro25252
 * @date：2024/1/16
 * @desc: 串行组件调度服务
 */

@Slf4j
public class FlowControlService {

    private static final String RESPONSE_BUILDER = "ResponseBuilder";

    private IMonitorService monitorService;

    public FlowControlService(IMonitorService monitorService) {
        this.monitorService = monitorService;
    }

    public <S, T> T process(S request,
                            List<BaseProcessor> processorList,
                            String serviceName,
                            String serviceNameWithSuccess,
                            String serviceNameWithBizException,
                            String serviceNameWithRuntimeException,
                            ContextBuilder<S> contextBuilder,
                            ResponseBuilder<S, T> responseBuilder) {
        long t1 = System.currentTimeMillis();
        Context<S> context = null;
        try {
            context = contextBuilder.buildContext(request);
        } catch (RuntimeException e) {
            monitorService.logEvent(SERVICE_WRONG_PARAM, serviceName, false);
            monitorService.logException(SERVICE_WRONG_PARAM, e);
            log.warn("请求失败 - 参数错误: request = {}, serviceName = {}, e = ", request, serviceName, e);
            return responseBuilder.buildResultWhenParamError(request, e);
        }
        IProcessor currProcessor = null;
        try {
            for (IProcessor processor : processorList) {
                currProcessor = processor;
                currProcessor.process(context);
            }
            T response = responseBuilder.buildResult(context);
            long timeCost = System.currentTimeMillis() - t1;
            monitorService.logEvent(SERVICE_PROCESS, serviceName, true);
            monitorService.logCost(SERVICE_TIME_COST, serviceNameWithSuccess, timeCost);
            monitorService.logCost(SERVICE_TIME_COST, serviceName, timeCost);
            return response;
        } catch (BizException e) {
            T response = responseBuilder.buildResultWhenBizException(context);
            long timeCost = System.currentTimeMillis() - t1;
            String errorProcessorName = currProcessor != null ? currProcessor.getProcessorName() : RESPONSE_BUILDER;
            monitorService.logEvent(FLOW_PROCESSOR_PROCESS_BIZ_EXCEPTION, errorProcessorName, false);
            monitorService.logEvent(FLOW_PROCESSOR_PROCESS, errorProcessorName, false);
            monitorService.logEvent(SERVICE_PROCESS, serviceName, false);
            monitorService.logEvent(SERVICE_PROCESS, serviceNameWithBizException, false);
            monitorService.logCost(SERVICE_TIME_COST, serviceNameWithBizException, timeCost);
            monitorService.logCost(SERVICE_TIME_COST, serviceName, timeCost);
            monitorService.logException(SERVICE_PROCESS, e);
            log.error("请求失败 - 业务错误: context = {}, serviceName = {}, processorName = {}, e = ", context, serviceName, errorProcessorName, e);
            return response;
        } catch (RuntimeException e) {
            T response = responseBuilder.buildResultWhenRuntimeException(context);
            long timeCost = System.currentTimeMillis() - t1;
            String errorProcessorName = currProcessor != null ? currProcessor.getProcessorName() : RESPONSE_BUILDER;
            monitorService.logEvent(FLOW_PROCESSOR_PROCESS_RUNTIME_EXCEPTION, errorProcessorName, false);
            monitorService.logEvent(FLOW_PROCESSOR_PROCESS, errorProcessorName, false);
            monitorService.logEvent(SERVICE_PROCESS, serviceName, false);
            monitorService.logEvent(SERVICE_PROCESS, serviceNameWithRuntimeException, false);
            monitorService.logCost(SERVICE_TIME_COST, serviceNameWithRuntimeException, timeCost);
            monitorService.logCost(SERVICE_TIME_COST, serviceName, timeCost);
            monitorService.logException(SERVICE_PROCESS, e);
            log.error("请求失败 - 执行错误: context = {}, serviceName = {}, processorName = {}, e = ", context, serviceName, errorProcessorName, e);
            return response;
        }
    }

}
