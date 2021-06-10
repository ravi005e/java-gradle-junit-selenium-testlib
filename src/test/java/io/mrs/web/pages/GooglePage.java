package io.mrs.web.pages;

import io.mrs.web.components.StandardPage;
import io.mrs.web.utils.LocatorType;
import io.mrs.web.utils.seleniun.ExpectedConditions;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;

public class GooglePage extends StandardPage {

  public GooglePage(WebDriver driver) {
    super(driver);
  }

  private Map<String, Pair<String, LocatorType>> fetchProperties() {
    Map<String, Pair<String, LocatorType>> elementsMap = new HashMap<>(); 
    elementsMap.put("query", Pair.of("q", LocatorType.NAME));
    
    return elementsMap;
  }

  protected ExpectedCondition<Boolean> isPageLoadedCondition() {
    return ExpectedConditions.presenceOfElementsLocated(fetchBy(fetchElementPair(fetchProperties(), "query")));
  }

  private WebElement searchField() {
    return findElement(fetchElementPair(fetchProperties(), "query"));
  }

  public void setQuery(String query) {
    searchField().clear();
    searchField().sendKeys(query);    
  }

  public ResultsPage fetchQuery(String query) {
    setQuery(query);
    searchField().submit();
    return new ResultsPage(getDriver());
  }  

}