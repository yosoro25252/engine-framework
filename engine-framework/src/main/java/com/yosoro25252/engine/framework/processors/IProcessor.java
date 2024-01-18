package com.yosoro25252.engine.framework.processors;

import com.yosoro25252.engine.framework.pojo.Context;

/**
 * @author：yosoro25252
 * @date：2024/1/16
 * @desc: 组件接口定义
 */
public interface IProcessor {

    /**
     * 获取组件名称
     * @return 组件名称
     */
    String getProcessorName();

    /**
     * 组件逻辑执行
     * @param context 流程上下文
     */
    void process(Context context);

}
