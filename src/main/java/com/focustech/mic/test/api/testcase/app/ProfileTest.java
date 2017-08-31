package com.focustech.mic.test.api.testcase.app;

import com.focustech.mic.test.api.annotation.DataFile;
import io.restassured.RestAssured;
import org.testng.ITestContext;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Created by caiwen on 2017/8/1.
 */
public class ProfileTest {

  @BeforeMethod(alwaysRun = true)
  public void getSessionId(ITestContext context) {
    String sessionId = RestAssured.given().basePath("/ssl/mic/login").param("loginId", "lhpb").
        param("password", "focus1234").
        param("region", "MIC for Supplier Android").
        post()
        .getSessionId();
    context.setAttribute("sessionId", sessionId);

  }

  @Test(groups = {"core"})
  @DataFile("/testcases/profile/get_success.json")
  public void get_success() {

  }
}
