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
package org.apache.juneau.dto.swagger;

import static org.apache.juneau.internal.BeanPropertyUtils.*;

import java.net.*;
import java.net.URI;

import org.apache.juneau.*;
import org.apache.juneau.annotation.*;

/**
 * Allows referencing an external resource for extended documentation.
 * 
 * <h5 class='section'>Example:</h5>
 * <p class='bcode'>
 * 	<jc>// Construct using SwaggerBuilder.</jc>
 * 	ExternalDocumentation x = <jsm>externalDocumentation</jsm>(<js>"https://swagger.io"</js>, <js>"Find more info here"</js>);
 * 
 * 	<jc>// Serialize using JsonSerializer.</jc>
 * 	String json = JsonSerializer.<jsf>DEFAULT</jsf>.toString(x);
 * 
 * 	<jc>// Or just use toString() which does the same as above.</jc>
 * 	String json = x.toString();
 * </p>
 * <p class='bcode'>
 * 	<jc>// Output</jc>
 * 	{
 * 		<js>"description"</js>: <js>"Find more info here"</js>,
 * 		<js>"url"</js>: <js>"https://swagger.io"</js>
 * 	}
 * </p>
 * 
 * <h6 class='topic'>Additional Information</h6>
 * <ul class='doctree'>
 * 	<li class='link'><a class='doclink' href='../../../../../overview-summary.html#juneau-dto.Swagger'>Overview > juneau-dto > Swagger</a>
 * </ul>
 */
@Bean(properties="description,url,*")
public class ExternalDocumentation extends SwaggerElement {

	private String description;
	private URI url;

	/**
	 * Bean property getter:  <property>description</property>.
	 * 
	 * <p>
	 * A short description of the target documentation. 
	 * 
	 * @return The property value, or <jk>null</jk> if it is not set.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Bean property setter:  <property>description</property>.
	 * 
	 * <p>
	 * A short description of the target documentation. 
	 * 
	 * @param value 
	 * 	The new value for this property.
	 * 	<br><a class="doclink" href="https://help.github.com/articles/github-flavored-markdown">GFM syntax</a> can be used for rich text representation.
	 * 	<br>Can be <jk>null</jk> to unset the property.
	 * @return This object (for method chaining).
	 */
	public ExternalDocumentation setDescription(String value) {
		description = value;
		return this;
	}

	/**
	 * Same as {@link #setDescription(String)}.
	 * 
	 * @param value
	 * 	The new value for this property.
	 * 	<br>Non-String values will be converted to String using <code>toString()</code>.
	 * 	<br>Can be <jk>null</jk> to unset the property.
	 * @return This object (for method chaining).
	 */
	public ExternalDocumentation description(Object value) {
		return setDescription(toStringVal(value));
	}

	/**
	 * Bean property getter:  <property>url</property>.
	 * 
	 * <p>
	 * The URL for the target documentation.
	 * 
	 * @return The property value, or <jk>null</jk> if it is not set.
	 */
	public URI getUrl() {
		return url;
	}

	/**
	 * Bean property setter:  <property>url</property>.
	 * 
	 * <p>
	 * The URL for the target documentation.
	 * 
	 * @param value 
	 * 	The new value for this property.
	 * 	<br>Property value is required.
	 * 	<br>URIs defined by {@link UriResolver} can be used for values.
	 * @return This object (for method chaining).
	 */
	public ExternalDocumentation setUrl(URI value) {
		url = value;
		return this;
	}

	/**
	 * Same as {@link #setUrl(URI)}.
	 * 
	 * @param value
	 * 	The new value for this property.
	 * 	<br>URIs defined by {@link UriResolver} can be used for values.
	 * 	<br>Valid types:
	 * 	<ul>
	 * 		<li>{@link URI}
	 * 		<li>{@link URL}
	 * 		<li>{@link String} 
	 * 			<br>Converted to URI using <code><jk>new</jk> URI(value.toString())</code>.
	 * 		<li>
	 * 	</ul>
	 * 	<br>Can be <jk>null</jk> to unset the property.
	 * @return This object (for method chaining).
	 */
	public ExternalDocumentation url(Object value) {
		return setUrl(toURI(value));
	}

	@Override /* SwaggerElement */
	public <T> T get(String property, Class<T> type) {
		if (property == null)
			return null;
		switch (property) {
			case "description": return toType(getDescription(), type);
			case "url": return toType(getUrl(), type);
			default: return super.get(property, type);
		}
	}

	@Override /* SwaggerElement */
	public ExternalDocumentation set(String property, Object value) {
		if (property == null)
			return this;
		switch (property) {
			case "description": return description(value);
			case "url": return url(value);
			default: 
				super.set(property, value);
				return this;
		}
	}
}
