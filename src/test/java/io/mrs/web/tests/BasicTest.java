package io.mrs.web.tests;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import io.mrs.web.WebBaseTest;
import io.mrs.web.pages.GooglePage;
import io.mrs.web.pages.ResultsPage;

public class BasicTest extends WebBaseTest {
	private GooglePage landingPage() throws Exception {
		getDriver().get(System.getProperty("baseurl"));
		return new GooglePage(getDriver());
	}
	
	@Test
	public void googleCheeseExample() throws Exception {
		String searchString = "Cheese!";
		GooglePage google = landingPage();
		ResultsPage results = google.fetchQuery(searchString);
	    assertThrows(Exception.class, () -> {
            results.getDriver().getTitle().contentEquals(searchString+"0000");
        });
//		results.validateQuery(searchString+"0000");
	}

	@Test
	public void googleMilkExample() throws Exception {
		String searchString = "Milk!";
		GooglePage google = landingPage();
		ResultsPage results = google.fetchQuery(searchString);
		results.validateQuery(searchString);
	}
}
