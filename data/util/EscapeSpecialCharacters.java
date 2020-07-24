/**
 * Project: A00_ass2
 * File: EscapeSingleQuote.java
 * Date: Jun. 22, 2020
 * Time: 7:41:48 p.m.
 */
package a00.data.util;

/**
 * @author Clara Fok, A00
 *
 */
public class EscapeSpecialCharacters {

	//replaces all single quotes with two singel quotes
	public static String escapeSingleQuote(String s) {

		String newString = s.replaceAll("'", "\\'\\'");
		
		return newString;

	}
	
	
}
