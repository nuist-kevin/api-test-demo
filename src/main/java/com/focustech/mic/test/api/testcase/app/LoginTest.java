package com.focustech.mic.test.api.testcase.app;

import com.focustech.mic.test.api.annotation.ExpectData;
import com.focustech.mic.test.api.annotation.PrepareData;
import com.focustech.mic.test.api.annotation.TestFile;

import org.testng.annotations.Test;

public class LoginTest {

  @Test(groups = {"core", "login"})
  @TestFile("testcases/login/loginTest.json")
  @PrepareData(xlsFile = "prepare1.xls",datasource = "cbDataSource")
  @PrepareData(xlsFile = "prepare2.xls",datasource = "cbossDataSource")
  @ExpectData(xlsFile = "expect.xls",datasource = "cbDataSource")
  public void login_success() {}



}
