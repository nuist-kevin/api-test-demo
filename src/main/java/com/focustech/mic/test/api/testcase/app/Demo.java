package com.focustech.mic.test.api.testcase.app;

import static io.restassured.RestAssured.given;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import jdk.nashorn.internal.runtime.regexp.joni.Regex;
import org.testng.Assert;
import org.testng.annotations.Test;

public class Demo {

  @Test
  public void testTask() {
    String seesionId = given().param("userName", "admin").
        param("password", "passw0rd").
        when().post("http://192.168.50.175:8080/hamster-admin/logon")
        .sessionId();
    given().log().all().
        param("jobScheduleConfId", 795)
        .sessionId(seesionId)
        .post("http://192.168.50.175:8080/hamster-admin/execute")
        .then().log().all().statusCode(200);

  }

  @Test
  public void testReg() {
    String text = "<?xml version=\"1.0\" encoding=\"GBK\"?>\n"
        + "<stream><status>AAAAAAA</status><statusText>SUCCESS</statusText><data>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</data></stream>";
    String replace = text.replaceAll("<data>[\\S]*<\\/data>", "<data>heelo<\\/data>");
    System.out.println(replace);
    System.out.println(text);
//    boolean isMatch = Pattern.matches("(<data>[\\S]*<\\/data>)(\\1)*","<stream><data>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</data></stream>");
//    Assert.assertTrue(isMatch);



//    System.out.println(m.group(1));


  }


}
