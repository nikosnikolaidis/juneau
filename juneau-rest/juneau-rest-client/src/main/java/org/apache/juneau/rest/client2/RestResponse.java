// ***************************************************************************************************************************
// * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements.  See the NOTICE file *
// * distributed with this work for additional information regarding copyright ownership.  The ASF licenses this file        *
// * to you under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance            *
// * with the License.  You may obtain a copy of the License at                                                              *
// *                                                                                                                         *
// *  http://www.apache.org/licenses/LICENSE-2.0                                                                             *
// *                                                                                                                         *
// * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an  *
// * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the        *
// * specific language governing permissions and limitations under the License.                                              *
// ***************************************************************************************************************************
package org.apache.juneau.rest.client2;

import org.apache.juneau.parser.*;

import static org.apache.juneau.httppart.HttpPartType.*;

import java.lang.reflect.*;
import java.util.*;
import java.util.function.*;

import org.apache.http.*;
import org.apache.http.message.*;
import org.apache.http.params.*;
import org.apache.http.util.*;
import org.apache.juneau.*;
import org.apache.juneau.httppart.*;
import org.apache.juneau.httppart.bean.*;
import org.apache.juneau.utils.*;

/**
 * Represents a response from a remote REST resource.
 *
 * <p>
 * Instances of this class are created by calling the {@link RestRequest#run()} method.
 *
 * <ul class='seealso'>
 * 	<li class='jc'>{@link RestClient}
 * 	<li class='link'>{@doc juneau-rest-client}
 * </ul>
 */
public final class RestResponse implements HttpResponse {

	private final RestClient client;
	private final RestRequest request;
	private final HttpResponse response;
	private final Parser parser;
	private HttpPartParserSession partParser;
	private RestResponseBody responseBody;
	private boolean isClosed;

	/**
	 * Constructor.
	 * @param client The RestClient that created this response.
	 * @param request The REST request.
	 * @param response The HTTP response.  Can be <jk>null</jk>.
	 * @param parser The overridden parser passed into {@link RestRequest#parser(Parser)}.
	 */
	protected RestResponse(RestClient client, RestRequest request, HttpResponse response, Parser parser) {
		this.client = client;
		this.request = request;
		this.parser = parser;
		this.response = response == null ? new BasicHttpResponse(null, 0, null) : response;
		this.responseBody = new RestResponseBody(client, request, this, parser);
		this.partParser = client.getPartParserSession();
	}

	//------------------------------------------------------------------------------------------------------------------
	// Setters
	//------------------------------------------------------------------------------------------------------------------

	/**
	 * Consumes the response body.
	 *
	 * <p>
	 * This is equivalent to closing the input stream.
	 *
	 * @return This object (for method chaining).
	 * @throws RestCallException If one of the {@link RestCallInterceptor RestCallInterceptors} threw an exception.
	 */
	public RestResponse consume() throws RestCallException {
		close();
		return this;
	}

	//------------------------------------------------------------------------------------------------------------------
	// Status line
	//------------------------------------------------------------------------------------------------------------------

	/**
	 * Same as {@link #getStatusLine()} but sets the value in a mutable for fluent calls.
	 *
	 * @param m The mutable to set the status line in.
	 * @return This object (for method chaining).
	 */
	public RestResponse getStatusLine(Mutable<StatusLine> m) {
		m.set(getStatusLine());
		return this;
	}

	/**
	 * Returns the status code of the response.
	 *
	 * Shortcut for calling <code>getStatusLine().getStatusCode()</code>.
	 *
	 * @return The status code of the response.
	 */
	public int getStatusCode() {
		StatusLine sl = getStatusLine();
		return sl == null ? -1 : sl.getStatusCode();
	}

	/**
	 * Same as {@link #getStatusCode()} but sets the value in a mutable for fluent calls.
	 *
	 * @param m The mutable to set the status code in.
	 * @return This object (for method chaining).
	 */
	public RestResponse getStatusCode(Mutable<Integer> m) {
		m.set(getStatusCode());
		return this;
	}

	/**
	 * Returns the status line reason phrase of the response.
	 *
	 * Shortcut for calling <code>getStatusLine().getReasonPhrase()</code>.
	 *
	 * @return The status line reason phrase of the response.
	 */
	public String getReasonPhrase() {
		StatusLine sl = getStatusLine();
		return sl == null ? null : sl.getReasonPhrase();
	}

