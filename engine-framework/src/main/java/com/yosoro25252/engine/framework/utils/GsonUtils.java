package com.yosoro25252.engine.framework.utils;

import com.google.gson.Gson;

public class GsonUtils {

    private static final Gson gson = new Gson();

    public static String getJsonStringFromObject(Object object) {
        return gson.toJson(object);
    }

    public static <T> T getObjectFromJson(String json, Class<T> clazz) {
        return gson.fromJson(json, clazz);
    }

}
