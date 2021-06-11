package io.mrs.web.components;

import java.time.Duration;
import java.util.List;
import java.util.NoSuchElementException;

import com.google.common.base.Function;

import io.mrs.web.accessKeys.Browser;
import io.mrs.web.accessKeys.OperatingSystem;
import io.mrs.web.utils.LocatorType;
import io.mrs.web.utils.offset.CalculateOffsetPosition;
import io.mrs.web.utils.offset.CursorPosition;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.Locatable;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.Map;

import org.openqa.selenium.Keys;
import org.openqa.selenium.Platform;
import org.openqa.selenium.Point;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static io.mrs.web.accessKeys.Browser.FIREFOX;
import static org.apache.commons.lang3.StringUtils.*;

public class Component {
  private final WebDriver driver;

  /**
   * Construct a component using the provided WebDriver
   * 
   * @param driver
   *            is a WebDriver whose state and actions are being is being
   *            encapsulated
   */
  public Component(WebDriver driver) {
    this.driver = driver;
  }

  public WebDriver getDriver() {
    return this.driver;
  }

  public Pair<String, LocatorType> fetchElementPair(Map<String, Pair<String, LocatorType>> map, String key) {
    return map.get(key);
  }
  
  protected boolean isVisible(By by) {
    List<WebElement> elements = this.driver.findElements(by);
    if (elements.isEmpty()) {
      return false;
    } else {
      for (WebElement ele : elements) {
        if (!ele.isDisplayed()) {
          return false;
        }
      }
      return true;
    }
  }

  protected boolean isPresent(By by) {
    try {
      this.driver.findElement(by);
      return true;
    } catch (NoSuchElementException e) {
      return false;
    }
  }  

  protected Logger getLogger() {
    return LogManager.getLogger(getClass());
  }
  
  protected boolean isEmptyString(String pattern) {
    return (isEmpty(pattern) || isBlank(pattern));
  }

  public void sleep(long waitTimeInMS, String reason) {
    if (isEmptyString(reason)) {
      throw new IllegalArgumentException(
          "a reason for sleeping must be specified");
    }

    getLogger().info(
        "Sleeping for " + waitTimeInMS + "ms because: " + reason);

    try {
      Thread.sleep(waitTimeInMS);
    } catch (InterruptedException e) {
      getLogger().error(
          "Caught InterruptedException while sleeping: "
              + e.getMessage(), e);
    }
  }  

  public boolean containsMatchingPattern(String pattern) {
    WebElement bodySource = this.driver.findElement(By.tagName("body"));
    String bodyText = bodySource.getText();
    return bodyText.contains(pattern);
  }  

  public boolean pageSourceContains(String pattern) {
    return this.driver.getPageSource().contains(pattern);
  }

  /**
   * Executes a script on an element
   * @param javascript The script to execute
   * @param element The target of the script, referenced as arguments[0]
   */
  protected void executeScript(String javascript, WebElement element) {
    ((JavascriptExecutor) this.driver).executeScript(javascript, element);
  }

  /**
   * Executes a script
   * @param javascript The script to execute
   * @return One of Boolean, Long, String, List or WebElement. Or null.
   */
  protected Object executeScript(String javascript) {
    return ((JavascriptExecutor) this.driver).executeScript(javascript);
  } 
  
  public By fetchBy(Pair<String, LocatorType> elementPair) {
    String selector = elementPair.getLeft();
    switch (elementPair.getRight()) {
      case CLASS_NAME:
        return By.className(selector);
      case CSS:
        return By.cssSelector(selector);
      case ID:
        return By.id(selector);
      case LINK_TEXT:
        return By.linkText(selector);
      case PARTIAL_LINK_TEXT:
        return By.partialLinkText(selector);
      case TAG_NAME:
        return By.tagName(selector);
      case XPATH:
        return By.xpath(selector);
      case NAME:
        return By.name(selector);
      default:
        throw new Error("failed to find selector ["+selector+"]");
    }
  }

  protected WebElement findElement(Pair<String, LocatorType> elementPair) {
    return this.driver.findElement(fetchBy(elementPair));
  }

  protected List<WebElement> findElements(Pair<String, LocatorType> elementPair) {
        return this.driver.findElements(fetchBy(elementPair));
  }

