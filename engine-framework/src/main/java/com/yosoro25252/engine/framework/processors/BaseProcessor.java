package com.yosoro25252.engine.framework.processors;

import com.yosoro25252.engine.framework.pojo.Context;
import com.yosoro25252.engine.framework.services.IMonitorService;
import lombok.Setter;

import static com.yosoro25252.engine.framework.constants.MonitorConstants.PROCESSOR_PROCESS;

/**
 * @author：yosoro25252
 * @date：2024/1/18 17:21
 * @desc: 基础processor，所有processor的父类
 */
public abstract class BaseProcessor implements IProcessor {

    @Setter
    protected IMonitorService monitorService;

    /**
     * 组件逻辑执行
     * @param context 流程上下文
     */
    @Override
    public final void process(Context context) {
        long t = System.currentTimeMillis();
        doProcess(context);
        long timeCost = System.currentTimeMillis() - t;
        monitorService.logEvent(PROCESSOR_PROCESS, getProcessorName(), true);
        monitorService.logCost(PROCESSOR_PROCESS, getProcessorName(), timeCost);
    }

    /**
     * 组件正常执行逻辑
     * @param context 流程上下文
     */
    protected abstract void doProcess(Context context);

}
