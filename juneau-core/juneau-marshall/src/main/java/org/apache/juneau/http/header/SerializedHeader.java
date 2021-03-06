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
package org.apache.juneau.http.header;

import static org.apache.juneau.internal.ExceptionUtils.*;
import static org.apache.juneau.internal.StringUtils.*;

import java.util.function.*;

import org.apache.http.*;
import org.apache.juneau.httppart.*;
import org.apache.juneau.internal.*;
import org.apache.juneau.oapi.*;
import org.apache.juneau.serializer.*;
import org.apache.juneau.urlencoding.*;

/**
 * Subclass of {@link NameValuePair} for serializing POJOs as URL-encoded form post entries using the
 * {@link UrlEncodingSerializer class}.
 *
 * <h5 class='section'>Example:</h5>
 * <p class='bcode w800'>
 * 	NameValuePairs params = <jk>new</jk> NameValuePairs()
 * 		.append(<jk>new</jk> SerializedNameValuePair(<js>"myPojo"</js>, pojo, UrlEncodingSerializer.<jsf>DEFAULT_SIMPLE</jsf>))
 * 		.append(<jk>new</jk> BasicNameValuePair(<js>"someOtherParam"</js>, <js>"foobar"</js>));
 * 	request.setEntity(<jk>new</jk> UrlEncodedFormEntity(params));
 * </p>
 */
@FluentSetters
public class SerializedHeader extends BasicHeader {
	private static final long serialVersionUID = 1L;

	/**
	 * Convenience creator.
	 *
	 * @param name The header name.
	 * @param value
	 * 	The POJO to serialize as the header value.
	 * 	<br>Can be <jk>null</jk>.
	 * @return A new header bean, or <jk>null</jk> if the name is <jk>null</jk> or empty.
	 */
	public static SerializedHeader of(String name, Object value) {
		if (isEmpty(name))
			return null;
		return new SerializedHeader(name, value, null, null, false);
	}

	/**
	 * Convenience creator with delayed value.
	 *
	 * <p>
	 * Header value is re-evaluated on each call to {@link #getValue()}.
	 *
	 * @param name The header name.
	 * @param value
	 * 	The supplier of the POJO to serialize as the header value.
	 * 	<br>Can be <jk>null</jk>.
	 * @return A new header bean, or <jk>null</jk> if the name is <jk>null</jk> or empty.
	 */
	public static SerializedHeader of(String name, Supplier<?> value) {
		if (isEmpty(name))
			return null;
		return new SerializedHeader(name, value, null, null, false);
	}

	/**
	 * Convenience creator.
	 *
	 * @param name The HTTP header name name.
	 * @param value
	 * 	The POJO to serialize as the header value.
	 * @param serializer
	 * 	The serializer to use for serializing the value to a string value.
	 * @param schema
	 * 	The schema object that defines the format of the output.
	 * 	<br>If <jk>null</jk>, defaults to the schema defined on the serializer.
	 * 	<br>If that's also <jk>null</jk>, defaults to {@link HttpPartSchema#DEFAULT}.
	 * 	<br>Only used if serializer is schema-aware (e.g. {@link OpenApiSerializer}).
	 * 	<br>Can also be a {@link Supplier}.
	 * @param skipIfEmpty If value is a blank string, the value should return as <jk>null</jk>.
	 * @return A new header bean, or <jk>null</jk> if the name is <jk>null</jk> or empty.
	 */
	public static SerializedHeader of(String name, Object value, HttpPartSerializerSession serializer, HttpPartSchema schema, boolean skipIfEmpty) {
		if (isEmpty(name))
			return null;
		return new SerializedHeader(name, value, serializer, schema, skipIfEmpty);
	}

	/**
	 * Convenience creator with delayed value.
	 *
	 * <p>
	 * Header value is re-evaluated on each call to {@link #getValue()}.
	 *
	 * @param name The HTTP header name name.
	 * @param value
	 * 	The supplier of the POJO to serialize as the header value.
	 * @param serializer
	 * 	The serializer to use for serializing the value to a string value.
	 * @param schema
	 * 	The schema object that defines the format of the output.
	 * 	<br>If <jk>null</jk>, defaults to the schema defined on the serializer.
	 * 	<br>If that's also <jk>null</jk>, defaults to {@link HttpPartSchema#DEFAULT}.
	 * 	<br>Only used if serializer is schema-aware (e.g. {@link OpenApiSerializer}).
	 * 	<br>Can also be a {@link Supplier}.
	 * @param skipIfEmpty If value is a blank string, the value should return as <jk>null</jk>.
	 * @return A new header bean, or <jk>null</jk> if the name is <jk>null</jk> or empty.
	 */
	public static SerializedHeader of(String name, Supplier<?> value, HttpPartSerializerSession serializer, HttpPartSchema schema, boolean skipIfEmpty) {
		if (isEmpty(name))
			return null;
		return new SerializedHeader(name, value, serializer, schema, skipIfEmpty);
	}

	private final Object value;
	private final Supplier<Object> supplier;
	private HttpPartSerializerSession serializer;
	private HttpPartSchema schema = HttpPartSchema.DEFAULT;
	private boolean skipIfEmpty;

