package com.focustech.mic.test.api.domain;

import java.util.HashMap;

public class Request {

  private HashMap<String, String> params;
  private HashMap<String, String> headers;
  private HashMap<String, String> cookies;
  private String file;

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
