package com.yosoro25252.engine.framework.builder;

import com.yosoro25252.engine.framework.pojo.Context;

/**
 * @author：yosoro25252
 * @date：2024/1/16
 * @desc: 流程上下文构造器
 */
public interface ContextBuilder<T> {

    /**
     * 校验请求入参，并生产流程上下文
     * @param request 原始请求
     * @return 流程上下文
     */
    Context<T> buildContext(T request);

}
