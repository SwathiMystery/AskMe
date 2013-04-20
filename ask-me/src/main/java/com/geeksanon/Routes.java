/**
 * 
 */
package com.geeksanon;

import java.io.IOException;
import java.io.StringWriter;

import freemarker.template.Configuration;
import freemarker.template.Template;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * This is an abstract class for freemarker templates routes created.
 * 
 * @author Swathi V
 * 
 */
abstract class Routes extends Route {

	private final Template template;
	private final Configuration configuration = new AppController()
			.getConfiguration();

	/**
	 * Constructor with the path.
	 * 
	 * @param path
	 *            route to be redirected to
	 * @throws IOException
	 *             when no template is found.
	 */
	protected Routes(String path) throws IOException {
		super(path);
		template = configuration.getTemplate(path);
	}

	/**
	 * Constructor with the path.
	 * 
	 * @param path
	 *            route to be redirected to
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
			StringWriter writer);
}
