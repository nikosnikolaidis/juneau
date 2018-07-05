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
package org.apache.juneau.http.annotation;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

import java.lang.annotation.*;
import java.util.*;

import org.apache.juneau.*;
import org.apache.juneau.httppart.*;
import org.apache.juneau.json.*;
import org.apache.juneau.remoteable.*;
import org.apache.juneau.urlencoding.*;

/**
 * REST request body annotation.
 *
 * <p>
 * Identifies a POJO to be used as the header of an HTTP request.
 *
 * <p>
 * Can be used in the following locations:
 * <ul>
 * 	<li>Java method arguments of client-side REST interface proxies.
 * 	<li>Java method arguments of server-side REST Java methods and/or their class types.
 * </ul>
 *
 * <h5 class='topic'>Server-side REST</h5>
 *
 * Annotation that can be applied to a parameter of a <ja>@RestMethod</ja>-annotated method to identify it as a HTTP
 * request header converted to a POJO.
 *
 * <h5 class='section'>Example:</h5>
 * <p class='bcode'>
 * 	<ja>@RestMethod</ja>(name=<jsf>GET</jsf>)
 * 	<jk>public void</jk> doGet(RestRequest req, RestResponse res, <ja>@Header</ja>(<js>"ETag"</js>) UUID etag) {
 * 		...
 * 	}
 * </p>
 *
 * <p>
 * This is functionally equivalent to the following code...
 * <p class='bcode'>
 * 	<ja>@RestMethod</ja>(name=<jsf>GET</jsf>)
 * 	<jk>public void</jk> doPostPerson(RestRequest req, RestResponse res) {
 * 		UUID etag = req.getHeader(UUID.<jk>class</jk>, "ETag");
 * 		...
 * 	}
 * </p>
 *
 * <h5 class='section'>See Also:</h5>
 * <ul>
 * 	<li class='link'><a class="doclink" href="../../../../../overview-summary.html#juneau-rest-server.Header">Overview &gt; juneau-rest-server &gt; @Header</a>
 * 	<li class='link'><a class="doclink" href="https://swagger.io/specification/v2/#parameterObject">Swagger Specification &gt; Parameter Object</a>
 * </ul>
 *
 * <h5 class='topic'>Client-side REST</h5>
 *
 * Annotation applied to Java method arguments of interface proxies to denote that they are serialized as an HTTP
 * header value.
 *
 * <h5 class='section'>Example:</h5>
 * <p class='bcode'>
 * 	<ja>@Remoteable</ja>(path=<js>"/myproxy"</js>)
 * 	<jk>public interface</jk> MyProxy {
 *
 * 		<jc>// Explicit names specified for HTTP headers.</jc>
 * 		<jc>// pojo will be converted to UON notation (unless plain-text parts enabled).</jc>
 * 		<ja>@RemoteMethod</ja>(path=<js>"/mymethod1"</js>)
 * 		String myProxyMethod1(<ja>@Header</ja>(<js>"Foo"</js>)</ja> String foo,
 * 			<ja>@Header</ja>(<js>"Bar"</js>)</ja> MyPojo pojo);
 *
 * 		<jc>// Multiple values pulled from a NameValuePairs object.</jc>
 * 		<jc>// Same as @Header("*").</jc>
 * 		<ja>@RemoteMethod</ja>(path=<js>"/mymethod2"</js>)
 * 		String myProxyMethod2(<ja>@Header</ja> NameValuePairs nameValuePairs);
 *
 * 		<jc>// Multiple values pulled from a Map.</jc>
 * 		<jc>// Same as @Header("*").</jc>
 * 		<ja>@RemoteMethod</ja>(path=<js>"/mymethod3"</js>)
 * 		String myProxyMethod3(<ja>@Header</ja> Map&lt;String,Object&gt; map);
 *
 * 		<jc>// Multiple values pulled from a bean.</jc>
 * 		<jc>// Same as @Header("*").</jc>
 * 		<ja>@RemoteMethod</ja>(path=<js>"/mymethod4"</js>)
 * 		String myProxyMethod4(<ja>@Header</ja> MyBean myBean);
 * 	}
 * </p>
 *
 * <p>
 * The annotation can also be applied to a bean property field or getter when the argument is annotated with
 * {@link RequestBean @RequestBean}:
 *
 * <h5 class='section'>Example:</h5>
 * <p class='bcode'>
 * 	<ja>@Remoteable</ja>(path=<js>"/myproxy"</js>)
 * 	<jk>public interface</jk> MyProxy {
 *
 * 		<ja>@RemoteMethod</ja>(path=<js>"/mymethod"</js>)
 * 		String myProxyMethod(<ja>@RequestBean</ja> MyRequestBean bean);
 * 	}
 *
 * 	<jk>public interface</jk> MyRequestBean {
 *
 * 		<jc>// Name explicitly specified.</jc>
 * 		<ja>@Header</ja>(<js>"Foo"</js>)
 * 		String getX();
 *
 * 		<jc>// Name inherited from bean property.</jc>
 * 		<jc>// Same as @Header("bar")</jc>
 * 		<ja>@Header</ja>
 * 		String getBar();
 *
 * 		<jc>// Name inherited from bean property.</jc>
 * 		<jc>// Same as @Header("Baz")</jc>
 * 		<ja>@Header</ja>
 * 		<ja>@BeanProperty</ja>(<js>"Baz"</js>)
 * 		String getY();
 *
 * 		<jc>// Multiple values pulled from NameValuePairs object.</jc>
 * 		<jc>// Same as @Header("*")</jc>
 * 		<ja>@Header</ja>
 * 		NameValuePairs getNameValuePairs();
 *
 * 		<jc>// Multiple values pulled from Map.</jc>
 * 		<jc>// Same as @Header("*")</jc>
 * 		<ja>@Header</ja>
 * 	 	Map&lt;String,Object&gt; getMap();
 *
 * 		<jc>// Multiple values pulled from bean.</jc>
 * 		<jc>// Same as @Header("*")</jc>
 * 		<ja>@Header</ja>
 * 	 	MyBean getBean();
 * 	}
 * </p>
 *
 * <p>
 * The {@link #name()} and {@link #value()} elements are synonyms for specifying the header name.
 * Only one should be used.
 * <br>The following annotations are fully equivalent:
 * <p class='bcode'>
 * 	<ja>@Header</ja>(name=<js>"Foo"</js>)
 *
 * 	<ja>@Header</ja>(<js>"Foo"</js>)
 * </p>
 *
 * <h5 class='section'>See Also:</h5>
 * <ul class='doctree'>
 * 	<li class='link'><a class='doclink' href='../../../../overview-summary.html#juneau-rest-client.3rdPartyProxies'>Overview &gt; juneau-rest-client &gt; Interface Proxies Against 3rd-party REST Interfaces</a>
 * </ul>
 */
