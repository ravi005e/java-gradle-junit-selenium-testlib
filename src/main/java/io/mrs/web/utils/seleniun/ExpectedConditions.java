package io.mrs.web.utils.seleniun;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;

/**
 * ExpectedConditions provides convenience methods for creating ExpectedConditions to pick-up where
 * Selenium's ExpectedConditions leaves off.
 *
 * @see org.openqa.selenium.support.ui.ExpectedConditions
 */
public class ExpectedConditions {

  /**
   * Looks for presence of elements located by the provided locator.
   *
   * @param locator is used to locate elements
   * @return false when no elements match; true when 1 or more elements match
   */
  public static ExpectedCondition<Boolean> presenceOfElementsLocated(final By locator){
    return new ExpectedCondition<Boolean>() {
      @Override
      public Boolean apply(WebDriver driver) {
        return !driver.findElements(locator).isEmpty();
      }

      @Override
      public String toString() {
        return String.format("looking for presence of elements located by \"%s\"", locator);
      }
    };
  }
}
