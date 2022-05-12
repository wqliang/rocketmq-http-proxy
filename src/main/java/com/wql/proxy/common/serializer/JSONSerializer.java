package com.wql.proxy.common.serializer;

import com.alibaba.fastjson.JSON;

public class JSONSerializer {
    public static <T> T parseObject(byte[] data, Class<T> clazz) {
        return JSON.parseObject(data, clazz);
    }

    public static String toJsonString(Object obj) {
        return JSON.toJSONString(obj);
    }

    public static byte[] toJsonBytes(Object obj) {
        String jsonString = toJsonString(obj);
        return jsonString.getBytes();
    }
}
