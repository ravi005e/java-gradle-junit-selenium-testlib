package io.mrs.web.config;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;

import io.github.bonigarcia.wdm.WebDriverManager;

import java.util.Collections;
import java.util.HashMap;

public enum DriverType implements DriverSetup {

  FIREFOX {
    public RemoteWebDriver getWebDriverObject(DesiredCapabilities capabilities) {
      WebDriverManager.firefoxdriver().setup();
      FirefoxOptions options = new FirefoxOptions();
      
      options.setCapability("screenResolution", "1280x1024");
      options.setCapability("marionette", true);
      
      options.merge(capabilities);
      options.setHeadless(HEADLESS);

      return new FirefoxDriver(options);
    }
  },
  CHROME {
    public RemoteWebDriver getWebDriverObject(DesiredCapabilities capabilities) {
      WebDriverManager.chromedriver().setup();
      HashMap<String, Object> chromePreferences = new HashMap<>();
      chromePreferences.put("profile.password_manager_enabled", false);
      chromePreferences.put("profile.default_content_settings.popups", 0);

      ChromeOptions options = new ChromeOptions();
      options.setCapability("chrome.switches",
          Collections.singletonList("--no-default-browser-check"));
      options.setCapability("screenResolution", "1280x1024");
      options.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
      options.addArguments("--test-type");
      
      options.merge(capabilities);
      options.setHeadless(HEADLESS);
      options.setExperimentalOption("prefs", chromePreferences);

      return new ChromeDriver(options);
    }
  },
  EDGE {
    public RemoteWebDriver getWebDriverObject(DesiredCapabilities capabilities) {
      WebDriverManager.edgedriver().setup();
      EdgeOptions options = new EdgeOptions();
      
      options.setCapability("screenResolution", "1280x1024");
      options.merge(capabilities);

      return new EdgeDriver(options);
    }
  },
  SAFARI {
    public RemoteWebDriver getWebDriverObject(DesiredCapabilities capabilities) {
      SafariOptions options = new SafariOptions();
      
      options.setCapability("safari.cleanSession", true);
      options.setCapability("screenResolution", "1280x1024");
      
      options.merge(capabilities);

      return new SafariDriver(options);
    }
  };

  public final static boolean HEADLESS = Boolean.getBoolean("headless");

  @Override
  public String toString() {
    return super.toString().toLowerCase();
  }
}