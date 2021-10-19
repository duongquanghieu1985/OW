package com.fpt.ivs.akaAT.keywords;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fpt.ivs.akaAT.common.Global;
import com.fpt.ivs.akaAT.data.Data;
import com.fpt.ivs.akaAT.data.SupportedData;
import com.fpt.ivs.akaAT.object.KeywordInfo;
import com.fpt.ivs.akaAT.pages.Home;
import com.fpt.ivs.akaAT.pages.SearchResult;

public class HomeKeywords extends Home {
	private static final Logger LOGGER = LogManager.getLogger(HomeKeywords.class);
	
	@KeywordInfo(Description = "[HOME SCREEN] Perform search with given city name.", Priority = 1)
	public void searchCity(String data) {
		Data searchData = new Data(data);
		searchData.loadDataFromExcel();		
		String stepDes = String.format("Perform search weather in [%s]", searchData.getData(SupportedData.CITY));
		LOGGER.info(stepDes);
		navigate(Global.UAT_PATH);
		Home home = Home.getPage(this);		
		SearchResult searchResult = home.performSearch(searchData);
		if (searchResult != null)
			reportPassed(stepDes);
		else
			reportFailed(stepDes, "Failed to search city with data set");
	}
}
