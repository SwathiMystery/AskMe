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

import com.mongodb.DB;
import com.mongodb.DBCollection;

/**
 * A Data Access Object class with the collection questions, performing all
 * necessary operations or querying on it.
 * 
 * @author Swathi V
 * 
 */
public class QuestionDAO {

	/** Instance of collection **/
	private final DBCollection questionsCollection;

	/**
	 * Gets the questions collection from the database.
	 * 
	 * @param database
	 *            is the database
	 */
	public QuestionDAO(DB database) {
		questionsCollection = database.getCollection("questions");
	}

}
