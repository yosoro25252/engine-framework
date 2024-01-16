package com.yosoro25252.engine.framework.builder;

import com.yosoro25252.engine.framework.pojo.Context;

public interface ResultBuilder<T> {

    T buildResult(Context context);

}
