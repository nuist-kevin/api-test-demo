package com.focustech.mic.test.api.testcase.channelfy;

import com.focustech.mic.test.api.annotation.TestFile;
import org.testng.annotations.Test;

public class DemoTest {

  @Test
  @TestFile("testcases/channelfy/demoTest.json")
  public void testReg() {}

//  @Test
//  public void test302() {
//
//    baseURI = "http://passport.channel-fy.com";
//    basePath = "/logon";
//    given().
//        param("logonUserName", "caiwen@devtest.com").
//        param("logonPassword", "focus1234")
//        .when().log().all()
//        .redirects().follow(true).redirects().max(5).
//
//        post()
//        .then().log().all();
//  }
}