  public ExpectedCondition<Boolean> angularHasFinishedProcessing() {
    return new ExpectedCondition<Boolean>() {
      @Override
      public Boolean apply(WebDriver driver) {
        return Boolean.valueOf(((JavascriptExecutor) driver).executeScript("return (window.angular !== undefined) && (angular.element(document).injector() !== undefined) && (angular.element(document).injector().get('$http').pendingRequests.length === 0)").toString());
      }
    };
  }

  public void isjQueryLoaded(WebDriver driver) {
    getLogger().info("Waiting for ready state complete");
    (new WebDriverWait(driver, 30)).until(new ExpectedCondition<Boolean>() {
      public Boolean apply(WebDriver d) {
        JavascriptExecutor js = (JavascriptExecutor) d;
        String readyState = js.executeScript("return document.readyState").toString();
        getLogger().info("Ready State: " + readyState);
        return (Boolean) js.executeScript("return !!window.jQuery && window.jQuery.active == 0");
      }
    });
  }

  public Function<WebDriver, Boolean> listenerIsRegisteredOnElement(final String listenerType, final WebElement element) {
    return new Function<WebDriver, Boolean>() {
      public Boolean apply(WebDriver driver) {
        @SuppressWarnings("unchecked")
        Map<String, Object> registeredListeners = (Map<String, Object>) ((JavascriptExecutor) driver).executeScript("return (window.jQuery != null)"
            + " && (jQuery._data(jQuery(arguments[0]).get(0)), 'events')", element);
        for (Map.Entry<String, Object> listener : registeredListeners.entrySet()) {
          if (listener.getKey().equals(listenerType)) {
            return true;
          }
        }
        return false;
      }
    };
  }

  public Function<WebDriver, Boolean> elementHasStoppedMoving(final WebElement element) {
    return new Function<WebDriver, Boolean>() {
      public Boolean apply(WebDriver driver) {
        Point initialLocation = ((Locatable) element).getCoordinates().inViewPort();
        try {
          Thread.sleep(50);
        } catch (InterruptedException ignored) {
          //ignored
        }
        Point finalLocation = ((Locatable) element).getCoordinates().inViewPort();
        return initialLocation.equals(finalLocation);
      }
    };
  }	

  protected void triggerAccessKeyLocal(String accessKey) {
    Actions advancedActions = new Actions(this.driver);
    OperatingSystem currentOS = OperatingSystem.getCurrentOperatingSystem();
    String currentBrowserName = ((RemoteWebDriver) this.driver).getCapabilities().getBrowserName();
    Browser currentBrowser = Browser.getBrowserType(currentBrowserName);

    switch (currentOS) {
      case OSX:
        advancedActions.keyDown(Keys.CONTROL)
        .keyDown(Keys.ALT)
        .sendKeys(accessKey)
        .keyUp(Keys.ALT)
        .keyUp(Keys.CONTROL)
        .perform();
        break;
      case LINUX:
      case WINDOWS:
        if (currentBrowser.equals(FIREFOX)) {
          advancedActions.keyDown(Keys.ALT)
          .keyDown(Keys.SHIFT)
          .sendKeys(accessKey)
          .keyUp(Keys.SHIFT)
          .keyUp(Keys.ALT)
          .perform();
        } else {
          advancedActions.keyDown(Keys.ALT)
          .sendKeys(accessKey)
          .keyUp(Keys.ALT)
          .perform();
        }
        break;
      default:
        throw new IllegalArgumentException("Unrecognised operating system name '" + currentOS + "'");
    }
  }

  protected void triggerAccessKey(String accessKey) {
    Actions advancedActions = new Actions(this.driver);
    Platform currentOS = ((RemoteWebDriver) this.driver).getCapabilities().getPlatform();
    Platform currentOSFamily = (null == currentOS.family() ? currentOS : currentOS.family());
    String currentBrowserName = ((RemoteWebDriver) this.driver).getCapabilities().getBrowserName();
    Browser currentBrowser = Browser.getBrowserType(currentBrowserName);

    switch (currentOSFamily) {
      case MAC:
        advancedActions.keyDown(Keys.CONTROL)
        .keyDown(Keys.ALT)
        .sendKeys(accessKey)
        .keyUp(Keys.ALT)
        .keyUp(Keys.CONTROL)
        .perform();
        break;
      case UNIX:
      case WINDOWS:
        if (currentBrowser.equals(FIREFOX)) {
          advancedActions.keyDown(Keys.ALT)
          .keyDown(Keys.SHIFT)
          .sendKeys(accessKey)
          .keyUp(Keys.SHIFT)
          .keyUp(Keys.ALT)
          .perform();
        } else {
          advancedActions.keyDown(Keys.ALT)
          .sendKeys(accessKey)
          .keyUp(Keys.ALT)
          .perform();
        }
        break;
      default:
        throw new IllegalArgumentException("Unrecognised operating system name '" + currentOS + "'");
    }
  }

