package com.yosoro25252.engine.framework.builder;

import com.yosoro25252.engine.framework.pojo.Context;

/**
 * @author：yosoro25252
 * @date：2024/1/16
 * @desc: 服务返回结果构造器
 */
public interface ResultBuilder<S, T> {

    /**
     * 服务正常执行完成时，构造返回结果
     * @param context context
     * @return 返回结果
     */
    T buildResult(Context<S> context);

    /**
     * 服务入参错误时，构造返回结果
     * @param request 请求
     * @param e 异常
     * @return 返回结果
     */
    T buildResultWhenParamError(S request, Exception e);

    /**
     * 服务执行业务异常时，构造返回结果
     * 业务如果需要针对一些特殊异常case、做特殊逻辑处理，可以通过实现这个方法完成
     * @param context context
     * @return 返回结果
     */
    T buildResultWhenBizException(Context<S> context);

    /**
     * 服务执行其他异常时，构造返回结果
     * 默认的异常处理方式
     * @param context context
     * @return 返回结果
     */
    T buildResultWhenRuntimeException(Context<S> context);

}
