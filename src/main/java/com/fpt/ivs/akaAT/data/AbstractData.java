package com.fpt.ivs.akaAT.data;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.fpt.ivs.akaAT.dal.ExcelData;

/**
 * Abstract data class
 * 
 * @author HieuDQ2
 *
 */
public abstract class AbstractData {
	private static final String TEMPLATE = "%s - %s";
	private static final String NOT_REQUIRED = "Not Required";
	
	protected String dataName;
	protected Map<String, String> dataMap;

	/**
	 * Get data name
	 * 
	 * @return String
	 * @author HieuDQ2
	 */
	public String getDataName() {
		return dataName;
	}

	/**
	 * Get data map
	 * 
	 * @return Map<String, String>
	 * @author HieuDQ2
	 */
	public Map<String, String> getDataMap() {
		return dataMap;
	}

	/**
	 * Constructor with dataName
	 * 
	 * @param dataName
	 * @author HieuDQ2
	 */
	protected AbstractData(String dataName) {
		this.dataName = dataName;
		this.dataMap = new LinkedHashMap<>();
	}

	/**
	 * Default constructor
	 * 
	 * @author HieuDQ2
	 */
	protected AbstractData() {
		this.dataMap = new LinkedHashMap<>();
	}

	/**
	 * Get data value
	 * 
	 * @param dataName
	 * @param fieldName
	 * @return String if data exists. Or else, NULL is returned
	 * @author HieuDQ2
	 */
	protected String getExcelData(String fieldName) {
		String datafieldName = String.format(TEMPLATE, this.dataName, fieldName);
		String result = null;
		if (checkFieldNameExist(datafieldName)) {
			result = ExcelData.getCellText(datafieldName).trim();
			if (result.equalsIgnoreCase(NOT_REQUIRED))
				result = null;
		}
		return result;
	}

	/**
	 * Check if data field name exist
	 * 
	 * @param dataFieldName
	 * @return true / false
	 * @author HieuDQ2
	 */
	private boolean checkFieldNameExist(String dataFieldName) {
		return !ExcelData.getCellText(dataFieldName).isEmpty();
	}

	/**
	 * Get data
	 * 
	 * @param property
	 * @return String
	 * @author HieuDQ2
	 */
	public String getData(String property) {
		return this.dataMap.get(property);
	}

	/**
	 * Set data
	 * 
	 * @param property
	 * @param value
	 * @param supportedProperties
	 * @author HieuDQ2
	 */
	protected void setData(String property, String value, Set<String> supportedProperties) {
		if (supportedProperties.contains(property)) {
			dataMap.put(property, value);
		} else {
			throw new UnsupportedOperationException(String.format("Invalid property [%s]", property));
		}
	}

	/**
	 * Load supported data from Excel
	 * 
	 * @param supportedProperties
	 * @author HieuDQ2
	 */
	protected void loadSupportedDataFromExcel(Set<String> supportedProperties) {
		for (String property : supportedProperties) {
			dataMap.put(property, getExcelData(property));
		}
	}
}