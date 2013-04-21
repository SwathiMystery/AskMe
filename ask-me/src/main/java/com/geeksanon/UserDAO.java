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

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

import org.apache.log4j.Logger;

import sun.misc.BASE64Encoder;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoException;

/**
 * A Data Access Object class with the collection users, performing all
 * necessary operations or querying on it.
 * 
 * @author Swathi V
 * 
 */
public class UserDAO {

	/** Instance of collection **/
	private final DBCollection userCollection;

	/** Obtain the logger instance for logging **/
	private static final Logger LOGGER = Logger.getLogger(UserDAO.class);

	/**
	 * Gets the users collection from the database.
	 * 
	 * @param database
	 *            is the database
	 */
	public UserDAO(DB database) {
		userCollection = database.getCollection("users");
	}

	/**
	 * @param username
	 * @param password
	 * @return
	 */
	public DBObject validateLoginCred(String username, String password) {
		DBObject user = userCollection.findOne(new BasicDBObject("_id",
				username));
		if (user == null) {
			LOGGER.error("User: " + username + " not found!");
			return null;
		}
		String hashedPassword = user.get("password").toString();
		String extra = hashedPassword.split(",")[1];
		if (!hashedPassword.equals(createHashPassword(password, extra))) {
			LOGGER.error("Password does not match!");
			return null;
		}
		return user;
	}

	/**
	 * @param password
	 * @param extra
	 * @return
	 */
	private String createHashPassword(String password, String extra) {
		try {
			String passwordExtra = password + "," + extra;
			MessageDigest messageDigest = MessageDigest.getInstance("MD5");
			messageDigest.update(passwordExtra.getBytes());
			BASE64Encoder encoder = new BASE64Encoder();
			byte hashedBytes[] = (new String(messageDigest.digest(), "UTF-8"))
					.getBytes();
			return encoder.encode(hashedBytes) + "," + extra;
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("MD5 algorithm is not available", e);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("UTF-8 Format unavailable?", e);
		}
	}

	/**
	 * Add the user to the database.
	 * 
	 * @param username
	 *            username
	 * @param password
	 *            password
	 * @param email
	 *            email
	 * @return boolean if added/ not
	 */
	public boolean addUser(String username, String password, String email) {
		String hashedPassword = createHashPassword(password,
				Integer.toString(new SecureRandom().nextInt()));
		BasicDBObject user = new BasicDBObject().append("_id", username)
				.append("password", hashedPassword);
		if (email != null && !email.equals("")) {
			user.append("email", email);
		}
		try {
			userCollection.insert(user);
			return true;
		} catch (MongoException.DuplicateKey e) {
			LOGGER.error("Username exists!");
			return false;
		}
	}
}
