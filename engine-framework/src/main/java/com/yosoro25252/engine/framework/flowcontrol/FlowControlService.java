package com.yosoro25252.engine.framework.flowcontrol;

import com.yosoro25252.engine.framework.builder.ContextBuilder;
import com.yosoro25252.engine.framework.builder.ResultBuilder;
import com.yosoro25252.engine.framework.exception.BizException;
import com.yosoro25252.engine.framework.pojo.Context;
import com.yosoro25252.engine.framework.processors.BaseProcessor;
import com.yosoro25252.engine.framework.processors.IProcessor;
import com.yosoro25252.engine.framework.services.IMonitorService;

import java.util.List;

import static com.yosoro25252.engine.framework.constants.MonitorConstants.*;

/**
 * @author：yosoro25252
 * @date：2024/1/16
 * @desc: 串行组件调度服务
 * TODO: 添加日志
 */
public class FlowControlService {

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
                            ResultBuilder<S, T> resultBuilder) {
        long t1 = System.currentTimeMillis();
        Context<S> context = null;
        try {
            context = contextBuilder.buildContext(request);
        } catch (RuntimeException e) {
            monitorService.logEvent(SERVICE_WRONG_PARAM, serviceName, false);
            monitorService.logException(SERVICE_WRONG_PARAM, e);
            return resultBuilder.buildResultWhenParamError(request, e);
        }
        try {
            for (IProcessor processor : processorList) {
                processor.process(context);
            }
            T response = resultBuilder.buildResult(context);
            long timeCost = System.currentTimeMillis() - t1;
            monitorService.logEvent(SERVICE_PROCESS, serviceName, true);
            monitorService.logCost(SERVICE_TIME_COST, serviceNameWithSuccess, timeCost);
            monitorService.logCost(SERVICE_TIME_COST, serviceName, timeCost);
            return response;
        } catch (BizException e) {
            T response = resultBuilder.buildResultWhenBizException(context);
            long timeCost = System.currentTimeMillis() - t1;
            monitorService.logEvent(SERVICE_PROCESS, serviceName, false);
            monitorService.logEvent(SERVICE_PROCESS, serviceNameWithBizException, false);
            monitorService.logCost(SERVICE_TIME_COST, serviceNameWithBizException, timeCost);
            monitorService.logCost(SERVICE_TIME_COST, serviceName, timeCost);
            monitorService.logException(SERVICE_PROCESS, e);
            return response;
        } catch (RuntimeException e) {
            T response = resultBuilder.buildResultWhenRuntimeException(context);
            long timeCost = System.currentTimeMillis() - t1;
            monitorService.logEvent(SERVICE_PROCESS, serviceName, false);
            monitorService.logEvent(SERVICE_PROCESS, serviceNameWithRuntimeException, false);
            monitorService.logCost(SERVICE_TIME_COST, serviceNameWithRuntimeException, timeCost);
            monitorService.logCost(SERVICE_TIME_COST, serviceName, timeCost);
            monitorService.logException(SERVICE_PROCESS, e);
            return response;
        }
    }

}