	/**
	 * Same as {@link #getReasonPhrase()} but sets the value in a mutable for fluent calls.
	 *
	 * @param m The mutable to set the status line reason phrase in.
	 * @return This object (for method chaining).
	 */
	public RestResponse getReasonPhrase(Mutable<String> m) {
		m.set(getReasonPhrase());
		return this;
	}

	//------------------------------------------------------------------------------------------------------------------
	// Status line assertions
	//------------------------------------------------------------------------------------------------------------------

	/**
	 * Asserts that the status code on the response is one of the specified values.
	 *
	 * @param validCodes The list of valid codes.
	 * @return This object (for method chaining).
	 * @throws RestCallException If REST call failed.
	 * @throws AssertionError If assertion failed.
	 */
	public RestResponse assertStatusCode(int...validCodes) throws RestCallException, AssertionError {
		int sc = getStatusCode();
		for (int c : validCodes)
			if (c == sc)
				return this;
		throw new BasicAssertionError("Response did not have the expected status code.\n\tExpected=[{0}]\n\tActual=[{1}]", validCodes, sc);
	}

	/**
	 * Asserts that the status code on the response passes the specified predicate test.
	 *
	 * @param test The test.
	 * @return This object (for method chaining).
	 * @throws RestCallException If REST call failed.
	 * @throws AssertionError If assertion failed.
	 */
	public RestResponse assertStatusCode(Predicate<Integer> test) throws RestCallException, AssertionError {
		int sc = getStatusCode();
		if (test.test(sc))
			return this;
		throw new BasicAssertionError("Response did not have the expected status code.\n\tActual=[{0}]", sc);
	}

	//------------------------------------------------------------------------------------------------------------------
	// Headers
	//------------------------------------------------------------------------------------------------------------------

	/**
	 * Shortcut for calling <code>getHeader(name).asString()</code>.
	 *
	 * @param name The header name.
	 * @return The header value, or <jk>null</jk> if header was not found.
	 */
	public String getStringHeader(String name) {
		return getHeader(name).asString();
	}

	/**
	 * Shortcut for calling <code>getHeader(name).asStringOrElse(def)</code>.
	 *
	 * @param name The header name.
	 * @param def The default value if the header was not found.
	 * @return The header value, or the default if header was not found.
	 */
	public String getStringHeader(String name, String def) {
		return getHeader(name).asStringOrElse(def);
	}

	/**
	 * Returns the last header with the specified name.
	 *
	 * Unlike {@link #getFirstHeader(String)} and {@link #getLastHeader(String)}, this method returns an empty
	 * {@link RestResponseHeader} object instead of returning <jk>null</jk>.  This allows it to be used more easily
	 * in fluent calls.
	 *
	 * @param name The header name.
	 * @return The header.  Never <jk>null</jk>.
	 */
	public RestResponseHeader getHeader(String name) {
		return new RestResponseHeader(request, this, getLastHeader(name)).parser(partParser);
	}

	//------------------------------------------------------------------------------------------------------------------
	// Body
	//------------------------------------------------------------------------------------------------------------------

	/**
	 * Returns the body of the response.
	 *
	 * This method can be called multiple times returning the same response body each time.
	 *
	 * @return The body of the response.
	 */
	public RestResponseBody getBody() {
		return responseBody;
	}

	/**
	 * Caches the response body so that it can be read as a stream multiple times.
	 *
	 * This is equivalent to calling the following:
	 * <p class='bcode w800'>
	 * 	getBody().cache();
	 * </p>
	 *
	 * @return The body of the response.
	 */
	public RestResponse cacheBody() {
		responseBody.cache();
		return this;
	}

