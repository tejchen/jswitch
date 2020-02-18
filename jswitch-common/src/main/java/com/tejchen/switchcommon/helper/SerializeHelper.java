package com.tejchen.switchcommon.helper;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.tejchen.switchcommon.JSwitchException;

import java.math.BigDecimal;

public class SerializeHelper {

    public static <T> T deserializeJson(String target, Class<T> t) {
        return JSON.parseObject(target, t);
    }

    public static <T> T deserializeJson(String target, TypeReference<T> t) {
        return JSON.parseObject(target, t);
    }

    public static String serializeJson(Object target) {
        return JSON.toJSONString(target);
    }

    public static Object deserialize(Class aClass, String target) {
        // 基础类型
        if (aClass == String.class) {
            return target;
        }
        if (aClass == BigDecimal.class) {
            return new BigDecimal(target);
        }
        if (aClass == Long.class || aClass == long.class) {
            return Long.parseLong(target);
        }
        if (aClass == Integer.class || aClass == int.class) {
            return Integer.parseInt(target);
        }
        if (aClass == Boolean.class || aClass == boolean.class) {
            return Boolean.valueOf(target);
        }
        // 复合类型
        if (isValidJson(target)) {
            return JSON.parseObject(target, aClass);
        }
        throw new JSwitchException(String.format("unSupport type, type:%s, config:%s", aClass, target));
    }

    public static String serialize(Object target) {
        // 基础类型
        if (target == String.class) {
            return String.valueOf(target);
        }
        if (target == BigDecimal.class) {
            return target.toString();
        }
        if (target == Long.class || target == long.class) {
            return String.valueOf(target);
        }
        if (target == Integer.class || target == int.class) {
            return String.valueOf(target);
        }
        if (target == Boolean.class || target == boolean.class) {
            return String.valueOf(target);
        }
        // 复合类型
        return JSON.toJSONString(target);
    }

    private static boolean isValidJson(String str) {
        try {
            JSON.parse(str);
            return true;
        }catch (Exception e){
            try {
                JSON.parseArray(str);
                return true;
            }catch (Exception ex){
                e.printStackTrace();
                ex.printStackTrace();
                return false;
            }
        }
    }
}
