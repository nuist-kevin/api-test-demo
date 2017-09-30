package com.focustech.mic.test.api.handler;

import com.focustech.mic.test.api.domain.TestCase;

public class DependHandler implements TestCaseHandler {

  @Override
  public void handle(TestCase testCase) {

    testCase.getDepend().setApiResponse(null);
    testCase.toJson

  }

  @Override
  public void setNextHandler(TestCaseHandler nextHandler) {

  }
}