@Documented
@Target({PARAMETER,FIELD,METHOD,TYPE})
@Retention(RUNTIME)
@Inherited
public @interface Header {

	/**
	 * The HTTP header name.
	 *
	 * <p>
	 * A blank value (the default) indicates to reuse the bean property name when used on a request bean property.
	 *
	 * <p>
	 * The value should be either <js>"*"</js> to represent multiple name/value pairs, or a label that defines the
	 * header name.
	 *
	 * <p>
	 * A blank value (the default) has the following behavior:
	 * <ul class='spaced-list'>
	 * 	<li>
	 * 		If the data type is <code>NameValuePairs</code>, <code>Map</code>, or a bean,
	 * 		then it's the equivalent to <js>"*"</js> which will cause the value to be serialized as name/value pairs.
	 *
	 * 		<h5 class='figure'>Example:</h5>
	 * 		<p class='bcode'>
	 * 	<jc>// When used on a remote method parameter</jc>
	 * 	<ja>@Remoteable</ja>(path=<js>"/myproxy"</js>)
	 * 	<jk>public interface</jk> MyProxy {
	 *
	 * 		<jc>// Equivalent to @Header("*")</jc>
	 * 		<ja>@RemoteMethod</ja>(path=<js>"/mymethod"</js>)
	 * 		String myProxyMethod1(<ja>@Header</ja> Map&lt;String,Object&gt; headers);
	 * 	}
	 *
	 * 	<jc>// When used on a request bean method</jc>
	 * 	<jk>public interface</jk> MyRequestBean {
	 *
	 * 		<jc>// Equivalent to @Header("*")</jc>
	 * 		<ja>@Header</ja>
	 * 		Map&lt;String,Object&gt; getFoo();
	 * 	}
	 * 		</p>
	 * 	</li>
	 * 	<li>
	 * 		If used on a request bean method, uses the bean property name.
	 *
	 * 		<h5 class='figure'>Example:</h5>
	 * 		<p class='bcode'>
	 * 	<jk>public interface</jk> MyRequestBean {
	 *
	 * 		<jc>// Equivalent to @Header("Foo")</jc>
	 * 		<ja>@Header</ja>
	 * 		<ja>@BeanProperty</ja>(<js>"Foo"</js>)
	 * 		String getFoo();
	 * 	}
	 * 		</p>
	 * 	</li>
	 * </ul>
	 */
//	String name() default "";

