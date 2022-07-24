package com.uchump.prime.Metatron.Lib._HTTP._Spark;

import static com.uchump.prime.Core.uAppUtils.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Instant;
import java.util.Date;

public class Response {

	private HttpServletResponse response;
	private String body;

	protected Response() {
		// Used by wrapper
	}

	Response(HttpServletResponse response) {
		this.response = response;
	}

	/**
	 * Sets the status code for the
	 *
	 * @param statusCode the status code
	 */
	public void status(int statusCode) {
		response.setStatus(statusCode);
	}

	/**
	 * Returns the status code
	 *
	 * @return the status code
	 */
	public int status() {
		return response.getStatus();
	}

	/**
	 * Sets the content type for the response
	 *
	 * @param contentType the content type
	 */
	public void type(String contentType) {
		response.setContentType(contentType);
	}

	/**
	 * Returns the content type
	 *
	 * @return the content type
	 */
	public String type() {
		return response.getContentType();
	}

	/**
	 * Sets the body
	 *
	 * @param body the body
	 */
	public void body(String body) {
		this.body = body;
	}

	/**
	 * returns the body
	 *
	 * @return the body
	 */
	public String body() {
		return this.body;
	}

	/**
	 * @return the raw response object handed in by Jetty
	 */
	public HttpServletResponse raw() {
		return response;
	}

	/**
	 * Trigger a browser redirect
	 *
	 * @param location Where to redirect
	 */
	public void redirect(String location) {

		Log("Redirecting ({} {} to {}" + "Found: " + HttpServletResponse.SC_FOUND + " " + location);

		try {
			response.sendRedirect(location);
		} catch (IOException ioException) {
			Log("Redirect failure: " + ioException);
		}
	}

	/**
	 * Trigger a browser redirect with specific http 3XX status code.
	 *
	 * @param location       Where to redirect permanently
	 * @param httpStatusCode the http status code
	 */
	public void redirect(String location, int httpStatusCode) {

		Log("Redirecting ({} to {} " + httpStatusCode + " " + location);

		response.setStatus(httpStatusCode);
		response.setHeader("Location", location);
		response.setHeader("Connection", "close");
		try {
			response.sendError(httpStatusCode);
		} catch (IOException e) {
			Log("Exception when trying to redirect permanently " + e);
		}
	}

	/**
	 * Adds/Sets a response header
	 *
	 * @param header the header
	 * @param value  the value
	 */
	public void header(String header, String value) {
		response.addHeader(header, value);
	}

	/**
	 * Adds/Sets a response header
	 *
	 * @param header the header
	 * @param value  the value
	 */
	public void header(String header, int value) {
		response.addIntHeader(header, value);
	}

	/**
	 * Adds/Sets a response header
	 *
	 * @param header the header
	 * @param value  the value
	 */
	public void header(String header, Date value) {
		response.addDateHeader(header, value.getTime());
	}

	/**
	 * Adds/Sets a response header
	 *
	 * @param header the header
	 * @param value  the value
	 */
	public void header(String header, java.sql.Date value) {
		response.addDateHeader(header, value.getTime());
	}

	/**
	 * Adds/Sets a response header
	 *
	 * @param header the header
	 * @param value  the value
	 */
	public void header(String header, Instant value) {
		response.addDateHeader(header, value.toEpochMilli());
	}

	/**
	 * Adds not persistent cookie to the response. Can be invoked multiple times to
	 * insert more than one cookie.
	 *
	 * @param name  name of the cookie
	 * @param value value of the cookie
	 */
	public void cookie(String name, String value) {
		cookie(name, value, -1, false);
	}

	/**
	 * Adds cookie to the response. Can be invoked multiple times to insert more
	 * than one cookie.
	 *
	 * @param name   name of the cookie
	 * @param value  value of the cookie
	 * @param maxAge max age of the cookie in seconds (negative for the not
	 *               persistent cookie, zero - deletes the cookie)
	 */
	public void cookie(String name, String value, int maxAge) {
		cookie(name, value, maxAge, false);
	}

	/**
	 * Adds cookie to the response. Can be invoked multiple times to insert more
	 * than one cookie.
	 *
	 * @param name    name of the cookie
	 * @param value   value of the cookie
	 * @param maxAge  max age of the cookie in seconds (negative for the not
	 *                persistent cookie, zero - deletes the cookie)
	 * @param secured if true : cookie will be secured
	 */
	public void cookie(String name, String value, int maxAge, boolean secured) {
		cookie(name, value, maxAge, secured, false);
	}

	/**
	 * Adds cookie to the response. Can be invoked multiple times to insert more
	 * than one cookie.
	 *
	 * @param name     name of the cookie
	 * @param value    value of the cookie
	 * @param maxAge   max age of the cookie in seconds (negative for the not
	 *                 persistent cookie, zero - deletes the cookie)
	 * @param secured  if true : cookie will be secured
	 * @param httpOnly if true: cookie will be marked as http only
	 */
	public void cookie(String name, String value, int maxAge, boolean secured, boolean httpOnly) {
		cookie("", "", name, value, maxAge, secured, httpOnly);
	}

	/**
	 * Adds cookie to the response. Can be invoked multiple times to insert more
	 * than one cookie.
	 *
	 * @param path    path of the cookie
	 * @param name    name of the cookie
	 * @param value   value of the cookie
	 * @param maxAge  max age of the cookie in seconds (negative for the not
	 *                persistent cookie, zero - deletes the cookie)
	 * @param secured if true : cookie will be secured
	 */
	public void cookie(String path, String name, String value, int maxAge, boolean secured) {
		cookie("", path, name, value, maxAge, secured, false);
	}

	/**
	 * Adds cookie to the response. Can be invoked multiple times to insert more
	 * than one cookie.
	 *
	 * @param path     path of the cookie
	 * @param name     name of the cookie
	 * @param value    value of the cookie
	 * @param maxAge   max age of the cookie in seconds (negative for the not
	 *                 persistent cookie, zero - deletes the cookie)
	 * @param secured  if true : cookie will be secured
	 * @param httpOnly if true: cookie will be marked as http only
	 */
	public void cookie(String path, String name, String value, int maxAge, boolean secured, boolean httpOnly) {
		cookie("", path, name, value, maxAge, secured, httpOnly);
	}

	/**
	 * Adds cookie to the response. Can be invoked multiple times to insert more
	 * than one cookie.
	 *
	 * @param domain   domain of the cookie
	 * @param path     path of the cookie
	 * @param name     name of the cookie
	 * @param value    value of the cookie
	 * @param maxAge   max age of the cookie in seconds (negative for the not
	 *                 persistent cookie, zero - deletes the cookie)
	 * @param secured  if true : cookie will be secured
	 * @param httpOnly if true: cookie will be marked as http only
	 */
	public void cookie(String domain, String path, String name, String value, int maxAge, boolean secured,
			boolean httpOnly) {
		Cookie cookie = new Cookie(name, value);
		cookie.setPath(path);
		cookie.setDomain(domain);
		cookie.setMaxAge(maxAge);
		cookie.setSecure(secured);
		cookie.setHttpOnly(httpOnly);
		response.addCookie(cookie);
	}

	/**
	 * Removes the cookie.
	 *
	 * @param name name of the cookie
	 */
	public void removeCookie(String name) {
		removeCookie(null, name);
	}

	/**
	 * Removes the cookie with given path and name.
	 *
	 * @param path path of the cookie
	 * @param name name of the cookie
	 */
	public void removeCookie(String path, String name) {
		Cookie cookie = new Cookie(name, "");
		cookie.setPath(path);
		cookie.setMaxAge(0);
		response.addCookie(cookie);
	}

}