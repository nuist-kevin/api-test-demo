package com.focustech.mic.test.api.listener;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.focustech.mic.test.api.annotation.DataFile;
import com.focustech.mic.test.api.domain.Request;
import com.focustech.mic.test.api.domain.Response;
import com.focustech.mic.test.api.domain.TestCase;
import com.focustech.mic.test.api.utils.json.JsonUtil;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.testng.Assert;
import org.testng.IHookCallBack;
import org.testng.IHookable;
import org.testng.ITestResult;

public class CustomHooker implements IHookable {

  @Override
  public void run(IHookCallBack callBack, ITestResult testResult) {
    System.out.println("---------- hooker ----------");
    String filePath = testResult.getMethod().getConstructorOrMethod().getMethod()
        .getDeclaredAnnotation(DataFile.class).value();
    System.out.println(filePath);

    List<TestCase> testCaseList = new ArrayList<>();
    try {
      testCaseList = JsonUtil.readObjectListFromJsonFile(filePath, TestCase.class);
    } catch (IOException e) {
      e.printStackTrace();
      Assert.fail("Json文件解析失败");
    }

    for (int i = 0; i < testCaseList.size(); i++) {
      System.out
          .println("============ testing " + testCaseList.get(i).getTitle() + " ==============");


      Request request = testCaseList.get(i).getRequest();
      Response response = testCaseList.get(i).getResponse();

      RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder();
      ResponseSpecBuilder responseSpecBuilder = new ResponseSpecBuilder();

      RequestSpecification requestSpecification = requestSpecBuilder
          .setContentType(
              testResult.getTestContext().getCurrentXmlTest().getParameter("contentType"))
          .setBasePath(request.getPath())
          .addQueryParams(request.getParams())
          .addHeaders(request.getHeaders())
          .addCookies(request.getCookies()).build();

      ResponseSpecification responseSpecification =
          bulidBodySpec(responseSpecBuilder, response.getBody(), "")
              .expectStatusCode(Integer.parseInt(response.getStatusCode()))
              .expectHeaders(response.getHeaders())
              .expectCookies(response.getCookies())
              .build();

      given().filter((requestSpec, responseSpec, ctx) -> {
        io.restassured.response.Response newResponse = new ResponseBuilder()
            .clone(ctx.next(requestSpec, responseSpec)).setContentType(ContentType.JSON)
            .build();
        return newResponse;
      }).spec(requestSpecification).

      when().log().all()
          .post().
      then().log().all()
          .spec(responseSpecification);
    }
    callBack.runTestMethod(testResult);
    System.out.println("---------- leave hooker ----------");
  }


  private ResponseSpecBuilder bulidBodySpec(ResponseSpecBuilder responseSpecBuilder,
      Map<String, Object> body, String startPath) {
    for (Map.Entry<String, Object> entry : body.entrySet()) {
      String currentPath = entry.getKey();
      if (!(entry.getValue() instanceof Map)) {
        if (!"".equals(startPath)) {
          currentPath = startPath + "." + entry.getKey();
        }
        responseSpecBuilder.expectBody(currentPath, equalTo(entry.getValue()));
      } else {
        bulidBodySpec(responseSpecBuilder, (Map<String, Object>) entry.getValue(), entry.getKey());
      }
    }
    return responseSpecBuilder;
  }
}
