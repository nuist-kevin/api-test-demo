package com.focustech.mic.test.api.domain;

import java.util.HashMap;

public class Depend {

  private String url;
  private String method;
  private HashMap<String, String> params;
  private HashMap<String, String> headers;
  private HashMap<String, String> cookies;
  private String file;

  public String getUrl() {
    return url;
  }

  public String getMethod() {
    return method;
  }

  public void setMethod(String method) {
    this.method = method;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public HashMap<String, String> getParams() {
    return params;
  }

  public void setParams(HashMap<String, String> params) {
    this.params = params;
  }

  public HashMap<String, String> getHeaders() {
    return headers;
  }

  public void setHeaders(HashMap<String, String> headers) {
    this.headers = headers;
  }

  public HashMap<String, String> getCookies() {
    return cookies;
  }

  public void setCookies(HashMap<String, String> cookies) {
    this.cookies = cookies;
  }

  public String getFile() {
    return file;
  }

  public void setFile(String file) {
    this.file = file;
  }
}
