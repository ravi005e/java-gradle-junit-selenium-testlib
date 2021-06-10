package io.mrs.web.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class PropertyLoader {
  public static Properties loadProperties() {
    ArrayList<String> propFileList = getPropertyFileList();
    try {
      return PropertyLoader.loadDefaultAndCustomProps(propFileList);
    } catch (IOException e) {
      throw new RuntimeException("Could not load properties from " + propFileList, e);
    }
  }

  /**
   * Load a new Properties instance created from the provided property file paths and the System's properties.
   * <p>
   * Files will be loaded in the order specified by the List.
   *
   * @param propFilePathList
   * @return Properties containing file and system properties
   */
  public static Properties loadDefaultAndCustomProps(List<String> propFilePathList)
      throws IOException {

    Properties testProps = new Properties();
    for (String propFilePath : propFilePathList) {
      testProps.load(PropertyLoader.class.getResourceAsStream(propFilePath));
    }

    testProps.putAll(System.getProperties());

    return testProps;
  }

  private static ArrayList<String> getPropertyFileList() {
    ArrayList<String> propFileList = new ArrayList<>();
    propFileList.add("/defaultTest.properties");
    return propFileList;
  }
}