	/**
	 * A synonym for {@link #name()}.
	 *
	 * <p>
	 * Allows you to use shortened notation if you're only specifying the name.
	 */
//	String value() default "";

	/**
	 * Skips this value if it's an empty string or empty collection/array.
	 *
	 * <p>
	 * Note that <jk>null</jk> values are already ignored.
	 */
	boolean skipIfEmpty() default false;

	/**
	 * Specifies the {@link HttpPartSerializer} class used for serializing values to strings.
	 *
	 * <p>
	 * The default value defaults to the using the part serializer defined on the {@link RequestBean @RequestBean} annotation,
	 * then on the client which by default is {@link UrlEncodingSerializer}.
	 *
	 * <p>
	 * This annotation is provided to allow values to be custom serialized.
	 */
	Class<? extends HttpPartSerializer> serializer() default HttpPartSerializer.Null.class;
	/**
	 * Specifies the {@link HttpPartParser} class used for parsing values from strings.
	 *
	 * <p>
	 * The default value for this parser is inherited from the servlet/method which defaults to {@link OpenApiPartParser}.
	 * <br>You can use {@link SimplePartParser} to parse POJOs that are directly convertible from <code>Strings</code>.
	 */
	Class<? extends HttpPartParser> parser() default HttpPartParser.Null.class;

	//=================================================================================================================
	// Attributes common to all Swagger Parameter objects
	//=================================================================================================================

	/**
	 * HTTP header name.
	 */
	String name() default "";

	/**
	 * A synonym for {@link #name()}.
	 *
	 * <p>
	 * Allows you to use shortened notation if you're only specifying the name.
	 *
	 * <p>
	 * The following are completely equivalent ways of defining a header entry:
	 * <p class='bcode w800'>
	 * 	<jk>public</jk> Order placeOrder(<jk>@Header</jk>(name=<js>"api_key"</js>) String apiKey) {...}
	 * </p>
	 * <p class='bcode w800'>
	 * 	<jk>public</jk> Order placeOrder(<jk>@Header</jk>(<js>"api_key"</js>) String apiKey) {...}
	 * </p>
	 */
	String value() default "";

	/**
	 * <mk>description</mk> field of the Swagger <a class="doclink" href="https://swagger.io/specification/v2/#parameterObject">Parameter</a> object.
	 *
	 * <p>
	 * A brief description of the parameter. This could contain examples of use.
	 *
	 * <h5 class='section'>Notes:</h5>
	 * <ul class='spaced-list'>
	 * 	<li>
	 * 		The format is plain text.
	 * 		<br>Multiple lines are concatenated with newlines.
	 * 	<li>
	 * 		Supports <a class="doclink" href="../../../../../overview-summary.html#DefaultRestSvlVariables">initialization-time and request-time variables</a>
	 * 		(e.g. <js>"$L{my.localized.variable}"</js>).
	 * </ul>
	 */
	String[] description() default {};

	/**
	 * <mk>required</mk> field of the Swagger <a class="doclink" href="https://swagger.io/specification/v2/#parameterObject">Parameter</a> object.
	 *
	 * <p>
	 * Determines whether the parameter is mandatory.
	 *
	 * <p>
	 * If validation is not met during parsing, the part parser will throw a {@link SchemaValidationException}.
	 */
	boolean required() default false;

	//=================================================================================================================
	// Attributes specific to parameters other than body
	//=================================================================================================================

