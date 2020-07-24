/*
 * @author Clara Fok A00
 * @version May 27, 2020
 */

package a00.data.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validator {

	/*
	 * method to validate email address according to RFC 5322 standards
	 */
	public static boolean validateEmail(String email) {
		String regex = "^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
		Matcher pattern = Pattern.compile(regex).matcher(email);
		if (pattern.matches()) {
			return true;
		} else {
			return false;
		}
	}

	/*
	 * method to validate Canadian phone number
	 */
	public static boolean validatePhone(String phone) {
		String regex = "^\\(?([0-9]{3})\\)?[-.\\s]?([0-9]{3})[-.\\s]?([0-9]{4})$";
		Matcher pattern = Pattern.compile(regex).matcher(phone);
		if (pattern.matches()) {
			return true;
		} else {
			return false;
		}
	}

	/*
	 * method to validate Canadian Postal Codes
	 * 
	 * @deprecated not applicable for ass 1
	 */
	@Deprecated
	public static boolean validatePostalCode(String postalCode) {
		String regex = "^(?!.*[DFIOQU])[A-VXY][0-9][A-Z] ?[0-9][A-Z][0-9]$";
		Matcher pattern = Pattern.compile(regex).matcher(postalCode);
		if (pattern.matches()) {
			return true;
		} else {
			return false;
		}
	}


	/*
	 * method to validate Strings checks for null and if empty
	 */
	public static boolean validateString(String s) {
		if (s != null && !s.isEmpty()) {
			return true;
		} else {
			return false;
		}
	}

	/*
	 * method to validate date Strings, checks for 8 digits
	 */
	public static boolean validateDateString(String s) {
		if (s.length() == 8) {
			return true;
		} else {
			return false;
		}
	}

	/*
	 * method to validate joined year
	 * 
	 * @param year 		the input year
	 * 
	 * @return true 	if year is between 1980 - 2020
	 */
	public static boolean validateJoinedYear(int year) {
		if (year >= 1980 && year <= 2020) {
			return true;
		} else {
			return false;
		}
	}

	/*
	 * method to validate joined month
	 * 
	 * @param month 	the input month
	 * 
	 * @return true 	if month is between 1 - 12
	 */
	public static boolean validateJoinedMonth(int month) {
		if (month >= 1 && month <= 12) {
			return true;
		} else {
			return false;
		}
	}

	/*
	 * method to validate joined day
	 * 
	 * @param day 		the input day
	 * 		  month 	the input month
	 * 
	 * @return true 	if input day is valid for the input month
	 */
	public static boolean validateJoinedDay(int month, int day) {

		// validate day of month for February
		if (month == 2) {
			if (day >= 1 && day <= 29) {
				return true;
			} else {
				return false;
			}
		}

		// validate day of month for months with 30 days
		else if (month == 4 || month == 6 || month == 9 || month == 11) {
			if (day >= 1 && day <= 30) {
				return true;
			} else {
				return false;
			}
		}

		// validate day of moth for months with 31 days
		else {
			if (day >= 1 && day <= 31) {
				return true;
			} else {
				return false;
			}
		}
	}
}
