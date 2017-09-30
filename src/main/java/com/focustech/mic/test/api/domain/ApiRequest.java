package com.focustech.mic.test.api.domain;

import java.util.Map;

public class ApiRequest {

  private String path;
  private String method;
  private Map<String, String> params;
  private Map<String, String> headers;
  private Map<String, String> cookies;
  private String file;

  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }

  public String getMethod() {
    return method;
  }

  public void setMethod(String method) {
    this.method = method;
  }

  public Map<String, String> getParams() {
    return params;
  }

  public void setParams(Map<String, String> params) {
    this.params = params;
  }

  public Map<String, String> getHeaders() {
    return headers;
  }

  public void setHeaders(Map<String, String> headers) {
    this.headers = headers;
  }

  public Map<String, String> getCookies() {
    return cookies;
  }

  public void setCookies(Map<String, String> cookies) {
    this.cookies = cookies;
  }

  public String getFile() {
    return file;
  }

  public void setFile(String file) {
    this.file = file;
  }
}
