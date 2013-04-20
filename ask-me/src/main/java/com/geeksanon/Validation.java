/**
 * Copyright (c) 2013 Swathi Venkatachala
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a
 * copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to permit
 * persons to whom the Software is furnished to do so, subject to the
 * following conditions:
 * 
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS
 * OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN
 * NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE
 * USE OR OTHER DEALINGS IN THE SOFTWARE.
 * 
 * ---------------------------END COPYRIGHT--------------------------------------
 */
package com.geeksanon;

import java.util.HashMap;

/**
 * @author Swathi V
 * 
 */
public class Validation {
	/** R.E for username **/
	private static final String USERNAME_REGEX = "^[a-zA-Z0-9_-]{3,20}$";
	/** R.E for password **/
	private static final String PASSWORD_REGEX = "^[a-z0-9_-]{6,18}$";
	/** R.E. for email **/
	private static final String EMAIL_REGEX = "^([a-z0-9_\\.-]+)@([\\da-z\\.-]+)\\.([a-z\\.]{2,6})$";

	/**
	 * @param username
	 * @param password
	 * @param verifyPassword
	 * @param email
	 * @param rootMap
	 * @return
	 */
	public static boolean validateForm(String username, String password,
			String verifyPassword, String email,
			HashMap<String, String> errorMap) {
		errorMap.put("username_error", "");
		errorMap.put("password_error", "");
		errorMap.put("verify_error", "");
		errorMap.put("email_error", "");
		if (!username.matches(USERNAME_REGEX)) {
			errorMap.put("username_error",
					"Invalid username! Hint: letters,numbers,_,- 3 to 20 characters!");
			return false;
		}

		if (!password.matches(PASSWORD_REGEX)) {
			errorMap.put("password_error", "invalid password.");
			return false;
		}

		if (!password.equals(verifyPassword)) {
			errorMap.put("verify_error", "password must match");
			return false;
		}

		if (!email.equals("")) {
			if (!email.matches(EMAIL_REGEX)) {
				errorMap.put("email_error", "Invalid Email Address");
				return false;
			}
		}

		return true;

	}

}