	@SuppressWarnings("unchecked")
	<T> T as(ResponseBeanMeta rbm) throws RestCallException {
		try {
			Class<T> c = (Class<T>)rbm.getClassMeta().getInnerClass();
			final RestClient rc = this.client;
			final HttpPartParserSession p = partParser == null ? rc.getPartParserSession() : partParser;
			return (T)Proxy.newProxyInstance(
				c.getClassLoader(),
				new Class[] { c },
				new InvocationHandler() {
					@Override /* InvocationHandler */
					public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
						ResponseBeanPropertyMeta pm = rbm.getProperty(method.getName());
						if (pm != null) {
							HttpPartParserSession pp = pm.getParser(p);
							HttpPartSchema schema = pm.getSchema();
							String name = pm.getPartName();
							ClassMeta<?> type = rc.getClassMeta(method.getGenericReturnType());
							HttpPartType pt = pm.getPartType();
							if (pt == RESPONSE_BODY)
								return getBody().schema(schema).as(type);
							if (pt == RESPONSE_HEADER)
								return getHeader(name).parser(pp).schema(schema).as(type);
							if (pt == RESPONSE_STATUS)
								return getStatusCode();
						}
						return null;
					}

			});
		} catch (Exception e) {
			throw new RestCallException(e);
		}
	}

	// -----------------------------------------------------------------------------------------------------------------
	// HttpResponse pass-through methods.
	// -----------------------------------------------------------------------------------------------------------------

	/**
	 * Obtains the status line of this response.
	 *
	 * The status line can be set using one of the setStatusLine methods, or it can be initialized in a constructor.
	 *
	 * @return The status line, or <jk>null</jk> if not yet set.
	 */
	@Override /* HttpResponse */
	public StatusLine getStatusLine() {
		return response.getStatusLine();
	}

	/**
	 * Sets the status line of this response.
	 *
	 * @param statusline The status line of this response
	 */
	@Override /* HttpResponse */
	public void setStatusLine(StatusLine statusline) {
		response.setStatusLine(statusline);
	}

	/**
	 * Sets the status line of this response.
	 *
	 * <p>
	 * The reason phrase will be determined based on the current locale.
	 *
	 * @param ver The HTTP version.
	 * @param code The status code.
	 */
	@Override /* HttpResponse */
	public void setStatusLine(ProtocolVersion ver, int code) {
		response.setStatusLine(ver, code);
	}

	/**
	 * Sets the status line of this response with a reason phrase.
	 *
	 * @param ver The HTTP version.
	 * @param code The status code.
	 * @param reason The reason phrase, or <jk>null</jk> to omit.
	 */
	@Override /* HttpResponse */
	public void setStatusLine(ProtocolVersion ver, int code, String reason) {
		response.setStatusLine(ver, code, reason);
	}

	/**
	 * Updates the status line of this response with a new status code.
	 *
	 * @param code The HTTP status code.
	 * @throws IllegalStateException If the status line has not be set.
	 */
	@Override /* HttpResponse */
	public void setStatusCode(int code) {
		response.setStatusCode(code);
	}

	/**
	 * Updates the status line of this response with a new reason phrase.
	 *
	 * @param reason The new reason phrase as a single-line string, or <jk>null</jk> to unset the reason phrase.
	 * @throws IllegalStateException If the status line has not be set.
	 */
	@Override /* HttpResponse */
	public void setReasonPhrase(String reason) {
		response.setReasonPhrase(reason);
	}

	/**
	 * Obtains the message entity of this response.
	 *
	 * <p>
	 * The entity is provided by calling setEntity.
	 *
	 * <ul class='notes'>
	 * 	<li>Unlike the {@link HttpResponse#getEntity()} method, this method never returns a <jk>null</jk> response.
	 * 		Instead, <c>getBody().isPresent()</c> can be used to determine whether the response has a body.
	 * </ul>
	 *
	 * @return The response entity.  Never <jk>null</jk>.
	 */
	@Override /* HttpResponse */
	public RestResponseBody getEntity() {
		return responseBody;
	}

	/**
	 * Associates a response entity with this response.
	 *
	 * <ul class='notes'>
	 * 	<li>If an entity has already been set for this response and it depends on an input stream
	 * 		({@link HttpEntity#isStreaming()} returns <jk>true</jk>), it must be fully consumed in order to ensure
	 * 		release of resources.
	 * </ul>
	 *
	 * @param entity The entity to associate with this response, or <jk>null</jk> to unset.
	 */
	@Override /* HttpResponse */
	public void setEntity(HttpEntity entity) {
		response.setEntity(entity);
		this.responseBody = new RestResponseBody(client, request, this, parser);
	}

	/**
	 * Obtains the locale of this response.
	 *
	 * The locale is used to determine the reason phrase for the status code.
	 * It can be changed using {@link #setLocale(Locale)}.
	 *
	 * @return The locale of this response, never <jk>null</jk>.
	 */
	@Override /* HttpResponse */
	public Locale getLocale() {
		return response.getLocale();
	}

	/**
	 * Changes the locale of this response.
	 *
	 * @param loc The new locale.
	 */
	@Override /* HttpResponse */
	public void setLocale(Locale loc) {
		response.setLocale(loc);
	}

	/**
	 * Returns the protocol version this message is compatible with.
	 *
	 * @return The protocol version this message is compatible with.
	 */
	@Override /* HttpMessage */
	public ProtocolVersion getProtocolVersion() {
		return response.getProtocolVersion();
	}

	/**
	 * Checks if a certain header is present in this message.
	 *
	 * <p>
	 * Header values are ignored.
	 *
	 * @param name The header name to check for.
	 * @return <jk>true</jk> if at least one header with this name is present.
	 */
	@Override /* HttpMessage */
	public boolean containsHeader(String name) {
		return response.containsHeader(name);
	}

	/**
	 * Returns all the headers with a specified name of this message.
	 *
	 * Header values are ignored.
	 * <br>Headers are ordered in the sequence they were sent over a connection.
	 *
	 * @param name The name of the headers to return.
	 * @return All the headers with a specified name of this message.
	 */
	@Override /* HttpMessage */
	public RestResponseHeader[] getHeaders(String name) {
		Header[] a = response.getHeaders(name);
		RestResponseHeader[] b = new RestResponseHeader[a.length];
		for (int i = 0; i < a.length; i++)
			b[i] = new RestResponseHeader(request, this, a[i]).parser(partParser);
		return b;
	}

	/**
	 * Returns the first header with a specified name of this message.
	 *
	 * Header values are ignored.
	 * <br>If there is more than one matching header in the message the first element of {@link #getHeaders(String)} is returned.
	 *
	 * @param name The name of the header to return.
	 * @return The header, or <jk>null</jk> if there is no matching header in the message.
	 */
	@Override /* HttpMessage */
	public RestResponseHeader getFirstHeader(String name) {
		Header h = response.getFirstHeader(name);
		return h == null ? null : new RestResponseHeader(request, this, h).parser(partParser);
	}

	/**
	 * Returns the last header with a specified name of this message.
	 *
	 * Header values are ignored.
	 * <br>?If there is more than one matching header in the message the last element of {@link #getHeaders(String)} is returned.
	 *
	 * @param name The name of the header to return.
	 * @return The header, or <jk>null</jk> if there is no matching header in the message.
	 */
	@Override /* HttpMessage */
	public RestResponseHeader getLastHeader(String name) {
		Header h = response.getLastHeader(name);
		return new RestResponseHeader(request, this, h).parser(partParser);
	}

	/**
	 * Returns all the headers of this message.
	 *
	 * Headers are ordered in the sequence they were sent over a connection.
	 *
	 * @return All the headers of this message.
	 */
	@Override /* HttpMessage */
	public RestResponseHeader[] getAllHeaders() {
		Header[] a = response.getAllHeaders();
		RestResponseHeader[] b = new RestResponseHeader[a.length];
		for (int i = 0; i < a.length; i++)
			b[i] = new RestResponseHeader(request, this, a[i]).parser(partParser);
		return b;
	}

	/**
	 * Adds a header to this message.
	 *
	 * The header will be appended to the end of the list.
	 *
	 * @param header The header to append.
	 */
	@Override /* HttpMessage */
	public void addHeader(Header header) {
		response.addHeader(header);
	}

	/**
	 * Adds a header to this message.
	 *
	 * The header will be appended to the end of the list.
	 *
	 * @param name The name of the header.
	 * @param value The value of the header.
	 */
	@Override /* HttpMessage */
	public void addHeader(String name, String value) {
		response.addHeader(name, value);
	}

	/**
	 * Overwrites the first header with the same name.
	 *
	 * The new header will be appended to the end of the list, if no header with the given name can be found.
	 *
	 * @param header The header to set.
	 */
	@Override /* HttpMessage */
	public void setHeader(Header header) {
		response.setHeader(header);
	}

	/**
	 * Overwrites the first header with the same name.
	 *
	 * The new header will be appended to the end of the list, if no header with the given name can be found.
	 *
	 * @param name The name of the header.
	 * @param value The value of the header.
	 */
	@Override /* HttpMessage */
	public void setHeader(String name, String value) {
		response.setHeader(name, value);
	}

	/**
	 * Overwrites all the headers in the message.
	 *
	 * @param headers The array of headers to set.
	 */
	@Override /* HttpMessage */
	public void setHeaders(Header[] headers) {
		response.setHeaders(headers);
	}

	/**
	 * Removes a header from this message.
	 *
	 * @param header The header to remove.
	 */
	@Override /* HttpMessage */
	public void removeHeader(Header header) {
		response.removeHeader(header);
	}

	/**
	 * Removes all headers with a certain name from this message.
	 *
	 * @param name The name of the headers to remove.
	 */
	@Override /* HttpMessage */
	public void removeHeaders(String name) {
		response.removeHeaders(name);
	}

	/**
	 * Returns an iterator of all the headers.
	 *
	 * @return {@link Iterator} that returns {@link Header} objects in the sequence they are sent over a connection.
	 */
	@Override /* HttpMessage */
	public HeaderIterator headerIterator() {
		return response.headerIterator();
	}

	/**
	 * Returns an iterator of the headers with a given name.
	 *
	 * @param name The name of the headers over which to iterate, or <jk>null</jk> for all headers.
	 * @return {@link Iterator} that returns {@link Header} objects with the argument name in the sequence they are sent over a connection.
	 */
	@Override /* HttpMessage */
	public HeaderIterator headerIterator(String name) {
		return response.headerIterator(name);
	}

	/**
	 * Returns the parameters effective for this message as set by {@link #setParams(HttpParams)}.
	 *
	 * @return The parameters effective for this message as set by {@link #setParams(HttpParams)}.
	 * @deprecated Use configuration classes provided <jk>org.apache.http.config</jk> and <jk>org.apache.http.client.config</jk>.
	 */
	@Override /* HttpMessage */
	@Deprecated
	public HttpParams getParams() {
		return response.getParams();
	}

	/**
	 * Provides parameters to be used for the processing of this message.
	 *
	 * @param params The parameters.
	 * @deprecated Use configuration classes provided <jk>org.apache.http.config</jk> and <jk>org.apache.http.client.config</jk>.
	 */
	@Override /* HttpMessage */
	@Deprecated
	public void setParams(HttpParams params) {
		response.setParams(params);
	}

	void close() throws RestCallException {
		if (isClosed)
			return;
		isClosed = true;
		EntityUtils.consumeQuietly(response.getEntity());

		if (client.logRequestsPredicate.test(request, this)) {
			if (client.logRequests == DetailLevel.SIMPLE) {
				client.log(client.logRequestsLevel, "HTTP {0} {1}, {2}", request.getMethod(), request.getURI(), this.getStatusLine());
			} else if (client.logRequests == DetailLevel.FULL) {
				String output = getBody().asString();
				StringBuilder sb = new StringBuilder();
				sb.append("\n=== HTTP Call (outgoing) ======================================================");
				sb.append("\n=== REQUEST ===\n");
				sb.append(request.getMethod()).append(" ").append(request.getURI());
				sb.append("\n---request headers---");
				for (Header h : request.getAllHeaders())
					sb.append("\n\t").append(h);
				if (request.hasHttpEntity()) {
					sb.append("\n---request entity---");
					HttpEntity e = request.getHttpEntity();
					if (e == null)
						sb.append("\nEntity is null");
					else {
						if (e.getContentType() != null)
							sb.append("\n").append(e.getContentType());
						if (e.getContentEncoding() != null)
							sb.append("\n").append(e.getContentEncoding());
						if (e.isRepeatable()) {
							try {
								sb.append("\n---request content---\n").append(EntityUtils.toString(e));
							} catch (Exception ex) {
								throw new RuntimeException(ex);
							}
						}
					}
				}
				sb.append("\n=== RESPONSE ===\n").append(getStatusLine());
				sb.append("\n---response headers---");
				for (Header h : getAllHeaders())
					sb.append("\n\t").append(h);
				sb.append("\n---response content---\n").append(output);
				sb.append("\n=== END =======================================================================");
				client.log(client.logRequestsLevel, sb.toString());
			}
		}

		for (RestCallInterceptor r : request.interceptors) {
			try {
				r.onClose(request, this);
			} catch (Exception e) {
				throw RestCallException.create(e);
			}
		}
	}

	//------------------------------------------------------------------------------------------------------------------
	// Other methods
	//------------------------------------------------------------------------------------------------------------------

	HttpResponse asHttpResponse() {
		return response;
	}

}
