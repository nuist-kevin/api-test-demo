package com.focustech.mic.test.api.utils.json;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JsonUtil {

  private static ObjectMapper objectMapper = new ObjectMapper();

  public static <T> List<T> readObjectListFromJsonFile(String filePath, Class<T> tClass)
      throws IOException {
    InputStream fileInputStream = JsonUtil.class.getClassLoader().getResourceAsStream(filePath);
    JavaType javaType = objectMapper.getTypeFactory().constructCollectionType(ArrayList.class, tClass);
    List<T> objectList = objectMapper.readValue(fileInputStream, javaType);
    return objectList;
  }

  public static <K, V> Map<K, V> readMapFromJsonString(String json, Class<K> keyClass,
      Class<V> valueClass) throws IOException {
    JavaType javaType = objectMapper.getTypeFactory().constructMapType(Map.class, keyClass, valueClass);
    Map<K, V> map = objectMapper.readValue(json, javaType);
    return map;
  }
}