	/**
	 * <mk>type</mk> field of the Swagger <a class="doclink" href="https://swagger.io/specification/v2/#parameterObject">Parameter</a> object.
	 *
	 * <p>
	 * The type of the parameter.
	 *
	 * <p>
	 * The possible values are:
	 * <ul class='spaced-list'>
	 * 	<li>
	 * 		<js>"string"</js>
	 * 		<br>Parameter must be a string or a POJO convertible from a string.
	 * 	<li>
	 * 		<js>"number"</js>
	 * 		<br>Parameter must be a number primitive or number object.
	 * 		<br>If parameter is <code>Object</code>, creates either a <code>Float</code> or <code>Double</code> depending on the size of the number.
	 * 	<li>
	 * 		<js>"integer"</js>
	 * 		<br>Parameter must be a integer/long primitive or integer/long object.
	 * 		<br>If parameter is <code>Object</code>, creates either a <code>Short</code>, <code>Integer</code>, or <code>Long</code> depending on the size of the number.
	 * 	<li>
	 * 		<js>"boolean"</js>
	 * 		<br>Parameter must be a boolean primitive or object.
	 * 	<li>
	 * 		<js>"array"</js>
	 * 		<br>Parameter must be an array or collection.
	 * 		<br>Elements must be strings or POJOs convertible from strings.
	 * 		<br>If parameter is <code>Object</code>, creates an {@link ObjectList}.
	 * 	<li>
	 * 		<js>"object"</js>
	 * 		<br>Parameter must be a map or bean.
	 * 		<br>If parameter is <code>Object</code>, creates an {@link ObjectMap}.
	 * 		<br>Note that this is an extension of the OpenAPI schema as Juneau allows for arbitrarily-complex POJOs to be serialized as HTTP parts.
	 * 	<li>
	 * 		<js>"file"</js>
	 * 		<br>This type is currently not supported.
	 * </ul>
	 *
	 * <h5 class='section'>See Also:</h5>
	 * <ul class='doctree'>
	 * 	<li class='link'><a class='doclink' href='https://swagger.io/specification/#dataTypes'>Swagger specification &gt; Data Types</a>
	 * </ul>
	 */
	String type() default "";

	/**
	 * <mk>format</mk> field of the Swagger <a class="doclink" href="https://swagger.io/specification/v2/#parameterObject">Parameter</a> object.
	 *
	 * <p>
	 * The extending format for the previously mentioned <a href='https://swagger.io/specification/v2/#parameterType'>type</a>.
	 *
	 * <p>
	 * The possible values are:
	 * <ul class='spaced-list'>
	 * 	<li>
	 * 		<js>"int32"</js> - Signed 32 bits.
	 * 		<br>Only valid with type <js>"integer"</js>.
	 * 	<li>
	 * 		<js>"int64"</js> - Signed 64 bits.
	 * 		<br>Only valid with type <js>"integer"</js>.
	 * 	<li>
	 * 		<js>"float"</js> - 32-bit floating point number.
	 * 		<br>Only valid with type <js>"number"</js>.
	 * 	<li>
	 * 		<js>"double"</js> - 64-bit floating point number.
	 * 		<br>Only valid with type <js>"number"</js>.
	 * 	<li>
	 * 		<js>"byte"</js> - BASE-64 encoded characters.
	 * 		<br>Only valid with type <js>"string"</js>.
	 * 		<br>Parameters of type POJO convertible from string are converted after the string has been decoded.
	 * 	<li>
	 * 		<js>"binary"</js> - Hexadecimal encoded octets (e.g. <js>"00FF"</js>).
	 * 		<br>Only valid with type <js>"string"</js>.
	 * 		<br>Parameters of type POJO convertible from string are converted after the string has been decoded.
	 * 	<li>
	 * 		<js>"date"</js> - An <a href='http://xml2rfc.ietf.org/public/rfc/html/rfc3339.html#anchor14'>RFC3339 full-date</a>.
	 * 		<br>Only valid with type <js>"string"</js>.
	 * 	<li>
	 * 		<js>"date-time"</js> - An <a href='http://xml2rfc.ietf.org/public/rfc/html/rfc3339.html#anchor14'>RFC3339 date-time</a>.
	 * 		<br>Only valid with type <js>"string"</js>.
	 * 	<li>
	 * 		<js>"password"</js> - Used to hint UIs the input needs to be obscured.
	 * 		<br>This format does not affect the serialization or parsing of the parameter.
	 * 	<li>
	 * 		<js>"uon"</js> - UON notation (e.g. <js>"(foo=bar,baz=@(qux,123))"</js>).
	 * 		<br>Only valid with type <js>"object"</js>.
	 * 		<br>If not specified, then the input is interpreted as plain-text and is converted to a POJO directly.
	 * </ul>
	 *
	 * <h5 class='section'>See Also:</h5>
	 * <ul class='doctree'>
	 * 	<li class='link'><a class='doclink' href='https://swagger.io/specification/v2/#dataTypeFormat'>Swagger specification &gt; Data Type Formats</a>
	 * </ul>
	 */
	String format() default "";


