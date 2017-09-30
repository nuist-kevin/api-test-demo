package com.focustech.mic.test.api.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)

@Repeatable(ExpectDataGroup.class)
public @interface ExpectData {

  String xlsFile();
  String datasource();

}
