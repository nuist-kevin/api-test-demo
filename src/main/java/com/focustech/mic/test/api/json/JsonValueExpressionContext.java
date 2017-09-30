package com.focustech.mic.test.api.json;

import static io.restassured.RestAssured.given;

import com.focustech.mic.test.api.domain.ApiResponse;
import com.focustech.mic.test.api.domain.TestCase;
import com.focustech.mic.test.api.utils.json.JsonUtil;
import io.restassured.RestAssured;
import io.restassured.http.Header;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.ParserContext;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

public class JsonValueExpressionContext {

  public static void main(String[] args)
      throws IOException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
    // Create and set a calendar
    GregorianCalendar c = new GregorianCalendar();
    c.set(1856, 7, 9);

    // The constructor arguments are name, birthday, and nationality.
    Inventor tesla = new Inventor("Nikola Tesla", c.getTime(), "Serbian");

    EvaluationContext evaluationContext = new StandardEvaluationContext(tesla);
    ExpressionParser parser = new SpelExpressionParser();
    ParserContext parserContext = new TemplateParserContext("${", "}");
    Expression exp = parser.parseExpression("name", parserContext);

    String name = (String) exp.getValue(evaluationContext);
    System.out.println(name);

//    Depend depend = new Depend();
//    ApiRequest dependRequest = new ApiRequest();

    List<TestCase> testCaseList = JsonUtil
        .readObjectListFromJsonFile("testcases/profile/get_success.json", TestCase.class);


    RestAssured.baseURI = "http://192.168.42.154:82";

    for (TestCase testCase : testCaseList) {

      ExtractableResponse<Response> extractableResponse = given()
          .params(testCase.getDepend().getApiRequest().getParams())
          .headers(testCase.getDepend().getApiRequest().getHeaders())
          .cookies(testCase.getDepend().getApiRequest().getCookies())
          .post(testCase.getDepend().getApiRequest().getPath())
          .then().extract();
      EvaluationContext testCaseevaluationContext = new StandardEvaluationContext(testCase);
      ApiResponse dependApiResponse = new ApiResponse();
      dependApiResponse.setCookies(extractableResponse.cookies().entrySet()
          .stream().collect(Collectors.toMap(Entry::getKey, Entry::getValue)));
      dependApiResponse.setHeaders(extractableResponse.headers().asList()
          .stream().collect(Collectors.toMap(Header::getName, Header::getValue)));
      dependApiResponse.setStatusCode(String.valueOf(extractableResponse.statusCode()));
      dependApiResponse.setBody(JsonUtil
          .readMapFromJsonString(extractableResponse.response().body().asString(), String.class,
              Object.class));
      testCase.getDepend().setApiResponse(dependApiResponse);

      Expression expression = parser
          .parseExpression("${depend.response.cookies[JSESSIONID]}", parserContext);
      String sessionId = (String) expression.getValue(testCaseevaluationContext);
      Map<String, String > cookies = testCase.getApiRequest().getCookies();
      cookies.forEach((k,v) -> {
        if (v.startsWith("${")&& v.endsWith("}")) {
          cookies.replace(k, (String) parser.parseExpression(v, parserContext).getValue());
        }
      });
      Map<String, String > params = testCase.getApiRequest().getParams();
      params.forEach((k,v) -> {
        if (v.startsWith("${")&& v.endsWith("}")) {
          cookies.replace(k, (String) parser.parseExpression(v, parserContext).getValue());
        }
      });
      Map<String, String > headers = testCase.getApiRequest().getHeaders();
      headers.forEach((k,v) -> {
        if (v.startsWith("${")&& v.endsWith("}")) {
          cookies.replace(k, (String) parser.parseExpression(v, parserContext).getValue());
        }
      });


      System.out.println(sessionId);
    }

  }


}
