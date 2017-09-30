package com.focustech.mic.test.api.handler;

import com.focustech.mic.test.api.domain.TestCase;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.ParserContext;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

public class ElExpressionHandler implements TestCaseHandler {

  private TestCaseHandler nextHandler;

  private ParserContext parserContext = new TemplateParserContext("${", "}");

  @Override
  public void handle(TestCase testCase) {

    EvaluationContext evaluationContext = new StandardEvaluationContext(testCase);

    ExpressionParser parser = new SpelExpressionParser();

    Expression exp = parser
        .parseExpression("", parserContext);

    String sessionId = (String) exp.getValue(evaluationContext);

    testCase.getApiRequest().getCookies().put("sessionId", sessionId);

  }

  @Override
  public void setNextHandler(TestCaseHandler nextHandler) {
    this.nextHandler = nextHandler;
  }
}
