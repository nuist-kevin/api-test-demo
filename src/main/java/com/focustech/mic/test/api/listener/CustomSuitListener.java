package com.focustech.mic.test.api.listener;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbcp2.BasicDataSourceFactory;
import org.apache.commons.dbcp2.DataSourceConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ISuite;
import org.testng.ISuiteListener;

public class CustomSuitListener implements ISuiteListener {

  Logger logger = LoggerFactory.getLogger("suite");

  @Override
  public void onStart(ISuite suite) {
    logger.debug("start test suite {}.", suite.getName());
    Map<String, Properties> dataSourceProperties = null;
    try {
      dataSourceProperties = loadDataSourceProperties();
    } catch (IOException e) {
      e.printStackTrace();
    }
    for (String dataSourceName : dataSourceProperties.keySet()) {
      if (suite.getAttribute(dataSourceName) != null) {
        logger.warn("Suite attribute {} already exists, skip setting it as datasource.",
            dataSourceName);
        continue;
      }
      try {
        BasicDataSource basicDataSource = BasicDataSourceFactory
            .createDataSource(dataSourceProperties.get(dataSourceName));
        DataSourceConnectionFactory dataSourceConnectionFactory = new DataSourceConnectionFactory(
            basicDataSource);
        suite.setAttribute(dataSourceName, dataSourceConnectionFactory);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  @Override
  public void onFinish(ISuite suite) {
    logger.debug("finish test suite {}.", suite.getName());
  }

  private Map<String, Properties> loadDataSourceProperties() throws IOException {
    Map<String, Properties> dataSourcePropertiesMap = new HashMap<>();
    Properties globalProperties = new Properties();
    globalProperties
        .load(this.getClass().getClassLoader().getResourceAsStream("datasource.properties"));
    String[] dbNames = globalProperties.getProperty("datasource.names").split(",");
    Arrays.stream(dbNames).map(String::trim)
        .forEach(dbName -> {
          Properties dataSourceProps = new Properties();
          globalProperties.keySet().stream()
              .filter(key -> !("datasource.names".equals(((String) key))))
              .filter(key ->
                  ((String) key).split("\\.")[2].equals(dbName)).
              forEach(key -> {
                dataSourceProps.setProperty(((String) key).split("\\.")[1],
                    globalProperties.getProperty((String) key));
              });
          dataSourcePropertiesMap.put(dbName, dataSourceProps);
        });
    return dataSourcePropertiesMap;
  }
}
