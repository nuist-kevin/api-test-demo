package com.focustech.mic.test.api.testcase.app;

import com.focustech.mic.test.api.annotation.ExpectData;
import com.focustech.mic.test.api.annotation.PrepareData;
import com.focustech.mic.test.api.annotation.TestFile;
import org.testng.annotations.Test;
import org.unitils.dbunit.annotation.DataSet;

/**
 * Created by caiwen on 2017/8/1.
 */
public class RegisterTest {

  @Test(groups = {"core"})
  @PrepareData(xlsFile = "test.xls", datasource = "cb")
  @PrepareData(xlsFile = "cboss.xls", datasource = "cboss")
  @ExpectData(xlsFile = "cboss.xls", datasource = "cboss")
  @ExpectData(xlsFile = "cb.xls", datasource = "cb")
  public void register_success() {

  }
}
