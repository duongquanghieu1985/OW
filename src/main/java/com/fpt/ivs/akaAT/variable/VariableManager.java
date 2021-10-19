package com.fpt.ivs.akaAT.variable;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Class contains actions relate to Variable
 * 
 * @author HieuDQ2
 *
 */
public class VariableManager {
	private static final Logger LOGGER = LogManager.getLogger(VariableManager.class);

	private VariableManager() {
	}

	private static Map<String, String> variableList = new HashMap<>();

	public static Map<String, String> getVariableList() {
		return variableList;
	}

	/**
	 * Clear variable list
	 * 
	 * @author HieuDQ2
	 */
	public static void clear() {
		variableList.clear();
	}

	/**
	 * Add variable into list
	 * 
	 * @param name
	 * @param value
	 * @return true / false
	 * @author HieuDQ2
	 */
	public static boolean add(String name, String value) {
		variableList.put(name, value);
		LOGGER.info(String.format("Added variable \"%s\"", name));
		boolean result = false;
		if (variableList.get(name).equals(value))
			result = true;
		return result;
	}

	/**
	 * Get variable by name and set it value to Clipboard for later use
	 * 
	 * @param name
	 * @author HieuDQ2
	 * @return
	 */
	public static String get(String name) {
		String result = null;
		if (variableList.containsKey(name)) {
			result = variableList.get(name);
		} else {
			LOGGER.debug(String.format("Variable [%s] does not exist", name));
		}
		return result;
	}
}
