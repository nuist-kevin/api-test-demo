package com.focustech.mic.test.api.domain;

public class TestCase {

  private String title;
  private Depend depend;
  private Request request;
  private Response response;

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public Request getRequest() {
    return request;
  }

  public void setRequest(Request request) {
    this.request = request;
  }

  public Response getResponse() {
    return response;
  }

  public void setResponse(Response response) {
    this.response = response;
  }

  public Depend getDepend() {
    return depend;
  }

  public void setDepend(Depend depend) {
    this.depend = depend;
  }

}
