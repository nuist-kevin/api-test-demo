package com.focustech.mic.test.api.utils.json;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonUtil {

  public static <T> List<T> readObjectListFromJsonFile(String filePath, Class<T> tClass) throws IOException {
    InputStream fileInputStream = JsonUtil.class.getClassLoader().getResourceAsStream(filePath);
    ObjectMapper mapper = new ObjectMapper();
    JavaType javaType = mapper.getTypeFactory().constructParametricType(ArrayList.class, tClass);
    List<T> objectList = mapper.readValue(fileInputStream, javaType);
    return objectList;
  }

  public static <K, V> Map<K, V> readMapFromJsonString(String json, Class<K> keyClass, Class<V> valueClass) throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    JavaType javaType = mapper.getTypeFactory().constructMapType(Map.class, keyClass, valueClass);
    Map<K, V> map = mapper.readValue(json, javaType);
    return map;
  }
}
