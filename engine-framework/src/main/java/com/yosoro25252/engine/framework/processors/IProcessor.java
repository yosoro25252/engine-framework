package com.yosoro25252.engine.framework.processors;

import com.yosoro25252.engine.framework.pojo.Context;

public interface IProcessor {

    String getProcessorName();

    default void process(Context context) {
        try {
            doProcess(context);
        } catch (Exception e) {
            fallback(context);
        }
    }

    void doProcess(Context context);

    void fallback(Context context);

}
