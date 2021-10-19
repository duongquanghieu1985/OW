package com.fpt.ivs.akaAT.keywords;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fpt.ivs.akaAT.data.Data;
import com.fpt.ivs.akaAT.data.SupportedData;
import com.fpt.ivs.akaAT.object.KeywordInfo;
import com.fpt.ivs.akaAT.pages.SearchResult;

public class SearchResultKeywords extends SearchResult {
	private static final Logger LOGGER = LogManager.getLogger(SearchResultKeywords.class);

	@KeywordInfo(Description = "[SEARCH RESULT SCREEN] Verifying search result contain city name.", Priority = 1)
	public void assertSearchResult(String data) {
		Data searchData = new Data(data);
		searchData.loadDataFromExcel();
		String stepDes = String.format("Verify [%s] appeared in search result.", searchData.getData(SupportedData.CITY));
		LOGGER.info(stepDes);
		Boolean result = assertSearchResult(searchData);
		if (result)
			reportPassed(stepDes);
		else
			reportFailed(stepDes, String.format("Unable to find [%s] in Search Result", searchData.getData(SupportedData.CITY)));
	}
}
