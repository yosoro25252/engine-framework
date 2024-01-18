package com.yosoro25252.engine.framework.pojo;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @author：yosoro25252
 * @date：2024/1/16
 * @desc: 流程上下文，存放流程中所有能用到的数据
 */
@Data
public class Context<T> {

    private T request;

    private Map<String, Object> bizData = new HashMap<>();

    public void putToBizData(String key, Object value) {
        bizData.put(key, value);
    }

    public Object getFromBizData(String key) {
        return bizData.get(key);
    }

}