	/**
	 * <mk>allowEmptyValue</mk> field of the Swagger <a class="doclink" href="https://swagger.io/specification/v2/#parameterObject">Parameter</a> object.
	 *
	 * <p>
	 * Sets the ability to pass empty-valued heaver values.
	 *
	 * <p>
	 * <b>Note:</b>  This is technically only valid for either query or formData parameters, but support is provided anyway for backwards compatability.
	 */
	boolean allowEmptyValue() default false;

	/**
	 * <mk>items</mk> field of the Swagger <a class="doclink" href="https://swagger.io/specification/v2/#parameterObject">Parameter</a> object.
	 *
	 * <p>
	 * Describes the type of items in the array.
	 * <p>
	 * Required if <code>type</code> is <js>"array"</js>.
	 * <br>Can only be used if <code>type</code> is <js>"array"</js>.
	 */
	Items items() default @Items;

	/**
	 * <mk>collectionFormat</mk> field of the Swagger <a class="doclink" href="https://swagger.io/specification/v2/#parameterObject">Parameter</a> object.
	 *
	 * <p>
	 * Determines the format of the array if <code>type</code> <js>"array"</js> is used.
	 * <br>Can only be used if <code>type</code> is <js>"array"</js>.
	 *
	 * <br>Possible values are:
	 * <ul class='spaced-list'>
	 * 	<li>
	 * 		<js>"csv"</js> (default) - Comma-separated values (e.g. <js>"foo,bar"</js>).
	 * 	<li>
	 * 		<js>"ssv"</js> - Space-separated values (e.g. <js>"foo bar"</js>).
	 * 	<li>
	 * 		<js>"tsv"</js> - Tab-separated values (e.g. <js>"foo\tbar"</js>).
	 * 	<li>
	 * 		<js>"pipes</js> - Pipe-separated values (e.g. <js>"foo|bar"</js>).
	 * 	<li>
	 * 		<js>"multi"</js> - Corresponds to multiple parameter instances instead of multiple values for a single instance (e.g. <js>"foo=bar&amp;foo=baz"</js>).
	 * 	<li>
	 * 		<js>"uon"</js> - UON notation (e.g. <js>"@(foo,bar)"</js>).
	 * 	<li>
	 * </ul>
	 *
	 * <p>
	 * Note that for collections/arrays parameters with POJO element types, the input is broken into a string array before being converted into POJO elements.
	 */
	String collectionFormat() default "";

	/**
	 * <mk>default</mk> field of the Swagger <a class="doclink" href="https://swagger.io/specification/v2/#parameterObject">Parameter</a> object.
	 *
	 * <p>
	 * Declares the value of the parameter that the server will use if none is provided, for example a "count" to control the number of results per page might default to 100 if not supplied by the client in the request.
	 * <br>(Note: "default" has no meaning for required parameters.)
	 *
	 * <p>
	 * Additionally, this value is used to create instances of POJOs that are then serialized as language-specific examples in the generated Swagger documentation
	 * if the examples are not defined in some other way.
	 *
	 * <p>
	 * The format of this value is a string.
	 * <br>Multiple lines are concatenated with newlines.
	 *
	 * <h5 class='section'>Examples:</h5>
	 * <p class='bcode w800'>
	 * 	<jk>public</jk> Order placeOrder(
	 * 		<jk>@Header</jk>(name=<js>"X-PetId"</jk>, _default=<js>"100"</js>) <jk>long</jk> petId,
	 * 		<jk>@Header</jk>(name=<js>"X-AdditionalInfo"</jk>, format=<js>"uon"</js>, _default=<js>"(rushOrder=false)"</js>) AdditionalInfo additionalInfo,
	 * 		<jk>@Header</jk>(name=<js>"X-Flags"</jk>, collectionFormat=<js>"uon"</js>, _default=<js>"@(new-customer)"</js>) String[] flags
	 * 	) {...}
	 * </p>
	 */
	String[] _default() default {};

