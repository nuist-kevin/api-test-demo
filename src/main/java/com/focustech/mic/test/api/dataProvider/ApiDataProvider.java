package com.focustech.mic.test.api.dataProvider;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.focustech.mic.test.api.domain.Depend;
import com.focustech.mic.test.api.domain.Request;
import com.focustech.mic.test.api.domain.TestCase;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.testng.ITestContext;
import org.testng.annotations.Test;

public class ApiDataProvider {

  @Test
  public void provider(ITestContext testContext) throws IOException {

    RestAssured.baseURI = "http://192.168.42.154:82";
    ObjectMapper objectMapper = new ObjectMapper();
    String testcaseDir = testContext.getCurrentXmlTest().getParameter("testcaseDir");

    try (InputStream fileInputStream = this.getClass()
        .getResourceAsStream("/testcases/login/loginTest.json")) {

      JavaType javaType = objectMapper.getTypeFactory().constructParametricType(ArrayList.class,
          TestCase.class);
      List<TestCase> testCaseList = objectMapper.readValue(fileInputStream, javaType);

      Object[][] objects = new Object[2][testCaseList.size()];

      for (int i = 0; i < testCaseList.size(); i++) {

        Depend depend = testCaseList.get(i).getDepend();

        Request request = testCaseList.get(i).getRequest();
        RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder();
        ResponseSpecBuilder responseSpecBuilder = new ResponseSpecBuilder();

        com.focustech.mic.test.api.domain.Response response = testCaseList.get(i).getResponse();

        io.restassured.response.Response dependResponse = given().basePath(depend.getUrl())
            .params(depend.getParams())
            .headers(depend.getHeaders())
            .cookies(depend.getCookies())
            .post()
            .then()
            .extract().response();


        Map<String,String> params = request.getParams();
        Iterator<String> iterator = params.keySet().iterator();
        while (iterator.hasNext()) {
          String value = params.get(iterator.next());
          if (value.startsWith("${") && value.endsWith("}")) {
            String[] paths = value.split(".");
            if (paths[0].equals("request")) {
              if (paths[1].equals("param")) {
                value = depend.getParams().get(paths[2]);
              }
              if (paths[1].equals("cookie")) {
                value = depend.getCookies().get(paths[2]);
              }
              if (paths[1].equals("header")) {
                value = depend.getHeaders().get(paths[2]);
              }
            }
            if (paths[0].equals("response")) {
              if (paths[1].equals("cookie")) {
                value = dependResponse.getCookies().get(paths[2]);
              }
              if (paths[1].equals("header")) {
                value = dependResponse.getHeaders().getValue(paths[2]);
              }
              if (paths[1].equals("body")) {
                String[] jsonPathArray = Arrays.copyOfRange(paths, 2, paths.length);
                Optional<String> jsonPath =  Arrays.stream(jsonPathArray).reduce((a,b)-> a + "." + b);
                value = new JsonPath(dependResponse.body().asString()).getString(jsonPath.get());
              }
            }
          }
        }

        RequestSpecification requestSpecification = requestSpecBuilder
            .addParams(request.getParams())
            .addHeaders(request.getHeaders())
            .addCookies(request.getCookies())
            .addMultiPart(new File(request.getFile())).build();



        ResponseSpecification responseSpecification =
            responseSpecBuilder
                .expectStatusCode(response.getStatusCode())
                .expectHeaders(response.getHeaders())
                .expectCookies(response.getCookies())
                .expectBody(equalTo(response.getBody())).build();
        objects[0][i] = requestSpecification;
        objects[1][i] = responseSpecification;
      }
    }

//    return objects;
  }
}
