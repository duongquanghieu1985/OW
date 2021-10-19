package com.fpt.ivs.akaAT;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.Seconds;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.html5.WebStorage;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Wait;

import com.fpt.ivs.akaAT.aut.web.WebBaseTest;
import com.fpt.ivs.akaAT.common.Global;
import com.fpt.ivs.akaAT.common.Message;
import com.fpt.ivs.akaAT.data.Data;
import com.fpt.ivs.akaAT.ui.object.TestResult;
import com.fpt.ivs.akaAT.variable.Clipboard;
import com.google.common.collect.Ordering;

public class WebBaseTestExtend extends WebBaseTest {
	private static final Logger LOGGER = LogManager.getLogger(WebBaseTestExtend.class);
	private static final String CLASS_ATTRIBUTE = "class";
	private static final String PASSWORD = "password";
	private static final String TIMEOUT_REACHED = "TIMEOUT reached";
	private static final String RPT_UNEXPECTED_ERROR = "RPT_UNEXPECTED_ERROR";
	private static final String INDEX = "index";
	public static final int ZERO_WAIT_TIME = 0;
	public static final int SHORT_WAIT_TIME = 1;
	public static final int MAX_STATE_RETRY = 5;
	protected static final int UPLOAD_TIME_OUT = Global.UAT_OBJECT_TIMEOUT * 20;

	/**
	 * Get current URL
	 * 
	 * @return String: Current URL
	 * @author HieuDQ2
	 */
	public String getUrl() {
		waitPageLoad();
		return Global.webDriver.getCurrentUrl();
	}

	/**
	 * Set browser wait time to 0 (zero)
	 * 
	 * @author HaiNV12
	 * 
	 */
	public static void setBrowserNoWait() {
		Global.webDriver.manage().timeouts().implicitlyWait(ZERO_WAIT_TIME, TimeUnit.SECONDS);
	}

	/**
	 * Set browser wait time to Default value (Global.UAT_OBJECT_TIMEOUT)
	 * 
	 * @author HaiNV12
	 */
	public static void setBrowserWaitDefault() {
		Global.webDriver.manage().timeouts().implicitlyWait((long) Global.UAT_OBJECT_TIMEOUT, TimeUnit.SECONDS);
	}

	/**
	 * Set browser wait time to specific value
	 * 
	 * @author TuanLA47
	 */
	public static void setBrowserWait(int timeout) {
		Global.webDriver.manage().timeouts().implicitlyWait((long) timeout, TimeUnit.SECONDS);
	}

	/**
	 * Get Text value of Object path
	 * 
	 * @param objPath Object path
	 * @return String
	 * @author HieuDQ2
	 */
	@Override
	public String getText(String objPath) {
		String textValue = "";
		try {
			if (getElement(objPath)) {
				WebElement webElement = getElement(getBy(objPath));
				String tagName = webElement.getTagName();
				switch (tagName) {
				case "input":
				case "textarea":
					textValue = webElement.getAttribute("value");
					break;
				case "select":
					textValue = getListSelectedItem(webElement);
					break;
				default:
					textValue = webElement.getText();
					break;
				}
			}
		} catch (Exception e) {
			LOGGER.error(Message.format(RPT_UNEXPECTED_ERROR, e.getMessage()));
			reportFailed(Message.format("RPT_GET_TEXT", objPath),
					Message.format("RPT_NOT_GET_TEXT", objPath, textValue));
		}
		return textValue;
	}

	/**
	 * Get Text value of Object path then compound to Unicode
	 * 
	 * @param objPath Object path
	 * @return String
	 * @author HieuDQ2
	 */

	public String getTextCompound2Unicode(String objPath) {
		String textValue = "";
		try {
			if (getElement(objPath)) {
				WebElement webElement = getElement(getBy(objPath));
				String tagName = webElement.getTagName();
				switch (tagName) {
				case "input":
				case "textarea":
					textValue = webElement.getAttribute("value");
					break;
				case "select":
					textValue = getListSelectedItem(webElement);
					break;
				default:
					textValue = webElement.getText();
					break;
				}
			}
		} catch (Exception e) {
			LOGGER.error(Message.format(RPT_UNEXPECTED_ERROR, e.getMessage()));
			reportFailed(Message.format("RPT_GET_TEXT", objPath),
					Message.format("RPT_NOT_GET_TEXT", objPath, textValue));
		}
		return compound2Unicode(textValue);
	}

	/**
	 * Get raw text without trimmed from WebElement
	 * 
	 * @param element
	 * @return String
	 * @author HieuDQ2
	 */
	public String getRawText(WebElement element) {
		return (String) ((JavascriptExecutor) Global.webDriver).executeScript("return arguments[0].textContent;",
				element);
	}

