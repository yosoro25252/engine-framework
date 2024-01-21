package com.yosoro25252.engine.framework.processors;

import com.yosoro25252.engine.framework.pojo.Context;

import static com.yosoro25252.engine.framework.constants.MonitorConstants.FLOW_PROCESSOR_PROCESS;

public abstract class FlowProcessor extends BaseProcessor {

    /**
     * 组件逻辑执行
     * @param context 流程上下文
     */
    @Override
    public final void process(Context context) {
        long t = System.currentTimeMillis();
        doProcess(context);
        long timeCost = System.currentTimeMillis() - t;
        monitorService.logEvent(FLOW_PROCESSOR_PROCESS, getProcessorName(), true);
        monitorService.logCost(FLOW_PROCESSOR_PROCESS, getProcessorName(), timeCost);
    }

    /**
     * 组件正常执行逻辑
     * @param context 流程上下文
     */
    protected abstract void doProcess(Context context);

}
