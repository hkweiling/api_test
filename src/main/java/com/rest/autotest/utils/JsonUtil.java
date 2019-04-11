package com.rest.autotest.utils;

import com.google.gson.Gson;
import com.rest.autotest.model.Device;

import java.lang.reflect.Type;
import java.util.List;

public class JsonUtil {

    private static final Gson GSON = new Gson();

    public static String toJson(Object o) {
        return GSON.toJson(o);
    }

    public static <T> T fromJson(String json, Type type) {
        return GSON.fromJson(json, type);
    }
}