	/**
	 * Convert Unicode [Tổ Hợp] sang [Dựng Sẵn]
	 * 
	 * @param sConvert - String
	 * @author HieuDQ2
	 */
	private static String compound2Unicode(String sConvert) {
		sConvert = sConvert.replaceAll("\u0065\u0309", "\u1EBB"); // ẻ
		sConvert = sConvert.replaceAll("\u0065\u0301", "\u00E9"); // é
		sConvert = sConvert.replaceAll("\u0065\u0300", "\u00E8"); // è
		sConvert = sConvert.replaceAll("\u0065\u0323", "\u1EB9"); // ẹ
		sConvert = sConvert.replaceAll("\u0065\u0303", "\u1EBD"); // ẽ
		sConvert = sConvert.replaceAll("\u00EA\u0309", "\u1EC3"); // ể
		sConvert = sConvert.replaceAll("\u00EA\u0301", "\u1EBF"); // ế
		sConvert = sConvert.replaceAll("\u00EA\u0300", "\u1EC1"); // ề
		sConvert = sConvert.replaceAll("\u00EA\u0323", "\u1EC7"); // ệ
		sConvert = sConvert.replaceAll("\u00EA\u0303", "\u1EC5"); // ễ
		sConvert = sConvert.replaceAll("\u0079\u0309", "\u1EF7"); // ỷ
		sConvert = sConvert.replaceAll("\u0079\u0301", "\u00FD"); // ý
		sConvert = sConvert.replaceAll("\u0079\u0300", "\u1EF3"); // ỳ
		sConvert = sConvert.replaceAll("\u0079\u0323", "\u1EF5"); // ỵ
		sConvert = sConvert.replaceAll("\u0079\u0303", "\u1EF9"); // ỹ
		sConvert = sConvert.replaceAll("\u0075\u0309", "\u1EE7"); // ủ
		sConvert = sConvert.replaceAll("\u0075\u0301", "\u00FA"); // ú
		sConvert = sConvert.replaceAll("\u0075\u0300", "\u00F9"); // ù
		sConvert = sConvert.replaceAll("\u0075\u0323", "\u1EE5"); // ụ
		sConvert = sConvert.replaceAll("\u0075\u0303", "\u0169"); // ũ
		sConvert = sConvert.replaceAll("\u01B0\u0309", "\u1EED"); // ử
		sConvert = sConvert.replaceAll("\u01B0\u0301", "\u1EE9"); // ứ
		sConvert = sConvert.replaceAll("\u01B0\u0300", "\u1EEB"); // ừ
		sConvert = sConvert.replaceAll("\u01B0\u0323", "\u1EF1"); // ự
		sConvert = sConvert.replaceAll("\u01B0\u0303", "\u1EEF"); // ữ
		sConvert = sConvert.replaceAll("\u0069\u0309", "\u1EC9"); // ỉ
		sConvert = sConvert.replaceAll("\u0069\u0301", "\u00ED"); // í
		sConvert = sConvert.replaceAll("\u0069\u0300", "\u00EC"); // ì
		sConvert = sConvert.replaceAll("\u0069\u0323", "\u1ECB"); // ị
		sConvert = sConvert.replaceAll("\u0069\u0303", "\u0129"); // ĩ
		sConvert = sConvert.replaceAll("\u006F\u0309", "\u1ECF"); // ỏ
		sConvert = sConvert.replaceAll("\u006F\u0301", "\u00F3"); // ó
		sConvert = sConvert.replaceAll("\u006F\u0300", "\u00F2"); // ò
		sConvert = sConvert.replaceAll("\u006F\u0323", "\u1ECD"); // ọ
		sConvert = sConvert.replaceAll("\u006F\u0303", "\u00F5"); // õ
		sConvert = sConvert.replaceAll("\u01A1\u0309", "\u1EDF"); // ở
		sConvert = sConvert.replaceAll("\u01A1\u0301", "\u1EDB"); // ớ
		sConvert = sConvert.replaceAll("\u01A1\u0300", "\u1EDD"); // ờ
		sConvert = sConvert.replaceAll("\u01A1\u0323", "\u1EE3"); // ợ
		sConvert = sConvert.replaceAll("\u01A1\u0303", "\u1EE1"); // ỡ
		sConvert = sConvert.replaceAll("\u00F4\u0309", "\u1ED5"); // ổ
		sConvert = sConvert.replaceAll("\u00F4\u0301", "\u1ED1"); // ố
		sConvert = sConvert.replaceAll("\u00F4\u0300", "\u1ED3"); // ồ
		sConvert = sConvert.replaceAll("\u00F4\u0323", "\u1ED9"); // ộ
		sConvert = sConvert.replaceAll("\u00F4\u0303", "\u1ED7"); // ỗ
		sConvert = sConvert.replaceAll("\u0061\u0309", "\u1EA3"); // ả
		sConvert = sConvert.replaceAll("\u0061\u0301", "\u00E1"); // á
		sConvert = sConvert.replaceAll("\u0061\u0300", "\u00E0"); // à
		sConvert = sConvert.replaceAll("\u0061\u0323", "\u1EA1"); // ạ
		sConvert = sConvert.replaceAll("\u0061\u0303", "\u00E3"); // ã
		sConvert = sConvert.replaceAll("\u0103\u0309", "\u1EB3"); // ẳ
		sConvert = sConvert.replaceAll("\u0103\u0301", "\u1EAF"); // ắ
		sConvert = sConvert.replaceAll("\u0103\u0300", "\u1EB1"); // ằ
		sConvert = sConvert.replaceAll("\u0103\u0323", "\u1EB7"); // ặ
		sConvert = sConvert.replaceAll("\u0103\u0303", "\u1EB5"); // ẵ
		sConvert = sConvert.replaceAll("\u00E2\u0309", "\u1EA9"); // ẩ
		sConvert = sConvert.replaceAll("\u00E2\u0301", "\u1EA5"); // ấ
		sConvert = sConvert.replaceAll("\u00E2\u0300", "\u1EA7"); // ầ
		sConvert = sConvert.replaceAll("\u00E2\u0323", "\u1EAD"); // ậ
		sConvert = sConvert.replaceAll("\u00E2\u0303", "\u1EAB"); // ẫ
		sConvert = sConvert.replaceAll("\u0045\u0309", "\u1EBA"); // Ẻ
		sConvert = sConvert.replaceAll("\u0045\u0301", "\u00C9"); // É
		sConvert = sConvert.replaceAll("\u0045\u0300", "\u00C8"); // È
		sConvert = sConvert.replaceAll("\u0045\u0323", "\u1EB8"); // Ẹ
		sConvert = sConvert.replaceAll("\u0045\u0303", "\u1EBC"); // Ẽ
		sConvert = sConvert.replaceAll("\u00CA\u0309", "\u1EC2"); // Ể
		sConvert = sConvert.replaceAll("\u00CA\u0301", "\u1EBE"); // Ế
		sConvert = sConvert.replaceAll("\u00CA\u0300", "\u1EC0"); // Ề
		sConvert = sConvert.replaceAll("\u00CA\u0323", "\u1EC6"); // Ệ
		sConvert = sConvert.replaceAll("\u00CA\u0303", "\u1EC4"); // Ễ
		sConvert = sConvert.replaceAll("\u0059\u0309", "\u1EF6"); // Ỷ
		sConvert = sConvert.replaceAll("\u0059\u0301", "\u00DD"); // Ý
		sConvert = sConvert.replaceAll("\u0059\u0300", "\u1EF2"); // Ỳ
		sConvert = sConvert.replaceAll("\u0059\u0323", "\u1EF4"); // Ỵ
		sConvert = sConvert.replaceAll("\u0059\u0303", "\u1EF8"); // Ỹ
		sConvert = sConvert.replaceAll("\u0055\u0309", "\u1EE6"); // Ủ
		sConvert = sConvert.replaceAll("\u0055\u0301", "\u00DA"); // Ú
		sConvert = sConvert.replaceAll("\u0055\u0300", "\u00D9"); // Ù
		sConvert = sConvert.replaceAll("\u0055\u0323", "\u1EE4"); // Ụ
		sConvert = sConvert.replaceAll("\u0055\u0303", "\u0168"); // Ũ
		sConvert = sConvert.replaceAll("\u01AF\u0309", "\u1EEC"); // Ử
		sConvert = sConvert.replaceAll("\u01AF\u0301", "\u1EE8"); // Ứ
		sConvert = sConvert.replaceAll("\u01AF\u0300", "\u1EEA"); // Ừ
		sConvert = sConvert.replaceAll("\u01AF\u0323", "\u1EF0"); // Ự
		sConvert = sConvert.replaceAll("\u01AF\u0303", "\u1EEE"); // Ữ
		sConvert = sConvert.replaceAll("\u0049\u0309", "\u1EC8"); // Ỉ
		sConvert = sConvert.replaceAll("\u0049\u0301", "\u00CD"); // Í
		sConvert = sConvert.replaceAll("\u0049\u0300", "\u00CC"); // Ì
		sConvert = sConvert.replaceAll("\u0049\u0323", "\u1ECA"); // Ị
		sConvert = sConvert.replaceAll("\u0049\u0303", "\u0128"); // Ĩ
		sConvert = sConvert.replaceAll("\u004F\u0309", "\u1ECE"); // Ỏ
		sConvert = sConvert.replaceAll("\u004F\u0301", "\u00D3"); // Ó
		sConvert = sConvert.replaceAll("\u004F\u0300", "\u00D2"); // Ò
		sConvert = sConvert.replaceAll("\u004F\u0323", "\u1ECC"); // Ọ
		sConvert = sConvert.replaceAll("\u004F\u0303", "\u00D5"); // Õ
		sConvert = sConvert.replaceAll("\u01A0\u0309", "\u1EDE"); // Ở
		sConvert = sConvert.replaceAll("\u01A0\u0301", "\u1EDA"); // Ớ
		sConvert = sConvert.replaceAll("\u01A0\u0300", "\u1EDC"); // Ờ
		sConvert = sConvert.replaceAll("\u01A0\u0323", "\u1EE2"); // Ợ
		sConvert = sConvert.replaceAll("\u01A0\u0303", "\u1EE0"); // Ỡ
		sConvert = sConvert.replaceAll("\u00D4\u0309", "\u1ED4"); // Ổ
		sConvert = sConvert.replaceAll("\u00D4\u0301", "\u1ED0"); // Ố
		sConvert = sConvert.replaceAll("\u00D4\u0300", "\u1ED2"); // Ồ
		sConvert = sConvert.replaceAll("\u00D4\u0323", "\u1ED8"); // Ộ
		sConvert = sConvert.replaceAll("\u00D4\u0303", "\u1ED6"); // Ỗ
		sConvert = sConvert.replaceAll("\u0041\u0309", "\u1EA2"); // Ả
		sConvert = sConvert.replaceAll("\u0041\u0301", "\u00C1"); // Á
		sConvert = sConvert.replaceAll("\u0041\u0300", "\u00C0"); // À
		sConvert = sConvert.replaceAll("\u0041\u0323", "\u1EA0"); // Ạ
		sConvert = sConvert.replaceAll("\u0041\u0303", "\u00C3"); // Ã
		sConvert = sConvert.replaceAll("\u0102\u0309", "\u1EB2"); // Ẳ
		sConvert = sConvert.replaceAll("\u0102\u0301", "\u1EAE"); // Ắ
		sConvert = sConvert.replaceAll("\u0102\u0300", "\u1EB0"); // Ằ
		sConvert = sConvert.replaceAll("\u0102\u0323", "\u1EB6"); // Ặ
		sConvert = sConvert.replaceAll("\u0102\u0303", "\u1EB4"); // Ẵ
		sConvert = sConvert.replaceAll("\u00C2\u0309", "\u1EA8"); // Ẩ
		sConvert = sConvert.replaceAll("\u00C2\u0301", "\u1EA4"); // Ấ
		sConvert = sConvert.replaceAll("\u00C2\u0300", "\u1EA6"); // Ầ
		sConvert = sConvert.replaceAll("\u00C2\u0323", "\u1EAC"); // Ậ
		sConvert = sConvert.replaceAll("\u00C2\u0303", "\u1EAA"); // Ẫ
		return sConvert;
	}

	/**
	 * Compare 2 list without order
	 * 
	 * @param actList
	 * @param expList
	 * @return true / false
	 * @author HieuDQ2
	 */
	protected <T extends Comparable<T>> boolean compareTwoListWithoutOrder(List<T> actList, List<T> expList) {
		LOGGER.debug("Compare 2 list without order");
		Collections.sort(actList);
		Collections.sort(expList);
		return compareTwoList(actList, expList);
	}

	/**
	 * Compare 2 list with order
	 * 
	 * @param actList
	 * @param expList
	 * @return true / false
	 * @author HieuDQ2
	 */
	protected <T extends Comparable<T>> boolean compareTwoListWithOrder(List<T> actList, List<T> expList) {
		LOGGER.debug("Compare 2 lists with order");
		return compareTwoList(actList, expList);
	}

	/**
	 * Compare two list without sorting
	 * 
	 * @param actList
	 * @param expList
	 * @return true / false
	 * @author HieuDQ2
	 */
	private <T extends Comparable<T>> boolean compareTwoList(List<T> actList, List<T> expList) {
		boolean result = Arrays.equals(actList.toArray(), expList.toArray());
		if (!result) {
			String actualList = getJoinedStringOfList(actList);
			String expectList = getJoinedStringOfList(expList);
			String errorMessage = String.format("Actual list: %s%nResult list: %s", actualList, expectList);
			LOGGER.error(errorMessage);
			reportFailed("Compare 2 list", errorMessage);
		}
		return result;
	}

	/**
	 * Get the String represent for the list
	 * 
	 * @param list
	 * @return String
	 * @author HieuDQ2
	 */
	private <T extends Object> String getJoinedStringOfList(List<T> list) {
		StringBuilder stringBuilder = new StringBuilder();
		if (list != null && !list.isEmpty()) {
			for (T item : list) {
				stringBuilder.append(String.format("%s; ", item.toString()).trim());
			}
		}
		return stringBuilder.toString();
	}

	/**
	 * Get page source
	 * 
	 * @return String
	 * @author HieuDQ2
	 */
	public String getPageSource() {
		return Global.webDriver.getPageSource();
	}

