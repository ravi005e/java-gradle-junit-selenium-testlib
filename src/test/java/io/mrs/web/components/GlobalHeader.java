package io.mrs.web.components;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import io.mrs.web.components.Component;
import io.mrs.web.utils.LocatorType;

/**
 * GlobalHeader models the global header and provides a convenient extension point.
 */
public abstract class GlobalHeader extends Component {

  public GlobalHeader(WebDriver driver) {
    super(driver);
  }

  private Map<String, Pair<String, LocatorType>> fetchProperties() {
    Map<String, Pair<String, LocatorType>> elementsMap = new HashMap<>(); 
    elementsMap.put("header", Pair.of("place-holder", LocatorType.CSS));
    elementsMap.put("welcome", Pair.of("place-holder", LocatorType.CSS));
    
    return elementsMap;
  }

  /**
   * Get the root element of the header.
   * @return
   */
  public WebElement getRootElement() {
    return findElement(fetchElementPair(fetchProperties(), "header"));
  }

  /**
   * Return the id of the root element for the header.
   * @return
   */
  protected abstract String getRootElementId();


  private WebElement getWelcomeMesage() {
    return findElement(fetchElementPair(fetchProperties(), "welcome"));
  }

  public String getWelcomeText() {
    return getWelcomeMesage().getText();
  }

}
