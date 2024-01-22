package com.example.impl;

import com.yosoro25252.engine.framework.builder.ContextBuilder;
import com.yosoro25252.engine.framework.pojo.Context;

/**
 * @author：yosoro25252
 * @date：2024/1/23 2:24
 * @desc:
 */
public class SimpleContextBuilder implements ContextBuilder<SimpleRequest> {

    @Override
    public Context<SimpleRequest> buildContext(SimpleRequest request) {
        Context<SimpleRequest> context = new Context<>();
        context.setRequest(request);
        context.putToBizData("uid", request.getUid());
        context.putToBizData("uuid", request.getUuid());
        context.putToBizData("strategy", request.getStrategy());
        return context;
    }

}
