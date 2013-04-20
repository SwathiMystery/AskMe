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
