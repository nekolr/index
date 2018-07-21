package com.nekolr.index.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * JSON 工具类
 *
 * @author nekolr
 */
public class JsonUtils {

    private static final String DEFAULT_CHARSET = "utf-8";

    public static <T> T readObject(String resourceLocation, Class<T> type) throws FileNotFoundException {
        String jsonString = null;
        InputStream in = ResourceUtils.getInputStream(resourceLocation);
        try {
            jsonString = FileUtils.readString(in, DEFAULT_CHARSET);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return JSONObject.parseObject(jsonString, type);
    }

    public static <T> List<T> readArray(String resourceLocation, Class<T> type) throws FileNotFoundException {
        String jsonString = null;
        InputStream in = ResourceUtils.getInputStream(resourceLocation);
        try {
            jsonString = FileUtils.readString(in, DEFAULT_CHARSET);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return JSONArray.parseArray(jsonString, type);
    }
}
