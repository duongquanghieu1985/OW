package com.fpt.ivs.akaAT.variable;

import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Class contains methods relate to Clipboard
 * 
 * @author HieuDQ2
 *
 */
public class Clipboard {
	private static final Logger LOGGER = LogManager.getLogger(Clipboard.class);
	
	/**
	 * Private constructor
	 */
	private Clipboard() {
		// Do nothing
	}
	
	/**
	 * Set String value to Clipboard
	 * 
	 * @param value
	 * @author HieuDQ2
	 */
	public static void setValue(String value) {
		LOGGER.debug("Set value [{}] to clipboard", value);
		java.awt.datatransfer.Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		StringSelection str = new StringSelection(value);
		clipboard.setContents(str, null);
	}

	/**
	 * Get String value from Clipboard
	 * 
	 * @return String
	 * @author HieuDQ2
	 */
	public static String getValue() {
		LOGGER.debug("Get value from clipboard");
		String value = "";
		try {
			value = (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
		} catch (HeadlessException | UnsupportedFlavorException | IOException e) {
			LOGGER.error(e.getMessage());
		}
		return value;
	}
}
