package com.fpt.ivs.akaAT.pages;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotVisibleException;

import com.fpt.ivs.akaAT.WebBaseTestExtend;
import com.fpt.ivs.akaAT.common.Global;
import com.fpt.ivs.akaAT.data.Data;
import com.fpt.ivs.akaAT.data.SupportedData;

public class SearchResult extends WebBaseTestExtend {
	private static final Logger LOGGER = LogManager.getLogger(SearchResult.class);

	private static final String RESULT_LIST = "OW.SearchResult.List";

	private static final String SEARCH_RESULT = "OW.SearchResult.List.City";

	public SearchResult() {
	}

	public static SearchResult getPage(WebBaseTestExtend driver) {
		LOGGER.info("Get search result page ready for the test");
		driver.waitPageLoad();
		driver.getPageSource();
		boolean isObjectDisplayed = driver.waitUntilDisplay(driver.getBy(RESULT_LIST));
		return isObjectDisplayed ? new SearchResult() : null;
	}

	public boolean assertSearchResult(Data data) {
		LOGGER.info("Verify search result contains correct city");
		boolean result = false;
		try {
			waitPageReady(Global.UAT_OBJECT_TIMEOUT);
			String cityName = data.getData(SupportedData.CITY);
			String xpath = getPathWithParams(Global.propObjRepo.getProperty(SEARCH_RESULT + ".XPath"));
			By objPath = By.xpath(String.format(xpath, cityName));
			if (getElement(objPath) != null)
				result = true;
		} catch (ElementNotVisibleException e) {
			LOGGER.error("Unable to find selected element.");
		}
		return result;
	}

}
