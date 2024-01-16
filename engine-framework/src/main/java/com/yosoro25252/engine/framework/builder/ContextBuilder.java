package com.yosoro25252.engine.framework.builder;

import com.yosoro25252.engine.framework.pojo.Context;

import java.util.Map;

public interface ContextBuilder<T> {

    Context buildContext(T request);

}
