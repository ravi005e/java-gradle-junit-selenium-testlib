package io.mrs.web.components;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import io.mrs.web.components.Component;
import io.mrs.web.components.Footer;
import io.mrs.web.utils.LocatorType;

/**
 * StandardFooter is responsible for implementing the standard footer
 */
public class StandardFooter extends Component implements Footer {

	public StandardFooter(WebDriver driver) {
		super(driver);
	}
	
	  private Map<String, Pair<String, LocatorType>> fetchProperties() {
	    Map<String, Pair<String, LocatorType>> elementsMap = new HashMap<>(); 
	    elementsMap.put("footer", Pair.of("place-holder", LocatorType.CSS));
	    
	    return elementsMap;
	  }

    
	public WebElement getRootElement() {
		return findElement(fetchElementPair(fetchProperties(), "footer"));
	}

}