	/**
	 * Select item in Select object by value
	 * 
	 * @param objPath
	 * @param value
	 * @author HieuDQ2
	 */
	public boolean selectDropdownListByValue(String objPath, String value) {
		boolean result = false;
		String stepDes = String.format("Select by value [%s] = [%s]", objPath, value);
		LOGGER.info(stepDes);
		try {
			Select select = new Select(getElement(getBy(objPath)));
			select.selectByValue(value);
			reportPassed(stepDes);
			result = true;
		} catch (NoSuchElementException e) {
			reportFailed(stepDes, String.format("No item has value [%s]", value));
			result = false;
		}
		return result;
	}

	/**
	 * Select item in Select object by value
	 * 
	 * @param objBy
	 * @param value
	 * @author TuanLA47
	 */
	public boolean selectDropdownListByValue(By objBy, String value) {
		boolean result = false;
		String stepDes = String.format("Select by value [%s] = [%s]", objBy, value);
		LOGGER.info(stepDes);
		try {
			Select select = new Select(getElement(objBy));
			select.selectByValue(value);
			reportPassed(stepDes);
			result = true;
		} catch (NoSuchElementException e) {
			reportFailed(stepDes, String.format("No item has value [%s]", value));
			result = false;
		}
		return result;
	}

	/**
	 * Set value
	 * 
	 * @author HieuDQ2
	 */
	@Override
	public boolean setValue(String objPath, String value) {
		String stepDes = Message.format("RPT_SET_TEXT", value, objPath);
		boolean result = setValue(getElement(getBy(objPath)), value);
		if (result) {
			if (!objPath.toLowerCase().contains(PASSWORD))
				reportPassed(stepDes);
		} else {
			if (!objPath.toLowerCase().contains(PASSWORD)) {
				reportFailed(stepDes, Message.format("RPT_NOT_SET_TEXT", value, objPath));
			}
		}
		return result;
	}

	/**
	 * Submit value
	 * 
	 * @author HieuDQ2
	 */
	public boolean submitValue(String objPath, String value) {
		String stepDes = Message.format("RPT_SET_TEXT", value, objPath);
		boolean result = submitValue(getElement(getBy(objPath)), value);
		if (result)
			reportPassed(stepDes);
		else
			reportFailed(stepDes, Message.format("RPT_NOT_SET_TEXT", value, objPath));
		return result;
	}

	/**
	 * Set value by By object
	 * 
	 * @param by
	 * @param value
	 * @return true / false
	 * @author HieuDQ2
	 */
	public boolean setValue(By by, String value) {
		return setValue(getElement(by), value);
	}

	/**
	 * Set value
	 * 
	 * @param element
	 * @param value
	 * @return true / false
	 * @author HieuDQ2
	 */
	public boolean setValue(WebElement element, String value) {
		boolean result = false;
		try {
			element.click();
			element.sendKeys(Keys.CONTROL + "a");
			element.sendKeys(Keys.DELETE);
			wait(SHORT_WAIT_TIME);
			element.sendKeys(value);
			wait(SHORT_WAIT_TIME);
			result = true;
		} catch (Exception e) {
			result = false;
			LOGGER.error(e.getMessage());
		}
		return result;
	}

	/**
	 * Submit value
	 * 
	 * @param element
	 * @param value
	 * @return true / false
	 * @author HieuDQ2
	 */
	public boolean submitValue(WebElement element, String value) {
		boolean result = false;
		try {
			waitPageReady(Global.UAT_OBJECT_TIMEOUT);
			element.sendKeys(value);
			element.submit();
			result = true;
		} catch (Exception e) {
			result = false;
			LOGGER.error(e.getMessage());
		}
		return result;
	}

	/**
	 * Paste value
	 * 
	 * @param element
	 * @param value
	 * @return true / false
	 * @author AnhNTV27
	 */
	public boolean pasteValue(String objPath, String value) {
		boolean result = false;
		WebElement element = getElement(getBy(objPath));
		Clipboard.setValue(value);
		try {
			element.click();
			element.sendKeys(Keys.CONTROL + "a");
			element.sendKeys(Keys.DELETE);
			element.sendKeys(Keys.HOME);
			wait(SHORT_WAIT_TIME / 2);
			element.sendKeys(Keys.CONTROL + "v");
			wait(SHORT_WAIT_TIME / 2);
			result = true;
		} catch (Exception e) {
			result = false;
			LOGGER.error(e.getMessage());
		}
		return result;
	}

	/**
	 * Paste value
	 * 
	 * @param element
	 * @param value
	 * @return true / false
	 * @author AnhNTV27
	 */
	public boolean pasteValue(By objBy, String value) {
		boolean result = false;
		WebElement element = getElement(objBy);
		Clipboard.setValue(value);
		try {
			element.click();
			element.sendKeys(Keys.CONTROL + "a");
			element.sendKeys(Keys.DELETE);
			element.sendKeys(Keys.HOME);
			wait(SHORT_WAIT_TIME / 2);
			element.sendKeys(Keys.CONTROL + "v");
			wait(SHORT_WAIT_TIME / 2);
			result = true;
		} catch (Exception e) {
			result = false;
			LOGGER.error(e.getMessage());
		}
		return result;
	}

