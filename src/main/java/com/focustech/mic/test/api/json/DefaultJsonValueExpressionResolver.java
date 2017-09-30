package com.focustech.mic.test.api.json;

import java.util.Arrays;
import org.apache.commons.beanutils.BeanUtils;
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

  public Object evaluate(Object object, EvaluationContext evalContext)
      throws Exception {
    Object returnObject = BeanUtils.cloneBean(object);
    Arrays.stream(object.getClass().getDeclaredFields())
        .filter(field -> field.getType().isAssignableFrom(String.class))
        .forEach(field -> {
          field.setAccessible(true);
          try {
            field.set(returnObject, evaluate(field.get(object), evalContext));
          } catch (Exception e) {
            e.printStackTrace();
          }
        });

    return returnObject;
  }
}
