package io.mrs.web.components;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;

import io.mrs.web.components.BasePage;

//import com.mrs.web.components.BasePage;

/**
 * StandardPage is responsible for modeling standard pages, having both a header and a footer.
 */
public abstract class StandardPage extends BasePage {
	private final StandardHeader header;
	private final StandardFooter footer;

	/**
	 * {@inheritDoc}
	 */
	public StandardPage(WebDriver driver) {
		this(driver, null);
	}

	/**
	 * {@inheritDoc}
	 */
	public StandardPage(WebDriver driver, ExpectedCondition<Boolean> isPageLoadedCondition){
		super(driver, isPageLoadedCondition);
		this.header = new StandardHeader(driver);
		this.footer = new StandardFooter(driver);

	}

	public StandardHeader getHeader() {
		return header;
	}

	public StandardFooter getFooter() {
		return footer;
	}
}

