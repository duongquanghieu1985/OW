package com.fpt.ivs.akaAT.pages;

import java.util.LinkedHashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.ElementNotInteractableException;

import com.fpt.ivs.akaAT.WebBaseTestExtend;
import com.fpt.ivs.akaAT.data.Data;
import com.fpt.ivs.akaAT.data.SupportedData;

public class Home extends WebBaseTestExtend {
	private static final Logger LOGGER = LogManager.getLogger(Home.class);

	private static final String HOME_NAVIGATION = "OW.Home.nav-website";

	private static final String SEARCH_INPUT = "OW.Home.Search.Input";

	private static LinkedHashMap<String, String[]> inputMap = new LinkedHashMap<>();

	private static Home uniqueInstance = new Home();

	public static Home getUniqueInstance() {
		return uniqueInstance;
	}

	public Home() {
	}

	static {
		initializeInputMapping();
	}

	private static void initializeInputMapping() {
		inputMap.clear();
		inputMap.put(SupportedData.CITY, new String[] { "submitValue", SEARCH_INPUT });
	}

	public static Home getPage(WebBaseTestExtend driver) {
		LOGGER.info("Get Home page ready for the test");
		driver.waitPageLoad();
		driver.getPageSource();
		boolean isPageReady = driver.waitUntilDisplay(driver.getBy(HOME_NAVIGATION));
		return isPageReady ? new Home() : null;
	}

	public SearchResult performSearch(Data searchData) {
		LOGGER.info("Perform search with string");
		try {
			waitPageLoad();
			getPageSource();
			LOGGER.debug("Waiting for Search input box visible.");
			waitUntilDisplay(getBy(SEARCH_INPUT));
			LOGGER.debug("Submit value for text box.");
			setData(searchData, inputMap);
		} catch (ElementNotInteractableException e) {
			LOGGER.error("Unable to interract with Element due to ", e.getCause());
		} catch (Exception ex) {
			LOGGER.error("Error while perform search due to ", ex.getCause());
		}
		return SearchResult.getPage(this);
	}
}
