package io.mrs.web.components;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import io.mrs.web.components.Header;
import io.mrs.web.utils.LocatorType;

public class StandardHeader extends GlobalHeader implements Header {

	public StandardHeader(WebDriver driver) {
		super(driver);
	}

	  private Map<String, Pair<String, LocatorType>> fetchProperties() {
	    Map<String, Pair<String, LocatorType>> elementsMap = new HashMap<>(); 
	    elementsMap.put("header", Pair.of("place-holder", LocatorType.CSS));
	    elementsMap.put("welcome", Pair.of("place-holder", LocatorType.CSS));
	    
	    return elementsMap;
	  }

    
	/**
	 * Return the id of the root element for the header.
	 * 
	 * @return the id of the root element
	 */
	protected String getRootElementId() {
		return fetchElementPair(fetchProperties(), "query").getLeft();
	}

	public WebElement getStandardHeaderContainerElement(){
		return findElement(fetchElementPair(fetchProperties(), "footer"));
	}

}
