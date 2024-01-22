package com.example.impl;

import com.yosoro25252.engine.framework.builder.ResponseBuilder;
import com.yosoro25252.engine.framework.pojo.Context;

/**
 * @author：yosoro25252
 * @date：2024/1/23 2:24
 * @desc:
 */
public class SimpleResponseBuilder implements ResponseBuilder<SimpleRequest, SimpleResponse> {

    @Override
    public SimpleResponse buildResult(Context<SimpleRequest> context) {
        SimpleResponse response = new SimpleResponse();
        response.setCode(0);
        response.setMessage("成功");
        response.setScore(3.0F);
        return response;
    }

    @Override
    public SimpleResponse buildResultWhenParamError(SimpleRequest request, Exception e) {
        SimpleResponse response = new SimpleResponse();
        response.setCode(1);
        response.setMessage("参数错误");
        return response;
    }

    @Override
    public SimpleResponse buildResultWhenBizException(Context<SimpleRequest> context) {
        SimpleResponse response = new SimpleResponse();
        response.setCode(2);
        response.setMessage("用户未注册");
        return response;
    }

    @Override
    public SimpleResponse buildResultWhenRuntimeException(Context<SimpleRequest> context) {
        SimpleResponse response = new SimpleResponse();
        response.setCode(2);
        response.setMessage("系统错误");
        return response;
    }

}
