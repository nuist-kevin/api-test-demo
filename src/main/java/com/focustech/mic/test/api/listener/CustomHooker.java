package com.focustech.mic.test.api.listener;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

import com.focustech.mic.test.api.annotation.TestFile;
import com.focustech.mic.test.api.domain.ApiRequest;
import com.focustech.mic.test.api.domain.ApiResponse;
import com.focustech.mic.test.api.domain.TestCase;
import com.focustech.mic.test.api.utils.json.JsonUtil;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.IHookCallBack;
import org.testng.IHookable;
import org.testng.ITestResult;

public class CustomHooker implements IHookable {

  private final static Logger logger = LoggerFactory.getLogger(CustomHooker.class);
  @Override
  public void run(IHookCallBack callBack, ITestResult testResult) {
    logger.debug(" running method hooker for test method {}", testResult.getMethod().getMethodName());

    String filePath = testResult.getMethod().getConstructorOrMethod().getMethod()
        .getDeclaredAnnotation(TestFile.class).value();

    logger.debug("Handling testcase file {}.", filePath);

    List<TestCase> testCaseList;
    try {
      testCaseList = JsonUtil.readObjectListFromJsonFile(filePath, TestCase.class);
    } catch (IOException e) {
      e.printStackTrace();
      throw new AssertionError("Json文件解析失败");
    }

    for (int i = 0; i < testCaseList.size(); i++) {

      logger.debug("Start running testcase {}", testCaseList.get(i).getTitle());

      ApiRequest apiRequest = testCaseList.get(i).getApiRequest();
      ApiResponse apiResponse = testCaseList.get(i).getApiResponse();

      RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder();
      ResponseSpecBuilder responseSpecBuilder = new ResponseSpecBuilder();

      RequestSpecification requestSpecification = requestSpecBuilder
          .setContentType(
              testResult.getTestContext().getCurrentXmlTest().getParameter("contentType"))
          .setBasePath(apiRequest.getPath())
          .addQueryParams(apiRequest.getParams())
          .addHeaders(apiRequest.getHeaders())
          .addCookies(apiRequest.getCookies()).build();

      ResponseSpecification responseSpecification =
          bulidBodySpec(responseSpecBuilder, apiResponse.getBody(), "")
              .expectStatusCode(Integer.parseInt(apiResponse.getStatusCode()))
              .expectHeaders(apiResponse.getHeaders())
              .expectCookies(apiResponse.getCookies())
              .build();

      given().filter((requestSpec, responseSpec, ctx) -> {
        Response newResponse = new ResponseBuilder()
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
    logger.debug("---------- leave hooker ----------");
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
