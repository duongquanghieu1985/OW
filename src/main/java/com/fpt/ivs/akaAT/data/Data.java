package com.fpt.ivs.akaAT.data;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Data extends AbstractData {
	private static final Logger LOGGER = LogManager.getLogger(Data.class);
	private static final Set<String> supportedProperties = SupportedData.getSupportedList();

	public Data() {
		super();
	}

	public Data(String dataName) {
		super(dataName);
	}

	public Data(Map<String, String> map) {
		for (Iterator<String> iterator = map.keySet().iterator(); iterator.hasNext();) {
			String key = iterator.next();
			if (supportedProperties.contains(key)) {
				dataMap.put(key, map.get(key));
			} else {
				LOGGER.error("Key [{}] is not supported", key);
			}
		}
	}

	public void loadDataFromExcel() {
		LOGGER.debug("Load data from Excel with data name [{}]", dataName);
		loadSupportedDataFromExcel(supportedProperties);
	}

	/**
	 * Set data
	 * 
	 * @param property
	 * @param value
	 * @author HieuDQ2
	 */
	public void setData(String property, String value) {
		setData(property, value, supportedProperties);
	}

	public Map<String, String> getJobDataHashmap() {
		HashMap<String, String> result = new HashMap<>();
		for (String item : supportedProperties) {
			result.put(item, dataMap.get(item));
		}
		return result;
	}
}