	/**
	 * Constructor.
	 *
	 * @param name The HTTP header name name.
	 * @param value The POJO to serialize to the parameter value.
	 * @param serializer
	 * 	The serializer to use for serializing the value to a string value.
	 * @param schema
	 * 	The schema object that defines the format of the output.
	 * 	<br>If <jk>null</jk>, defaults to the schema defined on the serializer.
	 * 	<br>If that's also <jk>null</jk>, defaults to {@link HttpPartSchema#DEFAULT}.
	 * 	<br>Only used if serializer is schema-aware (e.g. {@link OpenApiSerializer}).
	 * 	<br>Can also be a {@link Supplier}.
	 * @param skipIfEmpty If value is a blank string, the value should return as <jk>null</jk>.
	 */
	@SuppressWarnings("unchecked")
	public SerializedHeader(String name, Object value, HttpPartSerializerSession serializer, HttpPartSchema schema, boolean skipIfEmpty) {
		super(name, null);
		this.value = value instanceof Supplier ? null : value;
		this.supplier = value instanceof Supplier ? (Supplier<Object>)value : null;
		this.serializer = serializer;
		this.schema = schema;
		this.skipIfEmpty = skipIfEmpty;
	}

	/**
	 * Constructor with delayed value.
	 *
	 * <p>
	 * Header value is re-evaluated on each call to {@link #getValue()}.
	 *
	 * @param name The HTTP header name name.
	 * @param value The supplier of the POJO to serialize to the parameter value.
	 * @param serializer
	 * 	The serializer to use for serializing the value to a string value.
	 * @param schema
	 * 	The schema object that defines the format of the output.
	 * 	<br>If <jk>null</jk>, defaults to the schema defined on the serializer.
	 * 	<br>If that's also <jk>null</jk>, defaults to {@link HttpPartSchema#DEFAULT}.
	 * 	<br>Only used if serializer is schema-aware (e.g. {@link OpenApiSerializer}).
	 * 	<br>Can also be a {@link Supplier}.
	 * @param skipIfEmpty If value is a blank string, the value should return as <jk>null</jk>.
	 */
	public SerializedHeader(String name, Supplier<Object> value, HttpPartSerializerSession serializer, HttpPartSchema schema, boolean skipIfEmpty) {
		super(name, null);
		this.value = null;
		this.supplier = value;
		this.serializer = serializer;
		this.schema = schema;
		this.skipIfEmpty = skipIfEmpty;
	}

	/**
	 * Copy constructor.
	 *
	 * @param copyFrom The object to copy.
	 */
	protected SerializedHeader(SerializedHeader copyFrom) {
		super(copyFrom);
		this.value = copyFrom.value;
		this.supplier = copyFrom.supplier;
		this.serializer = copyFrom.serializer == null ? serializer : copyFrom.serializer;
		this.schema = copyFrom.schema == null ? schema : copyFrom.schema;
		this.skipIfEmpty = copyFrom.skipIfEmpty;
	}

	/**
	 * Creates a copy of this object.
	 *
	 * @return A new copy of this object.
	 */
	public SerializedHeader copy() {
		return new SerializedHeader(this);
	}

	/**
	 * Sets the serializer to use for serializing the value to a string value.
	 *
	 * @param value The new value for this property.
	 * @return This object (for method chaining).
	 */
	public SerializedHeader serializer(HttpPartSerializer value) {
		if (value != null)
			return serializer(value.createPartSession(null));
		return this;
	}

	/**
	 * Sets the serializer to use for serializing the value to a string value.
	 *
	 * @param value The new value for this property.
	 * @return This object (for method chaining).
	 */
	public SerializedHeader serializer(HttpPartSerializerSession value) {
		serializer = value;
		return this;
	}

	/**
	 * Sets the schema object that defines the format of the output.
	 *
	 * @param value The new value for this property.
	 * @return This object (for method chaining).
	 */
	public SerializedHeader schema(HttpPartSchema value) {
		this.schema = value;
		return this;
	}

	/**
	 * Copies this bean and sets the serializer and schema on it.
	 *
	 * @param serializer The new serializer for the bean.  Can be <jk>null</jk>.
	 * @param schema The new schema for the bean.  Can be <jk>null</jk>.
	 * @return Either a new bean with the serializer set, or this bean if
	 * 	both values are <jk>null</jk> or the serializer and schema were already set.
	 */
	public SerializedHeader copyWith(HttpPartSerializerSession serializer, HttpPartSchema schema) {
		if ((this.serializer == null && serializer != null) || (this.schema == null && schema != null)) {
			SerializedHeader h = copy();
			if (serializer != null)
				h.serializer(serializer);
			if (schema != null)
				h.schema(schema);
			return h;
		}
		return this;
	}

	/**
	 * Don't serialize this header if the value is <jk>null</jk> or an empty string.
	 *
	 * @return This object (for method chaining).
	 */
	public SerializedHeader skipIfEmpty() {
		return skipIfEmpty(true);
	}

	/**
	 * Don't serialize this header if the value is <jk>null</jk> or an empty string.
	 *
	 * @param value The new value of this setting.
	 * @return This object (for method chaining).
	 */
	public SerializedHeader skipIfEmpty(boolean value) {
		this.skipIfEmpty = value;
		return this;
	}

	@Override /* NameValuePair */
	public String getValue() {
		try {
			Object v = value;
			if (supplier != null)
				v = supplier.get();
			HttpPartSchema schema = this.schema == null ? HttpPartSchema.DEFAULT : this.schema;
			String def = schema.getDefault();
			if (v == null) {
				if (def == null && ! schema.isRequired())
					return null;
				if (def == null && schema.isAllowEmptyValue())
					return null;
			}
			if (isEmpty(v) && skipIfEmpty && def == null)
				return null;
			return serializer == null ? stringify(v) : serializer.serialize(HttpPartType.HEADER, schema, v);
		} catch (SchemaValidationException e) {
			throw runtimeException(e, "Validation error on request {0} parameter ''{1}''=''{2}''", HttpPartType.HEADER, getName(), value);
		} catch (SerializeException e) {
			throw runtimeException(e, "Serialization error on request {0} parameter ''{1}''", HttpPartType.HEADER, getName());
		}
	}

	// <FluentSetters>

	// </FluentSetters>
}
