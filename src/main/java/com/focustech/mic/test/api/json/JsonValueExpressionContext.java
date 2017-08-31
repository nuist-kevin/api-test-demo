package com.focustech.mic.test.api.json;

import static io.restassured.RestAssured.given;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.focustech.mic.test.api.domain.Depend;
import com.focustech.mic.test.api.domain.Request;
import com.focustech.mic.test.api.domain.TestCase;
import com.focustech.mic.test.api.utils.json.JsonUtil;
import io.restassured.RestAssured;
import io.restassured.http.Header;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.io.IOException;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.BeanUtilsBean2;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.ParserContext;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

public class JsonValueExpressionContext {

  public static void main(String[] args) throws IOException {
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
//    Request dependRequest = new Request();

    List<TestCase> testCaseList = JsonUtil
        .readObjectListFromJsonFile("testcases/profile/get_success.json", TestCase.class);
    RestAssured.baseURI = "http://192.168.42.154:82";

    for (TestCase testCase : testCaseList) {
      ExtractableResponse<Response> extractableResponse = given()
          .params(testCase.getDepend().getRequest().getParams())
          .headers(testCase.getDepend().getRequest().getHeaders())
          .cookies(testCase.getDepend().getRequest().getCookies())
          .post(testCase.getDepend().getRequest().getPath())
          .then().extract();
      EvaluationContext testCaseevaluationContext = new StandardEvaluationContext(testCase);
      com.focustech.mic.test.api.domain.Response dependResponse = new com.focustech.mic.test.api.domain.Response();
      dependResponse.setCookies(extractableResponse.cookies().entrySet()
          .stream().collect(Collectors.toMap(Entry::getKey, Entry::getValue)));
      dependResponse.setHeaders(extractableResponse.headers().asList()
          .stream().collect(Collectors.toMap(Header::getName, Header::getValue)));
      dependResponse.setStatusCode(String.valueOf(extractableResponse.statusCode()));
      dependResponse.setBody(JsonUtil
          .readMapFromJsonString(extractableResponse.response().body().asString(), String.class,
              Object.class));
      testCase.getDepend().setResponse(dependResponse);


      Expression expression = parser.parseExpression("${depend.response.cookies[JSESSIONID]}", parserContext);
      String sessionId = (String) expression.getValue(testCaseevaluationContext);
      System.out.println(sessionId);
    }

  }
}