	/**
	 * Upload File by using SendKeys
	 * 
	 * @author TuanLA47
	 */
	public boolean uploadFile(String objPath, String value) {
		WebElement element = getElement(getBy(objPath));
		if (element.isEnabled()) {
			element.sendKeys(value);
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Method to filter the index inside input
	 * 
	 * @param input
	 * @return number inside Index, example: index:1 or index:2
	 */
	public int filterIndex(String input) {
		LOGGER.debug("Filter out index");
		int result = -1;
		if (input.toLowerCase().contains(INDEX)) {
			result = Integer.parseInt(input.replaceAll("[\\D]", ""));
		} else {
			LOGGER.error("INVALID format [{}]", input);
		}
		return result;
	}

	/**
	 * This click method is used for some element which is notified "Element is not
	 * clickable"
	 * 
	 * @param objPath object to click on
	 * @return true/false
	 * @author ChanhPQ
	 * @author HieuDQ2
	 */
	private <T extends Object> boolean clickExtend(T objPath) {
		boolean result = false;
		try {
			Actions actions = new Actions(Global.webDriver);
			if (objPath instanceof WebElement) {
				actions.moveToElement((WebElement) objPath).click().perform();
				wait(SHORT_WAIT_TIME);
			} else {
				actions.moveToElement(Global.webDriver.findElement((By) getInputBy(objPath))).click().perform();
				wait(SHORT_WAIT_TIME);
			}
			result = true;
		} catch (Exception e) {
			reportFailed("Click extend", e.toString());
			result = false;
		}
		return result;
	}

	/**
	 * Get Object repository name of Object
	 * 
	 * @param objPath
	 * @return String
	 * @author HieuDQ2
	 */
	protected String getObjectXPath(Object obj) {
		String objPathClassName = obj.getClass().getSimpleName();
		String content = obj.toString();
		String result = null;
		if (objPathClassName.equals(RemoteWebElement.class.getSimpleName())
				|| objPathClassName.equals(WebElement.class.getSimpleName())) {
			result = (content.contains("-> xpath:") ? content.split("-> xpath:")[1].trim()
					: content.split("-> id:")[1].trim());
			result = result.substring(0, result.length() - 1);
		} else {
			result = content;
		}
		return result;
	}

	/**
	 * Rewrite click method using either string input or By input, already included
	 * reporting.
	 * 
	 * @param obj object to click on
	 * @return true/false
	 * @author HaiNV12
	 * @author HieuDQ2
	 */
	protected <T extends Object> boolean clickT(T obj) {
		String stepDes = Message.format("RPT_CLICK", getObjectXPath(obj));
		LOGGER.debug(stepDes);
		getPageSource();
		Object tempBy = getInputBy(obj);
		boolean result = false;
		if (tempBy != null) {
			try {
				if (tempBy instanceof By) {
					LOGGER.debug("Get (By) for object");
					By objBy = (By) tempBy;
					LOGGER.debug("Moving to object then perform click.");
					getElement(objBy).click();
				} else if (tempBy instanceof WebElement) {
					LOGGER.debug("Proceed click action with WebElement object");
					WebElement element = (WebElement) obj;
					element.click();
				}
				result = true;
			} catch (NoSuchElementException | StaleElementReferenceException ex) {
				LOGGER.error("Element no longer the same");
				reportFailed(stepDes, Message.format("RPT_NOT_EXIST", obj));
			} catch (WebDriverException ex) {
				LOGGER.error("Web Driver Exception is thrown. Try again with clickExtend");
				result = clickExtend(obj);
			} catch (Exception ex) {
				LOGGER.error("Element is not clickable");
				reportFailed(stepDes, Message.format("RPT_UNEXPECTED_ERROR", obj));
			}
		} else {
			reportFailed(stepDes, Message.format("RPT_UNEXPECTED_ERROR", obj));
		}
		return result;
	}

	/**
	 * Rewrite hover method using either string input or By input, already included
	 * reporting.
	 * 
	 * @param obj object to hover on
	 * @return true/false
	 * @author ThanhNT44
	 */
	private <T extends Object> boolean hoverT(T obj) {
		String stepDes = String.format("Hover on [%s]", obj.toString());
		LOGGER.debug(stepDes);
		Object tempBy = getInputBy(obj);
		boolean result = false;
		// waitUntilDisappear(getBy(Home.GLOBAL_LOADING), Global.UAT_OBJECT_TIMEOUT *
		// 2);
		if (tempBy != null) {
			try {
				Actions actions = new Actions(Global.webDriver);
				if (tempBy instanceof By) {
					LOGGER.debug("tempBy is instance of By");
					By objBy = (By) tempBy;
					waitUntilDisplay(objBy);
					actions.moveToElement(getElement(objBy)).perform();
				} else if (tempBy instanceof WebElement) {
					LOGGER.debug("tempBy is instance of WebElement");
					WebElement element = (WebElement) obj;
					actions.moveToElement(element).perform();
					wait(SHORT_WAIT_TIME);
				}
				result = true;
			} catch (NoSuchElementException | StaleElementReferenceException ex) {
				LOGGER.error("Element no longer the same");
				reportFailed(stepDes, Message.format("RPT_NOT_EXIST", obj));
			} catch (Exception e) {
				LOGGER.error(e.getMessage());
				reportFailed(stepDes, e.getMessage());
			}
		} else {
			reportFailed(stepDes, Message.format("RPT_UNEXPECTED_ERROR", obj));
		}
		return result;
	}

	/**
	 * Method to wait until one object display
	 * 
	 * @param objPath object to wait for
	 * @return true/false
	 * @author HaiNV12
	 * @author HieuDQ2
	 */
	public boolean waitUntilDisplay(By objPath) {
		return waitUntilDisplay(objPath, Global.UAT_OBJECT_TIMEOUT);
	}

	/**
	 * Method to wait until one object display
	 * 
	 * @param String objPath to wait for
	 * @return true/false
	 * @author HaiNV12
	 * @author HieuDQ2
	 */
	public boolean waitUntilDisplay(String objPath) {
		return waitUntilDisplay(getBy(objPath), Global.UAT_OBJECT_TIMEOUT);
	}

	/**
	 * Wait until web element is displayed with default time out
	 * 
	 * @param element
	 * @return boolean
	 * @author HieuDQ2
	 */
	public boolean waitUntilDisplay(WebElement element) {
		return waitUntilDisplay(element, Global.UAT_OBJECT_TIMEOUT);
	}

	/**
	 * Wait until web element is displayed with specific time out
	 * 
	 * @param element
	 * @param timeout
	 * @return boolean
	 * @author HieuDQ2
	 */
	public boolean waitUntilDisplay(WebElement element, int timeout) {
		LOGGER.debug("Wait until object [{}] display within [{}] seconds", element, timeout);
		DateTime before = LocalDate.now().toDateTimeAtCurrentTime();
		setBrowserNoWait();
		Wait<WebDriver> wait = new FluentWait<WebDriver>(Global.webDriver).withTimeout(Duration.ofSeconds(timeout))
				.pollingEvery(Duration.ofMillis(500)).ignoring(NoSuchElementException.class)
				.ignoring(StaleElementReferenceException.class);
		boolean result = false;
		try {
			result = wait.until(driver -> {
				driver.getPageSource();
				return element.isDisplayed();
			});
			if (!result)
				result = wait.until(ExpectedConditions.visibilityOf(element)) != null;
		} catch (TimeoutException ex) {
			LOGGER.error("Waiting object [{}] displayed, TIMEOUT!", element);
			reportFailed(String.format("Waiting object [%s] displayed", element.toString()), TIMEOUT_REACHED);
		}
		if (result) {
			Seconds diff = Seconds.secondsBetween(before, LocalDate.now().toDateTimeAtCurrentTime());
			LOGGER.debug("Time taken for [{}] to display is: [{}] seconds.", element, diff.getSeconds());
		}
		setBrowserWaitDefault();
		return result;
	}

	/**
	 * Method to wait until one object display
	 * 
	 * @param objPath object to wait for
	 * @return true/false
	 * @author HaiNV12
	 */
	public boolean waitUntilDisplay(By objPath, int timeout) {
		LOGGER.debug("Wait until object [{}] display within [{}] seconds", objPath, timeout);
		DateTime before = LocalDate.now().toDateTimeAtCurrentTime();
		setBrowserNoWait();
		Wait<WebDriver> wait = new FluentWait<WebDriver>(Global.webDriver).withTimeout(Duration.ofSeconds(timeout))
				.pollingEvery(Duration.ofMillis(250)).ignoring(NoSuchElementException.class)
				.ignoring(StaleElementReferenceException.class);
		boolean result = false;
		try {
			result = wait.until(driver -> {
				driver.getPageSource();
				boolean temp = driver.findElement(objPath).isDisplayed();
				if (!temp)
					LOGGER.debug(String.format("Object [%s] still NOT displayed", objPath.toString()));
				return temp;
			});
			result = wait.until(ExpectedConditions.visibilityOfElementLocated(objPath)) != null;
		} catch (TimeoutException ex) {
			LOGGER.error(String.format("Waiting object [%s] displayed, TIMEOUT!", objPath.toString()));
			reportFailed(String.format("Waiting object [%s] displayed", objPath.toString()), TIMEOUT_REACHED);
		}
		if (result) {
			Seconds diff = Seconds.secondsBetween(before, LocalDate.now().toDateTimeAtCurrentTime());
			LOGGER.debug("Time taken for [{}] to display is: [{}] seconds.", objPath, diff.getSeconds());
		}
		setBrowserWaitDefault();
		return result;
	}

	/**
	 * Wait until specific object located from parent is displayed within default
	 * timeout
	 * 
	 * @param root
	 * @param objPath
	 * @return boolean
	 * @author HieuDQ2
	 */
	public boolean waitUntilDisplay(WebElement root, By objPath) {
		return waitUntilDisplay(root, objPath, Global.UAT_OBJECT_TIMEOUT);
	}

	/**
	 * @param root
	 * @param objPath
	 * @param timeout
	 * @return
	 */
	public boolean waitUntilDisplay(WebElement root, By objPath, int timeout) {
		LOGGER.debug("Wait until object [{}] display within [{}] seconds", objPath, timeout);
		DateTime before = LocalDate.now().toDateTimeAtCurrentTime();
		setBrowserNoWait();
		Wait<WebDriver> wait = new FluentWait<WebDriver>(Global.webDriver).withTimeout(Duration.ofSeconds(timeout))
				.pollingEvery(Duration.ofMillis(250)).ignoring(NoSuchElementException.class)
				.ignoring(StaleElementReferenceException.class);
		boolean result = false;
		try {
			result = wait.until(driver -> {
				driver.getPageSource();
				return root.findElement(objPath).isDisplayed();
			});
			if (!result)
				result = wait.until(ExpectedConditions.visibilityOfElementLocated(objPath)) != null;
		} catch (TimeoutException ex) {
			LOGGER.error(String.format("Waiting object [%s] displayed, TIMEOUT!", objPath.toString()));
			reportFailed(String.format("Waiting object [%s] displayed", objPath.toString()), TIMEOUT_REACHED);
		}
		if (result) {
			Seconds diff = Seconds.secondsBetween(before, LocalDate.now().toDateTimeAtCurrentTime());
			LOGGER.debug("Time taken for [{}] to display is: [{}] seconds.", objPath, diff.getSeconds());
		}
		setBrowserWaitDefault();
		return result;
	}

	/**
	 * Method to wait until one object is disappeared
	 * 
	 * @param objPath object to wait for
	 * @return true/false
	 * @author HaiNV12
	 * @author HieuDQ2
	 */
	public boolean waitUntilDisappear(By objPath) {
		return waitUntilDisappear(objPath, Global.UAT_OBJECT_TIMEOUT);
	}

	/**
	 * Method to wait until one object is disappeared
	 * 
	 * @param objBy object to wait for
	 * @return true/false
	 */
	public boolean waitUntilDisappear(By objBy, int timeout) {
		LOGGER.debug(String.format("Wait until object [%s] disappear within [%s] seconds", objBy.toString(), timeout));
		setBrowserNoWait();
		Wait<WebDriver> wait = new FluentWait<WebDriver>(Global.webDriver).withTimeout(Duration.ofSeconds(timeout))
				.pollingEvery(Duration.ofMillis(500));
		DateTime before = LocalDate.now().toDateTimeAtCurrentTime();
		boolean result = false;
		try {
			result = wait.until(driver -> {
				try {
					getPageSource();
					WebElement element = driver.findElement(objBy);
					boolean temp = element.isDisplayed();
					if (temp)
						LOGGER.debug(String.format("[%s] is still displayed!", objBy.toString()));
					return !temp;
				} catch (NoSuchElementException | StaleElementReferenceException e) {
					return true;
				} catch (Exception ex) {
					String er = ex.getMessage();
					LOGGER.debug(er);
					return true;
				}
			});
		} catch (TimeoutException ex) {
			LOGGER.error(String.format("Waiting object [%s] disappear, TIMEOUT!", objBy.toString()));
			reportFailed(String.format("Waiting object [%s] disappear", objBy.toString()), TIMEOUT_REACHED);
		}
		if (result) {
			Seconds diff = Seconds.secondsBetween(before, LocalDate.now().toDateTimeAtCurrentTime());
			LOGGER.debug(String.format("Time taken for [%s] to disappear is [%s] seconds.", objBy.toString(),
					diff.getSeconds()));
		}
		setBrowserWaitDefault();
		return result;
	}

	/**
	 * Wait until disappear
	 * 
	 * @param element
	 * @param objPath
	 * @return boolean
	 * @author HieuDQ2
	 */
	public boolean waitUntilDisappear(WebElement element, By objPath) {
		return waitUntilDisappear(element, objPath, Global.UAT_OBJECT_TIMEOUT);
	}

	/**
	 * Wait until disappear
	 * 
	 * @param root
	 * @param objBy
	 * @param timeout
	 * @return boolean
	 * @author HieuDQ2
	 */
	public boolean waitUntilDisappear(WebElement root, By objBy, int timeout) {
		LOGGER.debug(String.format("Wait until object [%s] disappear within [%s] seconds", objBy.toString(), timeout));
		setBrowserNoWait();
		Wait<WebDriver> wait = new FluentWait<WebDriver>(Global.webDriver).withTimeout(Duration.ofSeconds(timeout))
				.pollingEvery(Duration.ofMillis(500));
		DateTime before = LocalDate.now().toDateTimeAtCurrentTime();
		boolean result = false;
		try {
			result = wait.until(driver -> {
				try {
					driver.getPageSource();
					WebElement element = root.findElement(objBy);
					return !element.isDisplayed();
				} catch (NoSuchElementException | StaleElementReferenceException e) {
					return true;
				} catch (Exception ex) {
					String er = ex.getMessage();
					LOGGER.debug(er);
					return true;
				}
			});
		} catch (TimeoutException ex) {
			LOGGER.error(String.format("Waiting object [%s] disappear, TIMEOUT!", objBy.toString()));
			reportFailed(String.format("Waiting object [%s] disappear", objBy.toString()), TIMEOUT_REACHED);
		}
		if (result) {
			Seconds diff = Seconds.secondsBetween(before, LocalDate.now().toDateTimeAtCurrentTime());
			LOGGER.debug(String.format("Time taken for [%s] to disappear is [%s] seconds.", objBy.toString(),
					diff.getSeconds()));
		}
		setBrowserWaitDefault();
		return result;
	}

	/**
	 * Method to check if object is disabled or not
	 * 
	 * @param objPath Path to the object, can be String or By
	 * @return true/false
	 * @author HaiNV12
	 */
	public <T extends Object> boolean isObjectReadonly(T objPath) {
		String stepDes = Message.format("RPT_VERIFY_READONLY", objPath);
		boolean result = false;
		Global.webDriver.getPageSource();
		LOGGER.debug(stepDes);
		T tempBy = getInputBy(objPath);
		if (tempBy != null) {
			WebElement element = (tempBy instanceof WebElement || objPath instanceof RemoteWebElement)
					? (WebElement) tempBy
					: getElement((By) tempBy);
			scrollTo(element);
			String tagName = element.getTagName();
			switch (tagName) {
			case "p-dropdown":
				result = (element.findElement(By.xpath("./div")).getAttribute(CLASS_ATTRIBUTE).contains("disabled")
						&& !isClickable(objPath.toString()));
				break;

			case "p-calendar":
				result = element.findElement(By.xpath("./span")).getAttribute(CLASS_ATTRIBUTE).contains("disabled");
				break;

			default:
				result = (!isClickable(objPath.toString()) && !isEnabled(objPath.toString())
						&& !isObjectAbleToInputValue(element));
				break;
			}
		}
		return result;
	}

	/**
	 * Check whether User is able to input value into element
	 * 
	 * @param element
	 * @return true / false
	 * @author HieuDQ2
	 */
	protected boolean isObjectAbleToInputValue(WebElement element) {
		String objXPath = getObjectXPath(element);
		String methodDes = String.format("Check whether User is able to send key to object [%s]", objXPath);
		LOGGER.debug(methodDes);
		boolean result = false;
		try {
			element.sendKeys("a");
			element.sendKeys(Keys.BACK_SPACE);
			result = true;
		} catch (WebDriverException ex) {
			// Do nothing
		} catch (Exception ex) {
			String errorMessage = ex.getMessage();
			LOGGER.error(errorMessage);
			reportFailed(methodDes, errorMessage);
		}
		return result;
	}

	/**
	 * Method to filter input either String or By
	 * 
	 * @param objPath Path to the object
	 * @return object's By
	 * @author HaiNV12
	 */
	@SuppressWarnings("unchecked")
	public <T extends Object> T getInputBy(T objPath) {
		T temp = null;
		if (objPath instanceof String) {
			temp = (T) getBy(objPath.toString());
		} else if (objPath instanceof By) {
			temp = objPath;
		} else if (objPath instanceof WebElement || objPath instanceof RemoteWebElement) {
			temp = objPath;
		} else {
			LOGGER.error("Wrong input value of action, not By or String inputted");
		}
		return temp;
	}

	/**
	 * Check whether checkbox is checked or not
	 * 
	 * @param objPath
	 * @return true / false
	 */
	protected boolean isCheckboxChecked(String objPath) {
		return isCheckboxChecked(getElement(getBy(objPath)));
	}

	/**
	 * Check whether WebElement is checked or not
	 * 
	 * @param element
	 * @return true / false
	 * @author HieuDQ2
	 */
	protected boolean isCheckboxChecked(WebElement element) {
		boolean isChecked = false;
		setBrowserNoWait();
		List<WebElement> listElements = element.findElements(By.xpath(".//span[contains(@class,'pi-check')]"));
		if (!listElements.isEmpty())
			isChecked = true;
		setBrowserWaitDefault();
		return isChecked;
	}

	/**
	 * Override isSelected
	 * 
	 * @return true / false
	 * @author HieuDQ2
	 */
	@Override
	public boolean isSelected(String objPath) {
		boolean result = false;
		if (getElement(objPath)) {
			WebElement element = getElement(getBy(objPath));
			result = isSelected(element);
		}
		return result;
	}

	/**
	 * 
	 * @param parentElement
	 * @param objPath
	 * @return
	 * @author HieuDQ2
	 */
	public boolean isSelected(WebElement parentElement, String objPath) {
		WebElement element = parentElement.findElement(getBy(objPath));
		return isSelected(element);
	}

	/**
	 * Compare value of element is equal expected value
	 * 
	 * @param objPath, expValue
	 * @return true / false
	 * @author QuangNV12
	 */
	public boolean compareEqual(String objPath, String expValue) {
		boolean result = false;
		scrollTo(objPath);
		String stepDes = String.format("Verify value of [%s] = [%s]", objPath, expValue);
		String actValue = getText(objPath);
		if (expValue.equals(actValue.trim())) {
			result = true;
			reportPassed(stepDes);
		} else {
			reportFailed(stepDes, String.format("Expected: [%s]; Actual: [%s]", expValue, actValue));
		}
		return result;
	}

	/**
	 * Compare value of element is equal expected value
	 * 
	 * @param element
	 * @param expectedValue
	 * @return boolean
	 * @author HieuDQ2
	 */
	public boolean compareEqual(WebElement element, String expectedValue) {
		boolean result = false;
		scrollTo(element);
		String stepDes = String.format("Verify value of element [%s] = [%s]", element, expectedValue);
		String actualValue = getText(element);
		if (expectedValue.equals(actualValue.trim())) {
			result = true;
			reportPassed(stepDes);
		} else {
			reportFailed(stepDes, String.format("Expected: [%s]; Actual: [%s]", expectedValue, actualValue));
		}
		return result;
	}

	/**
	 * Check value of element contains expected value
	 * 
	 * @param objPath, expectedValue
	 * @return true / false
	 * @author QuangNV12
	 */
	public boolean checkElementContain(String objPath, String expValue) {
		boolean result = false;
		scrollTo(objPath);
		String stepDes = String.format("Verify value of [%s] = [%s]", objPath, expValue);
		String actValue = getText(objPath);
		if (actValue.trim().toLowerCase().contains(expValue.toLowerCase())) {
			result = true;
			reportPassed(stepDes);
		} else {
			reportFailed(stepDes, String.format("Expected: [%s]; Actual: [%s]", expValue, actValue));
		}
		return result;
	}

	/**
	 * Check whether element is selected
	 * 
	 * @param element
	 * @return true / false
	 * @author HieuDQ2
	 */
	public boolean isSelected(WebElement element) {
		boolean result = false;
		switch (element.getTagName()) {
		case "p-radiobutton":
			result = element.findElement(By.xpath(".//span")).getAttribute(CLASS_ATTRIBUTE).contains("pi-circle-on");
			break;

		case "p-checkbox":
		case "div":
			result = element.findElement(By.xpath(".//span")).getAttribute(CLASS_ATTRIBUTE).contains("pi-check");
			break;

		default:
			result = element.isSelected();
			break;
		}
		return result;
	}

	/**
	 * @param objPath
	 * @param sortOrder
	 * @author ChanhPQ
	 */
	@Override
	public boolean checkListIsSorted(String objPath, String sortOrder) {
		List<WebElement> listWebElement = getElements(objPath);
		if (listWebElement.size() <= 1) {
			return true;
		}
		List<String> elementText = new ArrayList<>();
		for (WebElement ele : listWebElement) {
			elementText.add(ele.getText().trim());
		}
		LOGGER.debug("List items: " + String.join(";", elementText));
		return isListIsSorted(elementText, sortOrder);
	}

	/**
	 * @param String
	 * @param format DB date match with UI
	 * @author ChanhPQ
	 */
	public String formatDate(String input) {
		String formatDate = null;
		if (input == null || input.equals("")) {
			formatDate = "";
		} else {
			try {
				DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
				Date date;
				date = inputFormat.parse(input);
				DateFormat outputFormat = new SimpleDateFormat("dd-MMM-yyyy");
				formatDate = outputFormat.format(date);
			} catch (ParseException e) {
				LOGGER.error("Occur exception when format Date: [{}]", e.getMessage());
			}
		}
		return formatDate;
	}

	/**
	 * Verify successful/error message content
	 * 
	 * @param objPath location of displaying message (XPath, Id, etc.)
	 * @param expMsg  Expected message
	 * @return true/false
	 * @author ThanhNT44
	 * @author HieuDQ2
	 */
	public boolean verifyMessageContentDisplayed(String objPath, String expMessage) {
		LOGGER.debug(Message.format("RPT_VERIFY_DISPLAY", objPath));
		boolean result = true;
		Wait<WebDriver> wait = new FluentWait<WebDriver>(Global.webDriver)
				.withTimeout(Duration.ofSeconds(Global.UAT_OBJECT_TIMEOUT))
				.pollingEvery(Duration.ofSeconds(SHORT_WAIT_TIME));
		setBrowserNoWait();
		try {
			result = wait.until(driver -> {
				driver.getPageSource();
				List<WebElement> element = driver.findElements(getBy(objPath));
				return !element.isEmpty() && element.get(0).getText().contains(expMessage);
			});
		} catch (Exception e) {
			LOGGER.error("Timeout");
			result = false;
		}
		setBrowserWaitDefault();
		return result;
	}

	/**
	 * @author ThanhNT44
	 */
	public String getMessageContent(String objPath) {
		String result = null;
		if (isExisted(objPath) || isDisplayed(objPath)) {
			result = getText(objPath);
		}
		return result;
	}

	/**
	 * Method to check if List is sorted follow Order or not
	 * 
	 * @param input    List need to be checked
	 * @param criteria "ASC" or "DESC"
	 * @return true/false
	 * @author HaiNV12
	 */
	@SuppressWarnings({ "rawtypes" })
	public <T extends Comparable> boolean isListIsSorted(List<T> input, String sortOrder) {
		LOGGER.debug(String.format("Checking list is Sorted follow: [%s]", sortOrder));
		boolean result = true;
		if (!input.isEmpty()) {
			if ("DESC".equalsIgnoreCase(sortOrder.toUpperCase())) {
				result = Ordering.natural().reverse().isOrdered(input);
			} else {
				result = Ordering.natural().isOrdered(input);
			}
		}
		return result;
	}

	/**
	 * Select checkbox
	 * 
	 * @param element
	 * @param selected
	 * @return true / false
	 * @author HieuDQ2
	 */
	public boolean selectCheckBox(WebElement element, boolean selected) {
		Boolean result = false;
		if (isSelected(element) != selected) {
			result = scrollTo(element);
			result &= click(element);
		}
		return result;
	}

	/**
	 * Override isDisplayed
	 * 
	 * @author HaiNV12
	 */
	@Override
	public boolean isDisplayed(String objPath) {
		boolean displayed;
		setBrowserNoWait();
		displayed = super.isDisplayed(objPath);
		setBrowserWaitDefault();
		return displayed;
	}

	public boolean isDisplayed(String objPath, int timeout) {
		boolean displayed;
		setBrowserWait(timeout);
		displayed = super.isDisplayed(objPath);
		setBrowserWaitDefault();
		return displayed;
	}

	/**
	 * Override click method
	 * 
	 * @author HaiNV12
	 */
	@Override
	public boolean click(String objPath) {
		return clickExtend(objPath);
	}

	/**
	 * Override click method
	 * 
	 * @author HaiNV12
	 */
	@Override
	public boolean click(WebElement ele) {
		return ele.isDisplayed() ? clickExtend(ele) : false;
	}

	/**
	 * Override click method
	 * 
	 * @author HaiNV12
	 */
	@Override
	public boolean click(By by) {
		return clickExtend(by);
	}

	/**
	 * click Radio
	 * 
	 * @param By
	 * @author TuanLA47
	 */
	public boolean clickRadio(By by) {
		return clickExtend(getElement(by));
	}

	/**
	 * Override hover method for object path (e.g. Id, Xpath...)
	 * 
	 * @author ThanhNT44
	 */
	@Override
	public boolean hover(String objPath) {
		return hoverT(objPath);
	}

	/**
	 * hover method for Web element
	 * 
	 * @author ThanhNT44
	 */
	public boolean hover(WebElement ele) {
		return ele.isDisplayed() ? hoverT(ele) : false;
	}

	/**
	 * Method to wait until non of the object matches the expression displayed
	 * 
	 * @param objPath object to wait for
	 * @return true/false
	 * @author HaiNV12
	 */
	public boolean waitUntilMultipleObjectsDisappear(By objPath) {
		LOGGER.debug(String.format("Wait until object [%s] disappear", objPath.toString()));
		setBrowserNoWait();
		Wait<WebDriver> wait = new FluentWait<WebDriver>(Global.webDriver)
				.withTimeout(Duration.ofSeconds(Global.UAT_OBJECT_TIMEOUT * 2)).pollingEvery(Duration.ofMillis(500));
		DateTime before = LocalDate.now().toDateTimeAtCurrentTime();

		boolean result = false;
		try {
			result = wait.until(driver -> {
				getPageSource();
				List<WebElement> tempList = driver.findElements(objPath);
				if (!tempList.isEmpty())
					LOGGER.debug(String.format("[%s] is still displayed!", objPath.toString()));
				return tempList.isEmpty();
			});
		} catch (TimeoutException ex) {
			LOGGER.error(String.format("Waiting until multiple object [%s] disappear, TIMEOUT!", objPath.toString()));
			reportFailed(String.format("Waiting until multiple object [%s] disappear", objPath.toString()),
					TIMEOUT_REACHED);
		}
		if (result) {
			Seconds diff = Seconds.secondsBetween(before, LocalDate.now().toDateTimeAtCurrentTime());
			LOGGER.debug(String.format("Time taken for [%s] to disappear is [%s] seconds.", objPath.toString(),
					diff.getSeconds()));
		}
		setBrowserWaitDefault();
		return result;
	}

	/**
	 * Wait page load with default timeout value
	 * 
	 * @return true / false
	 * @author HieuDQ2
	 */
	public boolean waitPageLoad() {
		return waitPageLoad(Global.UAT_OBJECT_TIMEOUT);
	}

	/**
	 * Wait page load
	 * 
	 * @author HieuDQ2
	 */
	@Override
	public boolean waitPageLoad(int seconds) {
		boolean result = true;
		result &= super.waitPageLoad(seconds);
		// waitUntilDisappear(getBy(Home.GLOBAL_LOADING));
		result &= waitPageReady(seconds);
//		if (result)
//			result &= waitJQueryReady(seconds);
		return result;
	}

	/**
	 * Wait Page ready without waiting for JQuery ready
	 * 
	 * @return true / false
	 * @author HieuDQ2
	 */
	public boolean waitPageLoadWithoutJQuery() {
		return waitPageReady(Global.UAT_OBJECT_TIMEOUT);
	}

	/**
	 * Wait until page is Ready
	 * 
	 * @param timeout
	 * @return true / false
	 * @author HieuDQ2
	 */
	public boolean waitPageReady(int timeout) {
		LOGGER.debug("Wait until Page is Ready within [{}] SECONDS", timeout);
		Wait<WebDriver> wait = new FluentWait<WebDriver>(Global.webDriver).withTimeout(Duration.ofSeconds(timeout))
				.pollingEvery(Duration.ofSeconds(SHORT_WAIT_TIME));
		boolean result = false;
		try {
			result = wait.until(driver -> ((JavascriptExecutor) driver).executeScript("return document.readyState")
					.toString().equals("complete"));
		} catch (TimeoutException e) {
			String errMgs = String.format("Page is not Ready within [%s] SECONDS", timeout);
			LOGGER.error(errMgs);
			reportFailed("Wait until page is Ready", errMgs);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		}
		return result;
	}

	/**
	 * Wait until AJAX is ready
	 * 
	 * @param timeout
	 * @return true / false
	 * @author HieuDQ2
	 */
	@SuppressWarnings("unused")
	private boolean waitJQueryReady(int timeout) {
		LOGGER.debug("Wait until AJAX is Ready within [{}] SECONDS", timeout);
		Wait<WebDriver> wait = new FluentWait<WebDriver>(Global.webDriver).withTimeout(Duration.ofSeconds(timeout))
				.pollingEvery(Duration.ofSeconds(SHORT_WAIT_TIME));
		boolean result = false;
		try {
			result = wait.until(
					driver -> (boolean) ((JavascriptExecutor) driver).executeScript("return jQuery.active == 0"));
		} catch (TimeoutException e) {
			LOGGER.error(String.format("jQuery is not Ready within [%s] SECONDS", timeout));
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		}
		return result;
	}

	/**
	 * Name Check this list is order ASC or DESC. This funtion just check with
	 * String
	 * 
	 * @param originalList
	 * @param sortOrder    "ASC" or "DESC"
	 * @return true/false
	 * 
	 * @author QuangNV12
	 */
	public boolean isSortedList(List<String> originalList, String sortOrder) {
		boolean result = true;
		Comparator<String> c = String.CASE_INSENSITIVE_ORDER.thenComparing(Comparator.naturalOrder());

		List<String> afterSort = new ArrayList<>();
		afterSort.addAll(originalList);
		afterSort.sort(c);

		List<String> reverseAfterSort = new ArrayList<>();

		if (sortOrder.equalsIgnoreCase("DESC")) {
			reverseAfterSort.addAll(com.google.common.collect.Lists.reverse(afterSort));
			result = originalList.equals(reverseAfterSort);
		} else {
			result = originalList.equals(afterSort);
		}
		return result;
	}

	/**
	 * @param objPath
	 * @param sortOrder
	 * @author QuangNV12
	 */
	public boolean checkObjectListIsSorted(String objPath, String sortOrder) {
		List<WebElement> listWebElement = getElements(objPath);
		if (listWebElement.size() <= 1) {
			return true;
		}
		List<String> elementText = new ArrayList<>();
		for (WebElement ele : listWebElement) {
			elementText.add(ele.getText().trim());
		}
		LOGGER.debug("List items: " + String.join(";", elementText));
		return isSortedList(elementText, sortOrder);
	}

	/**
	 * Wait until item clickable
	 * 
	 * @param objPath
	 * @return true / false
	 * @author HieuDQ2
	 */
	public boolean waitUntilClickable(By objBy) {
		WebElement webElement = getElement(objBy);
		return waitUntilClickable(webElement);
	}

	/**
	 * Wait until item click able (web element)
	 * 
	 * @param web element
	 * @return true / false
	 * @author HieuDQ2
	 */
	public boolean waitUntilClickable(WebElement webElement) {
		String objPath = getObjectXPath(webElement);
		String methodDes = String.format("Wait until [%s] is clickable", objPath);
		LOGGER.debug(methodDes);
		setBrowserNoWait();
		Wait<WebDriver> wait = new FluentWait<WebDriver>(Global.webDriver)
				.withTimeout(Duration.ofSeconds(Global.UAT_OBJECT_TIMEOUT))
				.pollingEvery(Duration.ofSeconds(SHORT_WAIT_TIME)).ignoring(NoSuchElementException.class)
				.ignoring(StaleElementReferenceException.class);
		DateTime before = LocalDate.now().toDateTimeAtCurrentTime();
		boolean result = false;
		try {
			result = wait.until(driver -> {
				driver.getPageSource();
				String attribute = webElement.getAttribute("disabled");
				return webElement.isDisplayed() && webElement.isEnabled()
						&& (attribute == null || (attribute != null && attribute.equalsIgnoreCase("true")));
			});
		} catch (TimeoutException e) {
			LOGGER.error(String.format("Waiting object [%s] is clickable, TIMEOUT!", webElement));
			reportFailed(String.format(methodDes, webElement), TIMEOUT_REACHED);
		} catch (Exception e) {
			LOGGER.error(e.toString());
			reportFailed(String.format(methodDes, webElement), e.toString());
		}
		setBrowserWaitDefault();
		if (result) {
			scrollTo(webElement);
			Seconds diff = Seconds.secondsBetween(before, LocalDate.now().toDateTimeAtCurrentTime());
			LOGGER.debug("Time taken for [{}] to be clickable is: [{}] seconds.", webElement, diff.getSeconds());
		}
		return result;
	}

	/**
	 * Get first 200 records of the list
	 * 
	 * @param originalList
	 * @return List<T>
	 * @author HieuDQ2
	 */
	public <T extends Object> List<T> getFirstRecords(List<T> originalList, int records) {
		List<T> result = null;
		if (originalList != null) {
			result = originalList.stream().limit(records).collect(Collectors.toList());
		}
		return result;
	}

	/**
	 * Get xPath value from objPath
	 * 
	 * @param objPath
	 * @return String
	 * @author HieuDQ2
	 */
	public String getXPathValue(String objPath) {
		String xPathValue = getBy(objPath).toString();
		return xPathValue.contains("By.xpath: ") ? xPathValue.replace("By.xpath: ", "") : objPath;
	}

	/**
	 * Get xPath value from By object
	 * 
	 * @param by
	 * @return String
	 * @author HieuDQ2
	 */
	public String getXPathValue(By by) {
		String xPathValue = by.toString();
		return xPathValue.contains("By.xpath: ") ? xPathValue.replace("By.xpath: ", "") : null;
	}

	/**
	 * Count item in list by its name
	 * 
	 * @param itemValue
	 * @param listObjPath
	 * @return int
	 * @author HieuDQ2
	 */
	public int countItemInList(String itemValue, String listObjPath) {
		LOGGER.debug("Count Item [{}] in [{}]", itemValue, listObjPath);
		int count = 0;
		getPageSource();
		List<WebElement> items = getElements(listObjPath);
		for (WebElement item : items) {
			if (item.getText().equals(itemValue)) {
				count++;
			}
		}
		return count;
	}

	/**
	 * Report pass with comment
	 * 
	 * @param stepDescription
	 * @param comment
	 * @author HieuDQ2
	 */
	public void reportPassed(String stepDescription, String comment) {
		File file = null;
		if (!Global.SnapshotFailedStepOnly) {
			file = captureSnapShot();
		}
		reportStep(stepDescription, TestResult.R_PASSED, file, comment);
	}

	/**
	 * Report Warning with comment
	 * 
	 * @param stepDescription
	 * @param comment
	 * @author HieuDQ2
	 */
	public void reportWarning(String stepDescription, String comment) {
		File file = null;
		if (!Global.SnapshotFailedStepOnly) {
			file = captureSnapShot();
		}
		reportStep(stepDescription, TestResult.R_WARNING, file, comment);
	}

	/**
	 * Report Warning without comment
	 * 
	 * @param stepDescription
	 * @param comment
	 * @author HieuDQ2
	 */
	public void reportWarning(String stepDescription) {
		File file = null;
		if (!Global.SnapshotFailedStepOnly) {
			file = captureSnapShot();
		}
		reportStep(stepDescription, TestResult.R_WARNING, file, "");
	}

	/**
	 * Clear local storage
	 * 
	 * @author HieuDQ2
	 */
	public void clearLocalStorage() {
		LOGGER.debug("Clear local storage");
		WebDriver driver = Global.webDriver;
		driver.manage().deleteAllCookies();
		if (driver instanceof WebStorage) {
			((WebStorage) driver).getLocalStorage().clear();
		}
	}

	/**
	 * Switch to the new tab
	 * 
	 * @author NhacLN1
	 */
	public void switchToNewTab() {
		LOGGER.debug("Switch to New tab");
		String[] tabs = Global.webDriver.getWindowHandles().toArray(new String[0]);
		Global.webDriver.switchTo().window(tabs[tabs.length - 1]);
	}

	public void switchToPreviousTab() {
		LOGGER.debug("Switch to Previous tab");
		String[] tabs = Global.webDriver.getWindowHandles().toArray(new String[0]);
		Global.webDriver.switchTo().window(tabs[tabs.length - 2]);
	}

	/**
	 * Set value for Textbox. This method won't be call internal but will be invoked
	 *
	 * @param objPath
	 * @param value
	 * @author HieuDQ2
	 */
	public void setValueForTextbox(String objPath, String value) {
		WebElement element = getElement(getBy(objPath));
		if (element.isEnabled()) {
			setValue(objPath, value);
			wait(SHORT_WAIT_TIME);
		} else {
			reportFailed("Set value for Textbox", "Element is not enable");
		}
	}

	/**
	 * Set value for Textbox. This method won't be call internal but will be invoked
	 * 
	 * @param element
	 * @param value
	 * @author HieuDQ2
	 */
	public void setValueForTextbox(WebElement element, String value) {
		if (element.isEnabled()) {
			setValue(element, value);
			wait(SHORT_WAIT_TIME);
		} else {
			reportFailed("Set value for Textbox", "Element is not enable");
		}
	}

	/**
	 * Verify value
	 * 
	 * @param objPath
	 * @param expectedValue
	 * @return true / false
	 * @author HieuDQ2
	 */
	public boolean verifyValueOfElement(String objPath, String expectedValue) {
		return compareEqual(objPath, expectedValue);
	}

	/**
	 * Verify value
	 * 
	 * @param element
	 * @param value
	 * @return boolean
	 * @author HieuDQ2
	 */
	public boolean verifyValueOfElement(WebElement element, String value) {
		return compareEqual(element, value);
	}

	/**
	 * Split a String From Last Index
	 * 
	 * @return String
	 * @author LamHD4
	 */
	public String splitStringFromLastIndex(String value, int lastIndex) {
		String valueString = "";
		lastIndex = value.length() - lastIndex;
		for (int index = 0; index < lastIndex; index++) {
			valueString = valueString + value.charAt(index);
		}
		return valueString;
	}

	/**
	 * Wait until web element disappear within default timeout
	 * 
	 * @param element
	 * @return boolean
	 * @author HieuDQ2
	 */
	public boolean waitUntilDisappear(WebElement element) {
		return waitUntilDisappear(element, Global.UAT_OBJECT_TIMEOUT);
	}

	/**
	 * Wait until web element disappear within specific timeout
	 * 
	 * @param element
	 * @param timeout
	 * @return boolean
	 * @author HieuDQ2
	 */
	public boolean waitUntilDisappear(WebElement element, int timeout) {
		LOGGER.debug("Wait until object [{}] disappear within [{}] seconds", element, timeout);
		setBrowserNoWait();
		Wait<WebDriver> wait = new FluentWait<WebDriver>(Global.webDriver).withTimeout(Duration.ofSeconds(timeout))
				.pollingEvery(Duration.ofMillis(500));
		DateTime before = LocalDate.now().toDateTimeAtCurrentTime();
		boolean result = false;
		try {
			result = wait.until(driver -> {
				try {
					driver.getPageSource();
					return !element.isDisplayed();
				} catch (NoSuchElementException | StaleElementReferenceException e) {
					return true;
				} catch (Exception ex) {
					String er = ex.getMessage();
					LOGGER.debug(er);
					return true;
				}
			});
		} catch (TimeoutException ex) {
			LOGGER.error(String.format("Waiting object [%s] disappear, TIMEOUT!", element.toString()));
			reportFailed(String.format("Waiting object [%s] disappear", element.toString()), TIMEOUT_REACHED);
		}
		if (result) {
			Seconds diff = Seconds.secondsBetween(before, LocalDate.now().toDateTimeAtCurrentTime());
			LOGGER.debug("Time taken for [{}] to disappear is [{}] seconds.", element, diff.getSeconds());
		}
		setBrowserWaitDefault();
		return result;
	}

	/**
	 * Check the file from a specific directory
	 * 
	 * @param fileName
	 * @return boolean
	 * @author ThanhNT44
	 */
	public boolean isFileDownloaded(String fileName) {
		LOGGER.debug("Check the file [{}] from a specific directory", fileName);
		boolean flag = false;
		String dirPath = System.getenv("USERPROFILE") + "\\Downloads";
		LOGGER.debug(dirPath);
		File dir = new File(dirPath);
		File[] dirContents = dir.listFiles();
		if (dirContents == null || dirContents.length == 0) {
			return flag;
		}

		for (File fileItem : dirContents) {
			if (fileItem.isFile()) {
				String fileItemName = fileItem.getName();
				LOGGER.debug("Scanning file [{}]", fileItemName);
				if (fileItemName.equals(fileName)) {
					flag = true;
					String fileNamePath = String.format("%s\\%s", dirPath, fileName);
					cleanUp(fileNamePath);
					break;
				}
			}
		}
		return flag;
	}

	/**
	 * Remove downloaded file
	 * 
	 * @param fileNamePath
	 * @author ThanhNT44
	 */
	private void cleanUp(String fileNamePath) {
		Path path = null;
		try {
			LOGGER.debug("Remove downloaded file from a specific directory [{}]", fileNamePath);
			path = Paths.get(fileNamePath);
			Files.delete(path);
		} catch (NoSuchFileException ex) {
			LOGGER.error("%s: no such" + " file or directory%n", path);
		} catch (DirectoryNotEmptyException ex) {
			LOGGER.error("%s not empty%n", path);
		} catch (IOException ioEX) {
			// File permission problems are caught here.
			LOGGER.error(ioEX);
		}
	}

	/**
	 * Set data based on data
	 * 
	 * @param data
	 * @return true / false
	 */
	public boolean setData(Data data, LinkedHashMap<String, String[]> inputMap) {
		String stepDes = "Set Data";
		LOGGER.debug(stepDes);
		boolean result = true;
		for (Iterator<String> iterator = inputMap.keySet().iterator(); iterator.hasNext();) {
			String field = iterator.next();
			if (!inputMap.containsKey(field)) {
				String errorMessage = String.format("Field [%s] is not supported", field);
				LOGGER.error(errorMessage);
				reportFailed(stepDes, errorMessage);
				continue;
			}
			String methodName = inputMap.get(field)[0];
			String value = data.getData(field);
			String objPath = null;
			if (value != null) {
				Method method;
				try {
					switch (field) {
					default:
						method = this.getClass().getSuperclass().getDeclaredMethod(methodName, String.class,
								String.class);
						objPath = inputMap.get(field)[1];
						method.setAccessible(true);
						method.invoke(this, objPath, value);
						break;
					}
				} catch (Exception e) {
					result = false;
					reportFailed(String.format("Input [%s]", field), e.toString());
				}
			}
		}
		return result;
	}

	/**
	 * Get WebElement
	 *
	 * @return WebElement
	 * @author LamHD4
	 */
	public WebElement getWebElement(String objPath, String firstElement, String secondElement) {
		By objBy = By.xpath(String.format(objPath, firstElement, secondElement));
		return Global.webDriver.findElement(objBy);
	}

	/**
	 * Get Element Attribute
	 *
	 * @return String
	 * @author LamHD4
	 */
	public String getAttributeOfElement(WebElement objElement, String attributeName) {
		return objElement.getAttribute(attributeName);
	}

	/**
	 * Check Element is Present
	 * 
	 * @return boolean
	 * @author LamHD4
	 */
	public boolean isElementPresent(String objStr) {
		String objPath = Global.propObjRepo.getProperty(objStr + ".XPath");
		By objBy = By.xpath(objPath);
		boolean result = true;
		try {
			Global.webDriver.findElement(objBy).getSize();
			result = true;
		} catch (Exception e) {
			result = false;
		}
		return result;
	}

	public boolean isDisplayedUntil(By objBy, int timeout) {
		LOGGER.debug("Wait until object [{}] display within [{}] seconds", objBy, timeout);
		setBrowserNoWait();
		Wait<WebDriver> wait = new FluentWait<WebDriver>(Global.webDriver).withTimeout(Duration.ofSeconds(timeout))
				.pollingEvery(Duration.ofMillis(250)).ignoring(NoSuchElementException.class)
				.ignoring(StaleElementReferenceException.class);
		boolean result = false;
		try {
			result = wait.until(driver -> {
				driver.getPageSource();
				boolean temp = driver.findElement(objBy).isDisplayed();
				if (!temp)
					LOGGER.debug(String.format("Object [%s] still NOT displayed", objBy.toString()));
				return temp;
			});
			result = wait.until(ExpectedConditions.visibilityOfElementLocated(objBy)) != null;
		} catch (TimeoutException ex) {
			LOGGER.warn(String.format("Can't select [%s]", objBy));
		}
		setBrowserWaitDefault();
		return result;
	}

	public boolean isDisplayedUntil(String objPath, int timeout) {
		LOGGER.debug("Wait until object [{}] display within [{}] seconds", objPath, timeout);
		By objBy = getBy(objPath);
		setBrowserNoWait();
		Wait<WebDriver> wait = new FluentWait<WebDriver>(Global.webDriver).withTimeout(Duration.ofSeconds(timeout))
				.pollingEvery(Duration.ofMillis(250)).ignoring(NoSuchElementException.class)
				.ignoring(StaleElementReferenceException.class);
		boolean result = false;
		try {
			result = wait.until(driver -> {
				driver.getPageSource();
				boolean temp = driver.findElement(objBy).isDisplayed();
				if (!temp)
					LOGGER.debug(String.format("Object [%s] still NOT displayed", objBy.toString()));
				return temp;
			});
			result = wait.until(ExpectedConditions.visibilityOfElementLocated(objBy)) != null;
		} catch (TimeoutException ex) {
			LOGGER.warn(String.format("Can't select [%s]", objBy));
		}
		setBrowserWaitDefault();
		return result;
	}

	public String getSelectedDropdown(String objPath) {
		LOGGER.info("Start get value of Selected Dropdown list");
		By objBy = getBy(objPath);
		Select select = new Select(getElement(objBy));
		WebElement option = select.getFirstSelectedOption();
		String defaultItem = option.getText();
		return defaultItem;
	}

	/***
	 * Check value is Double
	 * 
	 * @param string
	 * @return true /false
	 */
	public boolean isDouble(String string) {
		LOGGER.debug("Check type of value [{}] is Double", string);
		try {
			Double.parseDouble(string);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}

//	/***
//	 * Override initialize() method of WebBaseTest
//	 * 
//	 */
//	@Override
//	public void initialize() {
//		LOGGER.info("[akaAT] UAT_BROWSER = " + Global.UAT_BROWSER);
//
//		if (Global.webDriver != null)
//			Global.webDriver.quit();
//
//		try {
//			initializeWebDriver();
//		} catch (Exception e) {
//			LOGGER.error(e.toString());
//			LOGGER.error("Driver initialize failed.");
//			Global.exitAKAAT(1);
//		}
//
//		Global.webDriver = this.webDriver;
//
//		setWaitTimeOut(Global.UAT_OBJECT_TIMEOUT);
//	}
//
//	/***
//	 * Initialize new WebDriver
//	 * 
//	 */
//	private void initializeWebDriver() {
//		LOGGER.info("Initialize Browser with custom options...");
//		if (Global.CHROME_DRIVER.equalsIgnoreCase(Global.UAT_BROWSER)) {
//			String chromeDriver = System.getProperty("webdriver.chrome.driver");
//			if (chromeDriver == null || chromeDriver.isEmpty()) {
//				if (Utils.isWindows())
//					System.setProperty("webdriver.chrome.driver", Global.FolderDriver + "chromedriver.exe");
//				else if (Utils.isMac())
//					System.setProperty("webdriver.chrome.driver", Global.FolderDriver + "chromedriver_mac64");
//			}
//		}
//
//		ChromeOptions options = new ChromeOptions();
//
//		options.addArguments("--start-maximized", "--disable-extensions", "--no-proxy-server");
//
//		if (Global.UAT_BROWSER_BINARY != null && !Global.UAT_BROWSER_BINARY.isEmpty())
//			options.setBinary(Global.UAT_BROWSER_BINARY);
//
//		if (Global.CHROME_HEADLESS)
//			options.addArguments("--headless");
//
//		HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
//		chromePrefs.put("profile.default_content_settings.popups", false);
//		chromePrefs.put("plugins.always_open_pdf_externally", true);
//		chromePrefs.put("plugins.plugins_disabled", "Chrome PDF Viewer");
//		chromePrefs.put("download.default_directory", pdfDir);
//		chromePrefs.put("download.prompt_for_download", false);
//		chromePrefs.put("download.directory_upgrade", true);
//		if (pdfDir != null && !pdfDir.isEmpty())
//			options.setExperimentalOption("prefs", chromePrefs);
//
//		this.webDriver = new ChromeDriver(options);
//	}
}