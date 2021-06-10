package io.mrs.web;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.remote.RemoteWebDriver;

import io.mrs.web.config.DriverFactory;
import io.mrs.web.utils.junit.rules.ScreenshotRule;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ExtendWith(ScreenshotRule.class)
public abstract class WebBaseTest {
  private static Class<?> thisClass =
      new Object() { }.getClass().getEnclosingClass();

  private static List<DriverFactory> webDriverThreadPool = Collections.synchronizedList(new ArrayList<DriverFactory>());
  private static ThreadLocal<DriverFactory> driverThread;

  @BeforeAll
  public static void instantiateDriverObject() {
    driverThread = new ThreadLocal<DriverFactory>() {
      @Override
      protected DriverFactory initialValue() {
        DriverFactory webDriverThread = new DriverFactory();
        webDriverThreadPool.add(webDriverThread);
        return webDriverThread;
      }
    };
  }

  public static RemoteWebDriver getDriver() throws MalformedURLException {
    return driverThread.get().getDriver();
  }
  
  public static void open() throws MalformedURLException {
    String url = System.getProperty("baseurl");
    if (null == url) {
      throw new Error("Page does not support navigating manually.");
    }
    if (url.endsWith("/")) {
      throw new Error(String.format("open need a url without a trailing / (url: %s)", url));
    }
    getLogger().info(String.format("navigating to url: %s", url));
    getDriver().get(url);
  }

  public static void open(String path) throws MalformedURLException {
    String url = System.getProperty("baseurl");
    if (null == url) {
      throw new Error("Please set 'baseurl' for test to run.");
    }
    getLogger().info(String.format("base url: %s", url));
    if (url.endsWith("/")) {
      throw new Error(String.format("navigate need a baseUrl without a trailing / (baseUrl: %s)", url));
    }
    if (!path.startsWith("/")) {
      throw new Error(String.format("navigate need a path with a leading / (path: %s)", path));
    }
    getLogger().info(String.format("navigating to site: %s%s", url, path));
    getDriver().get(url + path);
  }

  
  @AfterEach
  public void clearCookies() throws MalformedURLException {
    try {
      getDriver().manage().deleteAllCookies();
    } catch (Exception ex) {
      System.err.println("Unable to delete cookies: " + ex);
      getDriver().navigate().refresh();
    }
  }

  @AfterAll
  public static void closeDriverObjects() {
    for (DriverFactory webDriverThread : webDriverThreadPool) {
      webDriverThread.quitDriver();
    }
  }
  
  protected static Logger getLogger() {
    return LogManager.getLogger(thisClass.getClass());
  }
}