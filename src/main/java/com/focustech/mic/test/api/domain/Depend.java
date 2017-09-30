package com.focustech.mic.test.api.domain;

public class Depend {
  private ApiRequest apiRequest;
  private ApiResponse apiResponse;

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
}
