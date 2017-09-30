package com.focustech.mic.test.api.listener;

import static org.dbunit.operation.DatabaseOperation.DELETE;
import static org.dbunit.operation.DatabaseOperation.INSERT;

import com.focustech.mic.test.api.annotation.ExpectData;
import com.focustech.mic.test.api.annotation.PrepareData;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import org.apache.commons.dbcp2.DataSourceConnectionFactory;
import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.excel.XlsDataSet;
import org.dbunit.operation.CompositeOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener2;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.unitils.dbunit.util.DataSetAssert;

public class DbVerifyListener implements IInvokedMethodListener2 {

  private final static Logger logger = LoggerFactory.getLogger(DbVerifyListener.class);

  @Override
  public void beforeInvocation(IInvokedMethod method, ITestResult testResult) {}

  @Override
  public void afterInvocation(IInvokedMethod method, ITestResult testResult) {}

  @Override
  public void beforeInvocation(IInvokedMethod method, ITestResult testResult,
      ITestContext context) {
    logger.debug("Preparing data for test method {}.", method.getTestMethod().getMethodName());
    PrepareData[] prepareDatas = method.getTestMethod().getConstructorOrMethod().getMethod()
        .getAnnotationsByType(PrepareData.class);
    CompositeOperation dbOperation = new CompositeOperation(DELETE, INSERT);
    for (PrepareData prepareData : prepareDatas) {
      DataSourceConnectionFactory connectionFactory = (DataSourceConnectionFactory) context
          .getSuite()
          .getAttribute(prepareData.datasource());
      if (connectionFactory == null) {
        logger.error("Getting connection factory {} failed.", prepareData.datasource());
        throw new AssertionError("Getting connection factory failed.");
      }
      IDataSet dataSet = createXlsDataSet(prepareData.xlsFile());
      IDatabaseConnection dbunitConenction = getDbunitDatabaseConnection(connectionFactory);
      try {
        dbOperation.execute(dbunitConenction, dataSet);
      } catch (Exception e) {
        e.printStackTrace();
        throw new AssertionError("Executing dbunit operation for preparing data failed.");
      } finally {
        closeDbunitConnection(dbunitConenction);
      }
    }
  }

  @Override
  public void afterInvocation(IInvokedMethod method, ITestResult testResult, ITestContext context) {
    ExpectData[] expectDatas =
        method.getTestMethod().getConstructorOrMethod().getMethod()
            .getAnnotationsByType(ExpectData.class);
    for (ExpectData expectData : expectDatas) {

      IDataSet expectDataSet = createXlsDataSet(expectData.xlsFile());
      IDataSet actualDataSet;

      DataSourceConnectionFactory connectionFactory = (DataSourceConnectionFactory) context
          .getSuite().getAttribute(expectData.datasource());
      if (connectionFactory == null) {
        logger.error("Getting connection factory {} failed.", expectData.datasource());
        throw new AssertionError("Getting connection factory failed.");
      }

      IDatabaseConnection dbunitConenction = getDbunitDatabaseConnection(connectionFactory);

      try {
        actualDataSet = dbunitConenction.createDataSet();
      } catch (SQLException e) {
        e.printStackTrace();
        throw new AssertionError("Creating dataset for verify failed.");
      } finally {
        closeDbunitConnection(dbunitConenction);
      }
      // 利用Unitils框架中已有的断言工具来断言
      new DataSetAssert()
          .assertEqualDbUnitDataSets(expectData.datasource(), expectDataSet, actualDataSet);
      // TODO 清空测试数据
    }

  }

  private IDatabaseConnection getDbunitDatabaseConnection(
      DataSourceConnectionFactory connectionFactory) {
    IDatabaseConnection dbunitConenction = null;
    try (Connection jdbcConnection = connectionFactory.createConnection()) {
      dbunitConenction = new DatabaseConnection(jdbcConnection);
    } catch (SQLException e) {
      e.printStackTrace();
      throw new AssertionError("Creating jdbc connection failed.");
    } catch (DatabaseUnitException e) {
      e.printStackTrace();
      throw new AssertionError("Creating dbunit connection failed.");
    }
    return dbunitConenction;
  }

  private void closeDbunitConnection(IDatabaseConnection dbunitConenction) {
    if (dbunitConenction != null) {
      try {
        dbunitConenction.close();
      } catch (SQLException e) {
        e.printStackTrace();
        throw new AssertionError("Closing dbunit connection failed.");
      }
    }
  }

  private XlsDataSet createXlsDataSet(String classpathFileName) {
    XlsDataSet dataSet;
    try {
      dataSet = new XlsDataSet(
          this.getClass().getClassLoader().getResourceAsStream(classpathFileName));
    } catch (IOException e) {
      e.printStackTrace();
      throw new AssertionError("Getting dataset file failed.");
    } catch (DataSetException e) {
      e.printStackTrace();
      throw new AssertionError("Creating dataset failed.");
    }
    return dataSet;
  }
}
