package com.focustech.mic.test.api.domain;

import java.util.Map;

public class Response {

  private String statusCode;
  private Map<String, Object> headers;
  private Map<String, Object> cookies;
  private Map<String, Object> body;

  public String getStatusCode() {
    return statusCode;
  }

  public void setStatusCode(String statusCode) {
    this.statusCode = statusCode;
  }

  public Map<String, Object> getHeaders() {
    return headers;
  }

  public void setHeaders(Map<String, Object> headers) {
    this.headers = headers;
  }

  public Map<String, Object> getCookies() {
    return cookies;
  }

  public void setCookies(Map<String, Object> cookies) {
    this.cookies = cookies;
  }

  public Map<String, Object> getBody() {
    return body;
  }

  public void setBody(Map<String, Object> body) {
    this.body = body;
  }
}
