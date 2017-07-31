package com.focustech.mic.test.api.testcase.app;

import com.focustech.mic.test.api.annotation.DataFile;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class LoginTest {

  @Test(groups = {"core"})
  @DataFile("testcases/login/loginTest.json")
  public void login_success() {}

  @Test(groups = {"core"})
  @DataFile("testcases/login/loginTest.json")
  public void testRegister() {}

}
