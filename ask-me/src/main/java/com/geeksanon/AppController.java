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

import java.net.UnknownHostException;

import org.apache.log4j.Logger;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

import freemarker.template.Configuration;

/**
 * This class is responsible for controlling the AskMe application. </p>
 * Interaction with all the questions, answers, users and their session is from
 * this class.
 * 
 * @author Swathi V
 * 
 */
public class AppController {

	/** Instance of freemarker configuration **/
	private final Configuration configuration;
	/** Obatin the logger instance for logging **/
	private static final Logger LOGGER = Logger.getLogger(AppController.class);

	/**
	 * Default constructor
	 */
	public AppController() {
		configuration = loadTemplateConfiguration();
	}

	/**
	 * Method to make a connection to the database, with the URI.
	 * 
	 * @param URIString
	 *            URI passed other than the localhost
	 * @throws UnknownHostException
	 *             error when host is not known
	 */
	public AppController(String URIString) throws UnknownHostException {
		MongoClient mongoClient = new MongoClient(new MongoClientURI(URIString));
		DB database = mongoClient.getDB("askme");
		configuration = loadTemplateConfiguration();
	}

	/**
	 * Getter of the configuration loaded from template.
	 * 
	 * @return the configuration
	 */
	public Configuration getConfiguration() {
		return configuration;
	}

	/**
	 * Method to load the templates for this class.
	 * 
	 * @return configuration instance of freemarker
	 */
	private Configuration loadTemplateConfiguration() {
		Configuration loadConf = new Configuration();
		loadConf.setClassForTemplateLoading(AppController.class, "/templates");
		return loadConf;
	}

	/**
	 * Entry point to the controller.
	 * 
	 * @param args
	 *            the URIString to make connection. Can be default to localhost
	 * @throws UnknownHostException
	 *             if hostname is not known
	 */
	public static void main(String[] args) throws UnknownHostException {
		if (args.length == 0) {
			LOGGER.warn("Using the default hostname : localhost");
			LOGGER.warn("If you want to use different hostname, run with the $HOSTNAME argument!");
			new AppController("mongodb://localhost");
		} else {
			new AppController(args[0]);
		}
	}
}
