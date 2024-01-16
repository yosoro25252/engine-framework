package com.yosoro25252.engine.framework.pojo;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class Context<T> {

    private T request;

    private Exception e;

    private Map<String, Object> bizData = new HashMap<>();

    public void putToBizData(String key, Object value) {
        bizData.put(key, value);
    }

    public Object getFromBizData(String key) {
        return bizData.get(key);
    }

}
