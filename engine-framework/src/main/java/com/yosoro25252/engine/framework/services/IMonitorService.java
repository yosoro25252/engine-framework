package com.yosoro25252.engine.framework.services;

/**
 * @author：yosoro25252
 * @date：2024/1/18 14:56
 * @desc: 通用埋点接口；可根据实际使用埋点工具实现其中方法
 */
public interface IMonitorService {

    void logEvent(String type, String subType, boolean success);

    void logCost(String type, String subType, long cost);

    void logException(String type, Exception e);

}
