package com.focustech.mic.test.api.listener;

import io.restassured.RestAssured;
import io.restassured.config.ConnectionConfig;
import io.restassured.config.EncoderConfig;
import io.restassured.config.MatcherConfig;
import io.restassured.config.RedirectConfig;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;

public class CustomTestListener extends TestListenerAdapter {

  @Override
  public void onStart(ITestContext context) {
    super.onStart(context);
    RestAssured.baseURI = context.getCurrentXmlTest().getParameter("baseURI");
    RestAssured.config().encoderConfig(EncoderConfig.encoderConfig().defaultContentCharset(
        context.getCurrentXmlTest().getParameter("charset")));
  }

}
