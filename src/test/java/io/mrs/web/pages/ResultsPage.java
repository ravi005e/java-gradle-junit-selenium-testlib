package io.mrs.web.pages;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.mrs.web.components.StandardPage;
import io.mrs.web.utils.LocatorType;
import io.mrs.web.utils.seleniun.ExpectedConditions;

import org.apache.commons.lang3.tuple.Pair;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ResultsPage extends StandardPage {

  public ResultsPage(WebDriver driver) {
    super(driver);
  }

  private Map<String, Pair<String, LocatorType>> fetchProperties() {
    Map<String, Pair<String, LocatorType>> elementsMap = new HashMap<>(); 
    elementsMap.put("results", Pair.of("rc", LocatorType.CLASS_NAME));
    
    return elementsMap;
  }


  protected ExpectedCondition<Boolean> isPageLoadedCondition() {
    return ExpectedConditions.presenceOfElementsLocated(fetchBy(fetchElementPair(fetchProperties(), "results")));
  }

  private List<WebElement>  results() {
    return findElements(fetchElementPair(fetchProperties(), "results"));
  }

  public int count() {
    return results().size();
  }

  public void validateQuery(final String searchString) throws Exception {
    WebDriverWait wait = new WebDriverWait(getDriver(), 10);
    wait.until((ExpectedCondition<Boolean>) d -> d.getTitle().toLowerCase().startsWith(searchString.toLowerCase()));

    System.out.println("Page title is: " + getDriver().getTitle());
  }

}