	/**
	 * <mk>maximum</mk> field of the Swagger <a class="doclink" href="https://swagger.io/specification/v2/#parameterObject">Parameter</a> object.
	 *
	 * <p>
	 * Defines the maximum value for a parameter of numeric types.
	 * <br>The value must be a valid JSON number.
	 *
	 * <p>
	 * If validation is not met during serialization or parsing, the part serializer/parser will throw a {@link SchemaValidationException}.
	 *
	 * <p>
	 * Only allowed for the following types: <js>"integer"</js>, <js>"number"</js>.
	 */
	String maximum() default "";

	/**
	 * <mk>exclusiveMaximum</mk> field of the Swagger <a class="doclink" href="https://swagger.io/specification/v2/#parameterObject">Parameter</a> object.
	 *
	 * <p>
	 * Defines whether the maximum is matched exclusively.
	 *
	 * <p>
	 * If validation is not met during serialization or parsing, the part serializer/parser will throw a {@link SchemaValidationException}.
	 *
	 * <p>
	 * Only allowed for the following types: <js>"integer"</js>, <js>"number"</js>.
	 * <br>If <jk>true</jk>, must be accompanied with <code>maximum</code>.
	 */
	boolean exclusiveMaximum() default false;

	/**
	 * <mk>minimum</mk> field of the Swagger <a class="doclink" href="https://swagger.io/specification/v2/#parameterObject">Parameter</a> object.
	 *
	 * <p>
	 * Defines the minimum value for a parameter of numeric types.
	 * <br>The value must be a valid JSON number.
	 *
	 * <p>
	 * If validation is not met during serialization or parsing, the part serializer/parser will throw a {@link SchemaValidationException}.
	 *
	 * <p>
	 * Only allowed for the following types: <js>"integer"</js>, <js>"number"</js>.
	 */
	String minimum() default "";

	/**
	 * <mk>exclusiveMinimum</mk> field of the Swagger <a class="doclink" href="https://swagger.io/specification/v2/#parameterObject">Parameter</a> object.
	 *
	 * <p>
	 * Defines whether the minimum is matched exclusively.
	 *
	 * <p>
	 * If validation is not met during serialization or parsing, the part serializer/parser will throw a {@link SchemaValidationException}.
	 *
	 * <p>
	 * Only allowed for the following types: <js>"integer"</js>, <js>"number"</js>.
	 * <br>If <jk>true</jk>, must be accompanied with <code>minimum</code>.
	 */
	boolean exclusiveMinimum() default false;

	/**
	 * <mk>maxLength</mk> field of the Swagger <a class="doclink" href="https://swagger.io/specification/v2/#parameterObject">Parameter</a> object.
	 *
	 * <p>
	 * A string instance is valid against this keyword if its length is less than, or equal to, the value of this keyword.
	 * <br>The length of a string instance is defined as the number of its characters as defined by <a href='https://tools.ietf.org/html/rfc4627'>RFC 4627</a>.
	 * <br>The value <code>-1</code> is always ignored.
	 *
	 * <p>
	 * If validation is not met during serialization or parsing, the part serializer/parser will throw a {@link SchemaValidationException}.
	 *
	 * <p>
	 * Only allowed for the following types: <js>"string"</js>.
	 */
	long maxLength() default -1;

	/**
	 * <mk>minLength</mk> field of the Swagger <a class="doclink" href="https://swagger.io/specification/v2/#parameterObject">Parameter</a> object.
	 *
	 * <p>
	 * A string instance is valid against this keyword if its length is greater than, or equal to, the value of this keyword.
	 * <br>The length of a string instance is defined as the number of its characters as defined by <a href='https://tools.ietf.org/html/rfc4627'>RFC 4627</a>.
	 * <br>The value <code>-1</code> is always ignored.
	 *
	 * <p>
	 * If validation is not met during serialization or parsing, the part serializer/parser will throw a {@link SchemaValidationException}.
	 *
	 * <p>
	 * Only allowed for the following types: <js>"string"</js>.
	 */
	long minLength() default -1;

