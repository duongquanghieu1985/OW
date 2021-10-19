package com.fpt.ivs.akaAT.data;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

public class SupportedData {

	public static final String CITY = "City";

	private static Set<String> list;

	static {
		list = new LinkedHashSet<>(Arrays.asList(CITY));
	}

	/**
	 * Private constructor
	 * 
	 * @author HieuDQ2
	 */
	private SupportedData() {
		// Do nothing
	}

	/**
	 * Get supported list
	 * 
	 * @return Set<String>
	 * @author HieuDQ2
	 */
	public static Set<String> getSupportedList() {
		return list;
	}

}
