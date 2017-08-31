package com.focustech.mic.test.api.json;

import org.springframework.expression.EvaluationContext;

public interface JsonValueExpressionResolver {

  Object evaluate(String value, EvaluationContext evalContext);

}