  protected void chooseCSSMenuOption(WebElement servicesMenuOption,
      WebElement webDevelopmentSubMenuOption) {
    Actions advancedActions = new Actions(driver);
    WebDriverWait wait = new WebDriverWait(driver, 5, 100);

    advancedActions.moveToElement(servicesMenuOption)
      .perform();

    wait.until(ExpectedConditions.visibilityOf(webDevelopmentSubMenuOption));

    advancedActions.moveToElement(webDevelopmentSubMenuOption)
      .click()
      .perform();
  }

  protected void moveToElementWithOffset( WebElement firstBox, WebElement firstBoxText, 
      WebElement obliterator) {
    Actions advancedActions = new Actions(driver);

    CalculateOffsetPosition op =
        new CalculateOffsetPosition(firstBox, firstBoxText, CursorPosition.CENTER);

    advancedActions.moveToElement(firstBox)
      .moveByOffset(op.getXOffset(), op.getYOffset())
      .clickAndHold()
      .moveToElement(obliterator)
      .release()
      .perform();
  }

  protected void injectScript(String scriptURL) {
    ((JavascriptExecutor) this.driver).executeScript("function injectScript(url) {\n" +
        "    var script = document.createElement('script');\n" +
        "    script.src = url;\n" +
        "    var head = document.getElementsByTagName('head')[0];\n" +
        "    head.appendChild(script);\n" +
        "}\n" +
        "\n" +
        "var scriptURL = arguments[0];\n" +
        "injectScript(scriptURL);"
        , scriptURL);
  }

  protected String getHiddenText(WebElement element) {
    return (String) ((JavascriptExecutor) driver).executeScript("return arguments[0].text", element);
  }

  protected WebElement waitUntilElementPresent(By by) {
    return waitUntilElementPresent(by, 30, 5);
  }
  
  protected WebElement waitUntilElementPresent(By by, int polling, int pollingInterval) {
    Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
        .withTimeout(Duration.ofSeconds(polling))
        .pollingEvery(Duration.ofSeconds(pollingInterval))
        .ignoring(NoSuchElementException.class);

    return wait.until(new Function<WebDriver, WebElement>() {
      public WebElement apply(WebDriver driver) {
        return driver.findElement(by);
      }
    });
  }

  protected void waitUntilElementNotPresent(By by) {
    waitUntilElementNotPresent(by, 30, 5);
  }
  
  protected void waitUntilElementNotPresent(By by, int polling, int pollingInterval) {
    Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
        .withTimeout(Duration.ofSeconds(polling))
        .pollingEvery(Duration.ofSeconds(pollingInterval))
        .ignoring(NoSuchElementException.class);

    wait.until(new Function<WebDriver, Boolean>() {
      public Boolean apply(WebDriver driver) {
        return (driver.findElement(by) == null);
      }
    });
  }
  
  public WebElement waitForElementToBeClickable(By locator, String reason, int timeOut){
    if (isEmptyString(reason)) {
        throw new IllegalArgumentException(
                "a reason for wait must be specified");
    }

    WebDriverWait wait = new WebDriverWait(getDriver(), timeOut);
    return wait.until(ExpectedConditions.elementToBeClickable(locator));
  }
  
  public void waitForTextToBePresent(By locator, String reason, int timeOut, String text){
    if (isEmptyString(reason)) {
        throw new IllegalArgumentException(
                "a reason for wait must be specified");
    }

    WebDriverWait wait = new WebDriverWait(getDriver(), timeOut);
    wait.until(ExpectedConditions.textToBePresentInElementLocated(locator, text));
  }
  
}
