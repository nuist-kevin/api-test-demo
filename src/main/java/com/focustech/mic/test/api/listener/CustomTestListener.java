package com.focustech.mic.test.api.listener;

import static io.restassured.RestAssured.given;

import io.restassured.RestAssured;
import io.restassured.config.EncoderConfig;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestContext;
import org.testng.TestListenerAdapter;

public class CustomTestListener extends TestListenerAdapter {

  private final Logger logger = LoggerFactory.getLogger(CustomTestListener.class);

  @Override
  public void onStart(ITestContext context) {
    super.onStart(context);
    logger.debug("start config RestAssured for test 【{}】", context.getCurrentXmlTest().getName());
    RestAssured.baseURI = context.getCurrentXmlTest().getParameter("baseURI");

    String charset = context.getCurrentXmlTest().getParameter("charset");
    RestAssured.config().
        encoderConfig(EncoderConfig.encoderConfig().defaultContentCharset(
            charset == null ? "utf-8" : charset));

    Boolean needLogin = false;
    if (context.getCurrentXmlTest().getParameter("needLogin") != null) {
      needLogin = Boolean.parseBoolean(context.getCurrentXmlTest().getParameter("needLogin"));
    }
    if (needLogin) {
      String loginUrl = context.getCurrentXmlTest().getParameter("loginUrl");

      String[] loginParamsArray = context.getCurrentXmlTest().getParameter("loginParams")
          .split(",");
      String[] loginParamsValueArray = context.getCurrentXmlTest().getParameter("loginParamsValue")
          .split(",");
      String[] loginCookiesArray = context.getCurrentXmlTest().getParameter("loginCookies")
          .split(",");

      Map<String, String> loginParams = new HashMap<>();
      for (int i = 0; i < loginParamsArray.length; i++) {
        loginParams.put(loginParamsArray[i].trim(), loginParamsValueArray[i].trim());
      }
      Map<String, String> loginCookies = new HashMap<>();
      Arrays.stream(loginCookiesArray).forEach(key -> loginCookies.put(key.trim(), null));

      given().log().all()
          .formParams(loginParams)
          .post(loginUrl)
          .then().log().all()
          .extract().cookies().forEach(
          (key, value) -> {
            if (loginCookies.containsKey(key)) {
              loginCookies.put(key, value);
            }
          }
      );
      context.setAttribute("loginCookies", loginCookies);
    }
  }

  @Override
  public void onFinish(ITestContext testContext) {
    super.onFinish(testContext);
    logger.debug("Clearing test context");
    if (testContext.getAttributeNames().contains("loginCookies")) {
      testContext.removeAttribute("loginCookies");
    }
  }
}
