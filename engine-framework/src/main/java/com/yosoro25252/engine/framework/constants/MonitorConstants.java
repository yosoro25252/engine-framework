package com.yosoro25252.engine.framework.constants;

/**
 * @author：yosoro25252
 * @date：2024/1/18 15:13
 * @desc: 埋点常量
 */
public class MonitorConstants {

    /**
     * 耗时埋点 - 服务执行总耗时
     */
    public static String SERVICE_TIME_COST = "ServiceTimeCost";

    /**
     * 事件埋点 - 服务入参错误次数
     */
    public static String SERVICE_WRONG_PARAM = "ServiceWrongParam";

    /**
     * 事件埋点 - 服务执行成功率
     */
    public static String SERVICE_PROCESS = "ServiceProcess";

    /**
     * 事件埋点 - 结点执行成功率
     */
    public static String PROCESSOR_PROCESS = "ProcessorProcess";

    /**
     * 事件埋点 - 结点执行特殊异常次数
     */
    public static String PROCESSOR_PROCESS_BIZ_EXCEPTION = "ProcessorProcessBizException";

    /**
     * 事件埋点 - 结点执行异常次数
     */
    public static String PROCESSOR_PROCESS_RUNTIME_EXCEPTION = "ProcessorProcessRuntimeException";

}
