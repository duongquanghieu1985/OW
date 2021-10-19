package com.fpt.ivs.akaAT.pages.objects;

import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebElement;

/**
 * @author HieuDQ2
 *
 */
public abstract class AbstractDataAccessableObject extends AbstractObject {
	private static final Logger LOGGER = LogManager.getLogger(AbstractDataAccessableObject.class);

	protected Map<String, List<String>> inputMap;
	protected Map<String, List<String>> verifyMap;

	/**
	 * Default constructor
	 * 
	 * @param pageName
	 * @param pageElement
	 * @author HieuDQ2
	 */
	protected AbstractDataAccessableObject(String pageName, WebElement pageElement) {
		super(pageName, pageElement);
	}

	/**
	 * Set data using Map
	 * 
	 * @param data
	 * @return boolean
	 * @author HieuDQ2
	 */
	public boolean setData(Map<String, String> data) {
		String stepDes = String.format("Set data for [%s]", objectName);
		LOGGER.debug(stepDes);
		boolean result = true;
		for (String field : data.keySet()) {
			if (!inputMap.containsKey(field)) {
				String errorMessage = String.format("Input map does not support for field [%s]", field);
				LOGGER.error(errorMessage);
				reportFailed(stepDes, errorMessage);
				result = false;
				continue;
			}
			String value = data.get(field);
			if (value == null)
				continue;
			stepDes = String.format("Set [%s] = [%s]", field, value);
			try {
				result &= invokeInputMethod(value, field, inputMap.get(field));
			} catch (Exception e) {
				String errorMessage = String.format("%s get error: %s", stepDes, e.toString());
				LOGGER.error(errorMessage);
				reportFailed(stepDes, errorMessage);
				result = false;
			}
		}
		return result;
	}

	/**
	 * Verify data using Map
	 * 
	 * @param data
	 * @return boolean
	 * @author HieuDQ2
	 */
	public boolean verifyData(Map<String, String> data) {
		String stepDes = String.format("Verify data for [%s]", objectName);
		LOGGER.debug(stepDes);
		boolean result = true;
		for (String field : data.keySet()) {
			if (!verifyMap.containsKey(field)) {
				String errorMessage = String.format("Verify map does not support for field [%s]", field);
				LOGGER.error(errorMessage);
				reportFailed(stepDes, errorMessage);
				result = false;
				continue;
			}
			String value = data.get(field);
			if (value == null)
				continue;
			stepDes = String.format("Verify [%s] = [%s]", field, value);
			try {
				result &= invokeVerifyMethod(value, field, verifyMap.get(field));
			} catch (Exception e) {
				String errorMessage = String.format("%s get error: %s", stepDes, e.toString());
				LOGGER.error(errorMessage);
				reportFailed(stepDes, errorMessage);
				result = false;
			}
		}
		return result;
	}

	/**
	 * Invoke input methods
	 * 
	 * @param value
	 * @param field
	 * @param args
	 * @return boolean
	 * @author HieuDQ2
	 */
	protected boolean invokeInputMethod(String value, String field, List<String> args) throws Exception {
		return defaultInvoke(value, field, args);
	}

	/**
	 * Invoke verify methods
	 * 
	 * @param value
	 * @param field
	 * @param args
	 * @return boolean
	 * @throws Exception
	 * @author HieuDQ2
	 */
	protected boolean invokeVerifyMethod(String value, String field, List<String> args) throws Exception {
		return defaultInvoke(value, field, args);
	}

	/**
	 * Default invoke
	 * 
	 * @param value
	 * @param field
	 * @param args
	 * @return boolean
	 * @throws Exception
	 * @author HieuDQ2
	 */
	protected abstract boolean defaultInvoke(String value, String field, List<String> args) throws Exception;
}