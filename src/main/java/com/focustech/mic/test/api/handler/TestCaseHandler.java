package com.focustech.mic.test.api.handler;

import com.focustech.mic.test.api.domain.TestCase;

public interface TestCaseHandler {

  void handle(TestCase testCase);

  void setNextHandler(TestCaseHandler nextHandler);

}
