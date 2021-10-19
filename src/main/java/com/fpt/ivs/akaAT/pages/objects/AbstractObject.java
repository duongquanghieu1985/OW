package com.fpt.ivs.akaAT.pages.objects;

import org.openqa.selenium.WebElement;

import com.fpt.ivs.akaAT.WebBaseTestExtend;

/**
 * @author HieuDQ2
 *
 */
public abstract class AbstractObject extends WebBaseTestExtend {
	protected WebElement objectElement;
	protected String objectName;

	/**
	 * Default constructor
	 * 
	 * @param objName
	 * @param element
	 * @author HieuDQ2
	 */
	protected AbstractObject(String objName, WebElement element) {
		objectElement = element;
		objectName = objName;
	}
}
