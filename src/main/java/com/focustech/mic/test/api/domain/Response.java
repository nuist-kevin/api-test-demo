package com.focustech.mic.test.api.domain;

import java.util.HashMap;

public class Response {

  private Integer statusCode;
  private HashMap<String, Object> headers;
  private HashMap<String, Object> cookies;
  private HashMap<String, String> body;

  public Integer getStatusCode() {
    return statusCode;
  }

  public void setStatusCode(Integer statusCode) {
    this.statusCode = statusCode;
  }

  public HashMap<String, Object> getHeaders() {
    return headers;
  }

  public void setHeaders(HashMap<String, Object> headers) {
    this.headers = headers;
  }

  public HashMap<String, Object> getCookies() {
    return cookies;
  }

  public void setCookies(HashMap<String, Object> cookies) {
    this.cookies = cookies;
  }

  public HashMap<String, String> getBody() {
    return body;
  }

  public void setBody(HashMap<String, String> body) {
    this.body = body;
  }
}
