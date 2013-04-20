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

import java.security.SecureRandom;

import sun.misc.BASE64Encoder;

import com.mongodb.BasicDBObject;
import com.mongodb.Bytes;
import com.mongodb.DB;
import com.mongodb.DBCollection;

/**
 * @author Swathi V
 * 
 */
public class SessionDAO {

	/** Instance of collection **/
	private final DBCollection sessionsCollection;

	/**
	 * Gets the sessions collection from the database.
	 * 
	 * @param database
	 */
	public SessionDAO(DB database) {
		sessionsCollection = database.getCollection("sessions");
	}

	/**
	 * Insert the session ID and username in the sessions collection, where the
	 * ID session ID generated is 32bytes.
	 * 
	 * @param string
	 * @return
	 */
	public String startSession(String username) {
		SecureRandom secureRandom = new SecureRandom();
		byte randomBytes[] = new byte[32];
		secureRandom.nextBytes(randomBytes);
		BASE64Encoder encoder = new BASE64Encoder();
		String sessionID = encoder.encode(randomBytes);

		BasicDBObject session = new BasicDBObject("username", username).append(
				"_id", sessionID);

		sessionsCollection.insert(session);
		return session.get("_id").toString();
	}

}
