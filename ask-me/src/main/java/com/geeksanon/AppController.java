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

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;

import javax.servlet.http.Cookie;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.log4j.Logger;

import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;

import com.mongodb.DB;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

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
	/** Obtain the logger instance for logging **/
	private static final Logger LOGGER = Logger.getLogger(AppController.class);

	/** Instance of QuestionDAO **/
	private final QuestionDAO questionDAO;
	/** Instance of UserDAO **/
	private final UserDAO userDAO;
	/** Instance of SessionDAO **/
	private final SessionDAO sessionDAO;

	/**
	 * Method to make a connection to the database, with the URI.
	 * 
	 * @param URIString
	 *            URI passed other than the localhost
	 * @throws IOException
	 *             when the page is not found.
	 */
	public AppController(String URIString) throws IOException {
		MongoClient mongoClient = new MongoClient(new MongoClientURI(URIString));
		DB database = mongoClient.getDB("askme");
		questionDAO = new QuestionDAO(database);
		userDAO = new UserDAO(database);
		sessionDAO = new SessionDAO(database);

		configuration = loadTemplateConfiguration();
		Spark.setPort(5115);
		intialiseRoutes();
	}

	/**
	 * Initialise the routes with get and post.
	 * 
	 * @throws IOException
	 *             when not found
	 */
	private void intialiseRoutes() throws IOException {

		/**
		 * Handle the login of the user.
		 */
		Spark.get(new Routes("/login", "login.ftl") {

			@Override
			protected void doHandle(Request request, Response response,
					StringWriter writer) throws IOException, TemplateException {
				HashMap<String, String> rootMap = new HashMap<String, String>();
				rootMap.put("username", "");
				rootMap.put("login_error", "");
				template.process(rootMap, writer);
			}
		});

		Spark.post(new Routes("/login", "login.ftl") {

			@Override
			protected void doHandle(Request request, Response response,
					StringWriter writer) throws IOException, TemplateException {
				String username = request.queryParams("username");
				String password = request.queryParams("password");
				LOGGER.info("Username:" + username + "\n" + "Password: "
						+ password);

				DBObject user = userDAO.validateLoginCred(username, password);

				if (user != null) {
					LOGGER.info("Valid user: " + username);
					String sessionID = sessionDAO.startSession(user.get("_id")
							.toString());
					if (sessionID == null) {
						LOGGER.error("SessionID is null");
						response.redirect("/_error");
					} else {
						LOGGER.info("Session ID added to cookie for user:"
								+ username);
						response.raw().addCookie(
								new Cookie("session", sessionID));
						response.redirect("/welcome");
					}
				} else {
					HashMap<String, String> rootMap = new HashMap<String, String>();
					rootMap.put("username",
							StringEscapeUtils.escapeHtml4(username));
					rootMap.put("password", "");
					rootMap.put("login_error", "Invalid Login! Try Again.");
					template.process(rootMap, writer);
				}
			}
		});

		/**
		 * Handle the signup of the user to create an account.
		 */
		Spark.get(new Routes("/signup", "signup.ftl") {

			@Override
			protected void doHandle(Request request, Response response,
					StringWriter writer) throws IOException, TemplateException {
				HashMap<String, String> rootMap = new HashMap<String, String>();
				rootMap.put("username", "");
				rootMap.put("password", "");
				rootMap.put("email", "");
				rootMap.put("username_error", "");
				rootMap.put("password_error", "");
				rootMap.put("verify_error", "");
				rootMap.put("email_error", "");
				template.process(rootMap, writer);
			}
		});

		Spark.post(new Routes("/signup", "signup.ftl") {

			@Override
			protected void doHandle(Request request, Response response,
					StringWriter writer) throws IOException, TemplateException {
				String username = request.queryParams("username");
				String password = request.queryParams("password");
				String verifyPassword = request.queryParams("verify");
				String email = request.queryParams("email");

				HashMap<String, String> rootMap = new HashMap<String, String>();
				rootMap.put("username", StringEscapeUtils.escapeHtml4(username));
				rootMap.put("email", StringEscapeUtils.escapeHtml4(email));
				boolean isValid = Helper.validateForm(username, password,
						verifyPassword, email, rootMap);
				if (isValid) {
					LOGGER.info("Creating user with Username : " + username
							+ "and Password :" + password);
					boolean isAdded = userDAO
							.addUser(username, password, email);
					if (!isAdded) {
						rootMap.put("username_error",
								"Username already exist! Please try another");
						template.process(rootMap, writer);
					} else {
						String sessionID = sessionDAO.startSession(username);
						LOGGER.info("Session ID : " + sessionID);
						response.raw().addCookie(
								new Cookie("session", sessionID));
						response.redirect("/welcome");
					}
				} else {
					LOGGER.error("Validation failed!!");
					template.process(rootMap, writer);
				}
			}
		});

		/**
		 * Welcome note to either ask a question, go-home or logout! Handle
		 * welcome page.
		 */
		Spark.get(new Routes("/welcome", "/welcome_note.ftl") {

			@Override
			protected void doHandle(Request request, Response response,
					StringWriter writer) throws IOException, TemplateException {
				String cookie = Helper.getSessionCookie(request);
				String username = sessionDAO.getUserSessionID(cookie);
				if (username == null) {
					LOGGER.error("Username not found. May be Signup?");
					response.redirect("/signup");
				} else {
					HashMap<String, String> rootMap = new HashMap<String, String>();
					rootMap.put("username", username);
					template.process(rootMap, writer);
				}
			}
		});

		/**
		 * Logout from the current session.
		 */
		Spark.get(new Routes("/logout", "/login.ftl") {

			@Override
			protected void doHandle(Request request, Response response,
					StringWriter writer) throws IOException, TemplateException {
				String sessionID = Helper.getSessionCookie(request);
				if (sessionID == null) {
					response.redirect("/login");
				} else {
					sessionDAO.stopSession(sessionID);
					Cookie cookie = Helper.getSessionCookieActual(request);
					cookie.setMaxAge(0);
					response.raw().addCookie(cookie);
					response.redirect("/login");
				}
			}
		});

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
	 * This is an abstract class for freemarker templates routes created.
	 * 
	 */
	abstract class Routes extends Route {

		final Template template;

		/**
		 * Constructor with the path.
		 * 
		 * @throws IOException
		 *             when no template is found.
		 */
		protected Routes(String path, String templateName) throws IOException {
			super(path);
			template = configuration.getTemplate(templateName);
		}

		@Override
		public Object handle(Request request, Response response) {
			StringWriter writer = new StringWriter();
			try {
				doHandle(request, response, writer);
			} catch (Exception e) {
				e.printStackTrace();
				response.redirect("/_error");
			}
			return writer;
		}

		/**
		 * Handle the routes.
		 * 
		 * @param request
		 *            Request object
		 * @param response
		 *            Response object
		 * @param writer
		 *            StringWriter object
		 */
		protected abstract void doHandle(Request request, Response response,
				StringWriter writer) throws IOException, TemplateException;
	}

	/**
	 * Entry point to the controller.
	 * 
	 * @param args
	 *            the URIString to make connection. Can be default to localhost
	 * @throws IOException
	 *             when not found.
	 */
	public static void main(String[] args) throws IOException {
		if (args.length == 0) {
			LOGGER.warn("Using the default hostname : localhost");
			LOGGER.warn("If you want to use different hostname, run with the $HOSTNAME argument!");
			new AppController("mongodb://localhost");
		} else {
			new AppController(args[0]);
		}
	}
}