	/**
	 * <mk>pattern</mk> field of the Swagger <a class="doclink" href="https://swagger.io/specification/v2/#parameterObject">Parameter</a> object.
	 *
	 * <p>
	 * A string input is valid if it matches the specified regular expression pattern.
	 *
	 * <p>
	 * If validation is not met during serialization or parsing, the part serializer/parser will throw a {@link SchemaValidationException}.
	 *
	 * <p>
	 * Only allowed for the following types: <js>"string"</js>.
	 */
	String pattern() default "";

	/**
	 * <mk>maxItems</mk> field of the Swagger <a class="doclink" href="https://swagger.io/specification/v2/#parameterObject">Parameter</a> object.
	 *
	 * <p>
	 * An array or collection is valid if its size is less than, or equal to, the value of this keyword.
	 *
	 * <p>
	 * If validation is not met during serialization or parsing, the part serializer/parser will throw a {@link SchemaValidationException}.
	 *
	 * <p>
	 * Only allowed for the following types: <js>"array"</js>.
	 */
	long maxItems() default -1;

	/**
	 * <mk>minItems</mk> field of the Swagger <a class="doclink" href="https://swagger.io/specification/v2/#parameterObject">Parameter</a> object.
	 *
	 * <p>
	 * An array or collection is valid if its size is greater than, or equal to, the value of this keyword.
	 *
	 * <p>
	 * If validation is not met during serialization or parsing, the part serializer/parser will throw a {@link SchemaValidationException}.
	 *
	 * <p>
	 * Only allowed for the following types: <js>"array"</js>.
	 */
	long minItems() default -1;

	/**
	 * <mk>uniqueItems</mk> field of the Swagger <a class="doclink" href="https://swagger.io/specification/v2/#parameterObject">Parameter</a> object.
	 *
	 * <p>
	 * If <jk>true</jk> the input validates successfully if all of its elements are unique.
	 *
	 * <p>
	 * If validation is not met during serialization or parsing, the part serializer/parser will throw a {@link SchemaValidationException}.
	 * <br>If the parameter type is a subclass of {@link Set}, this validation is skipped (since a set can only contain unique items anyway).
	 * <br>Otherwise, the collection or array is checked for duplicate items.
	 *
	 * <p>
	 * Only allowed for the following types: <js>"array"</js>.
	 */
	boolean uniqueItems() default false;

	/**
	 * <mk>enum</mk> field of the Swagger <a class="doclink" href="https://swagger.io/specification/v2/#parameterObject">Parameter</a> object.
	 *
	 * <p>
	 * If specified, the input validates successfully if it is equal to one of the elements in this array.
	 *
	 * <p>
	 * If validation is not met during serialization or parsing, the part serializer/parser will throw a {@link SchemaValidationException}.
	 *
	 * <p>
	 * The format is a {@link JsonSerializer#DEFAULT_LAX Simple-JSON} array or comma-delimited list.
	 * <br>Multiple lines are concatenated with newlines.
	 *
	 * <h5 class='section'>Examples:</h5>
	 * <p class='bcode w800'>
	 * 	<jk>public</jk> Collection&lt;Pet&gt; findPetsByStatus(
	 * 		<ja>@Header</ja>(
	 * 			name=<js>"X-Status"</js>,
	 * 			_enum=<js>"AVAILABLE,PENDING,SOLD"</js>,
	 * 		) PetStatus status
	 * 	) {...}
	 * </p>
	 * <p class='bcode w800'>
	 * 	<jk>public</jk> Collection&lt;Pet&gt; findPetsByStatus(
	 * 		<ja>@Header</ja>(
	 * 			name=<js>"X-Status"</js>,
	 * 			_enum=<js>"['AVAILABLE','PENDING','SOLD']"</js>,
	 * 		) PetStatus status
	 * 	) {...}
	 * </p>
	 */
	String[] _enum() default {};

	/**
	 * <mk>multipleOf</mk> field of the Swagger <a class="doclink" href="https://swagger.io/specification/v2/#parameterObject">Parameter</a> object.
	 *
	 * <p>
	 * A numeric instance is valid if the result of the division of the instance by this keyword's value is an integer.
	 * <br>The value must be a valid JSON number.
	 *
	 * <p>
	 * If validation is not met during serialization or parsing, the part serializer/parser will throw a {@link SchemaValidationException}.
	 *
	 * <p>
	 * Only allowed for the following types: <js>"integer"</js>, <js>"number"</js>.
	 */
	String multipleOf() default "";

