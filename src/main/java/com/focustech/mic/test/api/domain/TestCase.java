package com.focustech.mic.test.api.domain;

public class TestCase {

  private String title;
  private Depend depend;
  private ApiRequest apiRequest;
  private ApiResponse apiResponse;

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public ApiRequest getApiRequest() {
    return apiRequest;
  }

  public void setApiRequest(ApiRequest apiRequest) {
    this.apiRequest = apiRequest;
  }

  public ApiResponse getApiResponse() {
    return apiResponse;
  }

  public void setApiResponse(ApiResponse apiResponse) {
    this.apiResponse = apiResponse;
  }

  public Depend getDepend() {
    return depend;
  }

  public void setDepend(Depend depend) {
    this.depend = depend;
  }

}
