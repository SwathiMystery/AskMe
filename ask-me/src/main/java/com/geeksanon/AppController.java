/**
 * 
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
	 *            the URIString to make connection. Can be default to localhost.
	 */
	public static void main(String[] args) {

	}
}
