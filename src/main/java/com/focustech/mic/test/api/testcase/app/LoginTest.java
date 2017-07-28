package com.focustech.mic.test.api.testcase.app;

import static io.restassured.RestAssured.*;
import static io.restassured.config.EncoderConfig.encoderConfig;
import static io.restassured.path.json.JsonPath.from;
import static org.hamcrest.MatcherAssert.assertThat;
import com.focustech.mic.test.api.dataProvider.ApiDataProvider;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import java.util.Arrays;
import java.util.Optional;
import org.hamcrest.CoreMatchers;
import org.testng.ITestContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class LoginTest {


  @Test(dataProviderClass = ApiDataProvider.class, dataProvider = "provider")
  public void testLogin(RequestSpecification requestSpecification,
      ResponseSpecification responseSpecification) {


    given().spec(requestSpecification)
        .then()
        .spec(responseSpecification);
  }

  @Test(groups = {"core"}, dependsOnMethods = {""})
  public void login_success() {
    RestAssured.basePath = "/ssl/mic/login";

    Response response = given().contentType(ContentType.JSON)
        .params("loginId", "lhpb")
        .params("password", "focus1234")
        .params("region", "MIC for Supplier Android")
        .when().log().all()
        .post()
        .then().log().all()
        .body(CoreMatchers.equalTo("{\"code\":10004,\"content\":\"\",\"err\":\"Incorrect Member ID/Email or Password\"}"))
    // 提取返回响应信息
    .extract().response();

    // 通过 jsonPath 校验 code
    assertThat(from(response.body().asString()).getInt("code"), CoreMatchers.equalTo(10004));

    System.out.println(response.cookie("sid"));
    System.out.println(response.sessionId());
  }

  @Test(invocationCount = 5, testName = "hello",groups = {"core"})
  public void testRegister() {

    RestAssured.basePath = "/ssl/openapi_mic/register";

    Response response = given().contentType(ContentType.JSON)
        .param("country", "Mexico")
        .param("countryKey", "Mexico")
        .param("email", "nuist_kevin072801@163.com")
        .param("memberId", "nuist_kevin072801")
        .param("password", "focus1234")
        .param("gender", "0")
        .param("companyName", "nuist_kevin072801 company")
        .param("companyRole", "tester")
        .param("region", "MIC for Supplier Android")
        .param("firstName", "Cai")
        .param("lastName", "Wen")
        .param("registerSource", "0")

        .when().log().all()
        .post()
        .then().log().all()
//        .body(CoreMatchers.equalTo("{\"code\":10004,\"content\":\"\",\"err\":\"Incorrect Member ID/Email or Password\"}"))
        // 提取返回响应信息
        .extract().response();

    // 通过 jsonPath 校验 code
//    assertThat(from(response.body().asString()).getInt("code"), CoreMatchers.equalTo(10004));

//    System.out.println(response.cookie("sid"));
//    System.out.println(response.sessionId());
  }

  @Test
  public void testStream() {
    String[] jsonPathArray = new String[]{"request", "body", "content", "username"};
    Optional<String> jsonPath =  Arrays.stream(jsonPathArray).reduce((a,b)-> a + "." + b);
    System.out.println(jsonPath.get());
  }
}
