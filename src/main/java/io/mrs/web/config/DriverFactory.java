package io.mrs.web.config;

import org.openqa.selenium.Platform;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import static io.mrs.web.config.DriverType.valueOf;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DriverFactory {

  private RemoteWebDriver webDriver;
  private DriverType selectedDriverType;

  private final String operatingSystem = System.getProperty("os.name").toUpperCase();
  private final String browser = System.getProperty("browser", "chrome").toUpperCase();
  private final String systemArchitecture = System.getProperty("os.arch");
  private final boolean useRemoteWebDriver = Boolean.getBoolean("remoteDriver");

  protected static final Logger LOGGER = LogManager.getLogger(DriverFactory.class);

  public DriverFactory() {
    DriverType driverType = null;
    try {
      driverType = valueOf(browser);
    } catch (IllegalArgumentException ignored) {
      LOGGER.error("Unknown driver specified, defaulting to '" + driverType + "'...");
    } catch (NullPointerException ignored) {
      LOGGER.error("No driver specified, defaulting to '" + driverType + "'...");
    }
    selectedDriverType = driverType;
  }

  public RemoteWebDriver getDriver() throws MalformedURLException {
    if (null == webDriver) {
      instantiateWebDriver(selectedDriverType);
    }

    return webDriver;
  }

  public void quitDriver() {
    if (null != webDriver) {
      webDriver.quit();
      webDriver = null;
    }
  }

  private void instantiateWebDriver(DriverType driverType) throws MalformedURLException {
    LOGGER.info(" ");
    LOGGER.info("Local Operating System: " + operatingSystem);
    LOGGER.info("Local Architecture: " + systemArchitecture);
    LOGGER.info("Selected Browser: " + selectedDriverType);
    LOGGER.info("Headless Mode: " + Boolean.getBoolean("headless"));
    //		LOGGER.info("Connecting to Selenium Grid: " + useRemoteWebDriver);
    LOGGER.info(" ");

    DesiredCapabilities desiredCapabilities = new DesiredCapabilities();

    if (useRemoteWebDriver) {
      URL seleniumGridURL = new URL(System.getProperty("gridURL"));
      String desiredBrowserVersion = System.getProperty("desiredBrowserVersion");
      String desiredPlatform = System.getProperty("desiredPlatform");

      if (null != desiredPlatform && !desiredPlatform.isEmpty()) {
        desiredCapabilities.setPlatform(Platform.valueOf(desiredPlatform.toUpperCase()));
      }

      if (null != desiredBrowserVersion && !desiredBrowserVersion.isEmpty()) {
        desiredCapabilities.setVersion(desiredBrowserVersion);
      }

      desiredCapabilities.setBrowserName(selectedDriverType.toString());
      webDriver = new RemoteWebDriver(seleniumGridURL, desiredCapabilities);
    } else {
      webDriver = driverType.getWebDriverObject(desiredCapabilities);
    }
  }
}