	//=================================================================================================================
	// Other
	//=================================================================================================================

	/**
	 * A serialized example of the parameter.
	 *
	 * <p>
	 * This attribute defines a JSON representation of the value that is used by <code>BasicRestInfoProvider</code> to construct
	 * an example of the header entry.
	 *
	 * <h5 class='section'>Notes:</h5>
	 * <ul class='spaced-list'>
	 * 	<li>
	 * 		The format is a {@link JsonSerializer#DEFAULT_LAX Simple-JSON} object or plain text string.
	 * 		<br>Multiple lines are concatenated with newlines.
	 * 	<li>
	 * 		Supports <a class="doclink" href="../../../../../overview-summary.html#DefaultRestSvlVariables">initialization-time and request-time variables</a>
	 * 		(e.g. <js>"$L{my.localized.variable}"</js>).
	 * </ul>
	 */
	String[] example() default {};

	/**
	 * Free-form value for the Swagger <a class="doclink" href="https://swagger.io/specification/v2/#parameterObject">Parameter</a> object.
	 *
	 * <p>
	 * This is a JSON object that makes up the swagger information for this field.
	 *
	 * <p>
	 * The following are completely equivalent ways of defining the swagger description of the request header:
	 * <p class='bcode w800'>
	 * 	<jc>// Normal</jc>
	 * 	<ja>@Header</ja>(
	 * 		name=<js>"api_key"</js>,
	 * 		description=<js>"Security API key"</js>,
	 * 		required=<js>"true"</js>,
	 * 		example=<js>"foobar"</js>
	 * 	)
	 * </p>
	 * <p class='bcode w800'>
	 * 	<jc>// Free-form</jc>
	 * 	<ja>@Header</ja>(
	 * 		name=<js>"api_key"</js>,
	 * 		api={
	 * 			<js>"description: 'Security API key',"</js>,
	 * 			<js>"required: true,"</js>,
	 * 			<js>"example: 'foobar'"</js>
	 * 		}
	 * 	)
	 * </p>
	 * <p class='bcode w800'>
	 * 	<jc>// Free-form with variables</jc>
	 * 	<ja>@Header</ja>(
	 * 		name=<js>"api_key"</js>,
	 * 		api=<js>"$L{apiKeySwagger}"</js>
	 * 	)
	 * </p>
	 * <p class='bcode w800'>
	 * 	<mc>// Contents of MyResource.properties</mc>
	 * 	<mk>apiKeySwagger</mk> = <mv>{ description: "Security API key", required: true, example: "foobar" }</mv>
	 * </p>
	 *
	 * <p>
	 * 	The reasons why you may want to use this field include:
	 * <ul>
	 * 	<li>You want to pull in the entire Swagger JSON definition for this field from an external source such as a properties file.
	 * 	<li>You want to add extra fields to the Swagger documentation that are not officially part of the Swagger specification.
	 * </ul>
	 *
	 * <h5 class='section'>Notes:</h5>
	 * <ul class='spaced-list'>
	 * 	<li>
	 * 		Note that the only swagger field you can't specify using this value is <js>"name"</js> whose value needs to be known during servlet initialization.
	 * 	<li>
	 * 		The format is a {@link JsonSerializer#DEFAULT_LAX Simple-JSON} object.
	 * 	<li>
	 * 		Automatic validation is NOT performed on input based on attributes in this value.
	 * 	<li>
	 * 		The leading/trailing <code>{ }</code> characters are optional.
	 * 		<br>The following two example are considered equivalent:
	 * 		<p class='bcode w800'>
	 * 	<ja>@Header</ja>(api=<js>"{required: true}"</js>)
	 * 		</p>
	 * 		<p class='bcode w800'>
	 * 	<ja>@Header</ja>(api=<js>"required: true"</js>)
	 * 		</p>
	 * 	<li>
	 * 		Multiple lines are concatenated with newlines so that you can format the value to be readable.
	 * 	<li>
	 * 		Supports <a class="doclink" href="../../../../../overview-summary.html#DefaultRestSvlVariables">initialization-time and request-time variables</a>
	 * 		(e.g. <js>"$L{my.localized.variable}"</js>).
	 * 	<li>
	 * 		Values defined in this field supersede values pulled from the Swagger JSON file and are superseded by individual values defined on this annotation.
	 * </ul>
	 */
	String[] api() default {};
}
