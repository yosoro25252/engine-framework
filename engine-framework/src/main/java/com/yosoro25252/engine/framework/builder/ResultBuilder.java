package com.yosoro25252.engine.framework.builder;

import com.yosoro25252.engine.framework.pojo.Context;

public interface ResultBuilder<S, T> {

    T buildResult(Context<S> context);

    T buildResultWhenError(Context<S> context);

}
