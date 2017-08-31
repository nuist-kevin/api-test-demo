package com.focustech.mic.test.api.testcase.app;

import com.focustech.mic.test.api.annotation.DataFile;
import org.testng.annotations.Test;

/**
 * Created by caiwen on 2017/8/1.
 */
public class RegisterTest {

  @Test(groups = {"core"})
  @DataFile("testcases/register/register_success.json")
  public void register_success() {

  }
}
