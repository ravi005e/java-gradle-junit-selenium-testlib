package io.mrs.web.components;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public abstract class BasePage extends Component implements Page {

  private final ExpectedCondition<Boolean> isPageLoadedCondition;
  protected static final long DELAY_BETWEEN_ELEMENT_CHECKS_IN_MS = 250;
  protected static final long PAGE_TIMEOUT_IN_SECONDS = 60;

  public BasePage(WebDriver driver, ExpectedCondition<Boolean> isPageLoadedCondition) {
    super(driver);

    this.isPageLoadedCondition = isPageLoadedCondition;
    waitForPageToLoad(driver);
  }

  public BasePage(WebDriver driver) {
    this(driver, null);
  }

  /**
   * Get an ExpectedCondition to use in validating that the underlying page has loaded for this Page object.
   * Currently, this implementation verifies that the page's title matches <code>getExpectedTitle()</code>
   *
   * @return an ExpectedCondition<Boolean>
   * @see org.openqa.selenium.support.ui.ExpectedConditions
   */
  protected ExpectedCondition<Boolean> isPageLoadedCondition() {
    return isPageLoadedCondition == null ?
        ExpectedConditions.titleIs(getDriver().getTitle()) :
          isPageLoadedCondition;
  }

  /**
   * waitForPageToLoad is part of the construction lifecycle of a page object. waitForPageToLoad is used to wait
   * until the page has loaded and is ready for further validation.
   * Delegates to <code>isPageLoadedCondition()</code> for the condition.
   *
   * @param driver is the driver to wait with
   */
  protected void waitForPageToLoad(final WebDriver driver) {
    WebDriverWait wait = new WebDriverWait(driver, PAGE_TIMEOUT_IN_SECONDS, DELAY_BETWEEN_ELEMENT_CHECKS_IN_MS);
    wait.until(isPageLoadedCondition());
  }

}
