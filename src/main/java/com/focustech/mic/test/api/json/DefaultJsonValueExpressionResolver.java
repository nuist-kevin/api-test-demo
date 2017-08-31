package com.focustech.mic.test.api.json;

import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;

public class DefaultJsonValueExpressionResolver implements JsonValueExpressionResolver {

  private ExpressionParser expressionParser;

  @Override
  public Object evaluate(String value, EvaluationContext evalContext) {
    Expression expr = this.expressionParser.parseExpression(value);
    return expr.getValue(evalContext);
  }
}
