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
 * InvisibleFooter is responsible for modeling the invisible footer that 'appears' at the bottom of some pages.
 */
public class InvisibleFooter extends Component implements Footer {

	private final WebElement rootElement;

	public InvisibleFooter(WebDriver driver) {
		super(driver);
		rootElement = findElement(fetchElementPair(fetchProperties(), "footer"));
	}

	  private Map<String, Pair<String, LocatorType>> fetchProperties() {
	    Map<String, Pair<String, LocatorType>> elementsMap = new HashMap<>(); 
	    elementsMap.put("footer", Pair.of("place-holder", LocatorType.CSS));
	    
	    return elementsMap;
	  }


    
	public WebElement getRootElement(){
		return rootElement;
	}

}
