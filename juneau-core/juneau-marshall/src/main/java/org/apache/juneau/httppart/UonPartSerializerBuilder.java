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
package org.apache.juneau.httppart;

import static org.apache.juneau.uon.UonSerializer.*;

import java.util.*;

import org.apache.juneau.*;
import org.apache.juneau.http.*;
import org.apache.juneau.serializer.*;
import org.apache.juneau.uon.*;

/**
 * Builder class for building instances of {@link UonPartSerializer}.
 */
public class UonPartSerializerBuilder extends UonSerializerBuilder {

	/**
	 * Constructor, default settings.
	 */
	public UonPartSerializerBuilder() {
		super();
	}

	/**
	 * Constructor.
	 *
	 * @param ps The initial configuration settings for this builder.
	 */
	public UonPartSerializerBuilder(PropertyStore ps) {
		super(ps);
	}

	@Override /* ContextBuilder */
	public UonPartSerializer build() {
		return build(UonPartSerializer.class);
	}


	//--------------------------------------------------------------------------------
	// Properties
	//--------------------------------------------------------------------------------

	/**
	 * <b>Configuration property:</b>  Format to use for top-level query names and simple parameters.
	 *
	 * <p>
	 * Specifies the format to use for URL GET parameter keys and values.
	 *
	 * <p>
	 * The possible values are:
	 * <ul class='spaced-list'>
	 * 	<li>
	 * 		<js>"UON"</js> (default) - Use UON notation for values.
	 * 		<br>String values such as <js>"(foo='bar')"</js> will end up being quoted and escaped to <js>"'(foo=bar~'baz~')'"</js>.
	 * 		<br>Similarly, boolean and numeric values will also end up quoted.
	 * 	<li>
	 * 		<js>"PLAINTEXT"</js> (default) - Serialize as plain text.
	 * 		<br>Strings will never be quoted or escaped.
	 * 		<br>Note that this can cause errors during parsing if you're using the URL-encoding parser to parse
	 * 		the results since UON constructs won't be differentiable.
	 * 		<br>However, this is not an issue if you're simply creating queries or form posts against 3rd-party interfaces.
	 * </ul>
	 *
	 * @param paramFormat The new value for this property.
	 * @return This object (for method chaining).
	 * @see UonSerializer#UON_paramFormat
	 */
	public UonPartSerializerBuilder paramFormat(String paramFormat) {
		return set(UON_paramFormat, paramFormat);
	}

	/**
	 * Shortcut for calling <code>paramFormat(<js>"PLAINTEXT"</js>)</code>.
	 *
	 * @return This object (for method chaining).
	 * @see UonSerializer#UON_paramFormat
	 */
	public UonPartSerializerBuilder plainTextParams() {
		return paramFormat("PLAINTEXT");
	}

	@Override /* UonSerializerBuilder */
	public UonPartSerializerBuilder encodeChars(boolean value) {
		super.encodeChars(value);
		return this;
	}

	@Override /* UonSerializerBuilder */
	public UonPartSerializerBuilder encoding() {
		super.encoding();
		return this;
	}

	@Override /* SerializerBuilder */
	public UonPartSerializerBuilder maxDepth(int value) {
		super.maxDepth(value);
		return this;
	}

	@Override /* SerializerBuilder */
	public UonPartSerializerBuilder initialDepth(int value) {
		super.initialDepth(value);
		return this;
	}

	@Override /* SerializerBuilder */
	public UonPartSerializerBuilder detectRecursions(boolean value) {
		super.detectRecursions(value);
		return this;
	}

	@Override /* SerializerBuilder */
	public UonPartSerializerBuilder ignoreRecursions(boolean value) {
		super.ignoreRecursions(value);
		return this;
	}

	@Override /* SerializerBuilder */
	public UonPartSerializerBuilder useWhitespace(boolean value) {
		super.useWhitespace(value);
		return this;
	}

	@Override /* SerializerBuilder */
	public UonPartSerializerBuilder ws() {
		super.ws();
		return this;
	}

	@Override /* SerializerBuilder */
	public UonPartSerializerBuilder addBeanTypeProperties(boolean value) {
		super.addBeanTypeProperties(value);
		return this;
	}

	@Override /* SerializerBuilder */
	public UonPartSerializerBuilder quoteChar(char value) {
		super.quoteChar(value);
		return this;
	}

	@Override /* SerializerBuilder */
	public UonPartSerializerBuilder sq() {
		super.sq();
		return this;
	}

	@Override /* SerializerBuilder */
	public UonPartSerializerBuilder trimNullProperties(boolean value) {
		super.trimNullProperties(value);
		return this;
	}

	@Override /* SerializerBuilder */
	public UonPartSerializerBuilder trimEmptyCollections(boolean value) {
		super.trimEmptyCollections(value);
		return this;
	}

	@Override /* SerializerBuilder */
	public UonPartSerializerBuilder trimEmptyMaps(boolean value) {
		super.trimEmptyMaps(value);
		return this;
	}

	@Override /* SerializerBuilder */
	public UonPartSerializerBuilder trimStrings(boolean value) {
		super.trimStrings(value);
		return this;
	}

	@Override /* SerializerBuilder */
	public UonPartSerializerBuilder uriContext(UriContext value) {
		super.uriContext(value);
		return this;
	}

	@Override /* SerializerBuilder */
	public UonPartSerializerBuilder uriResolution(UriResolution value) {
		super.uriResolution(value);
		return this;
	}

	@Override /* SerializerBuilder */
	public UonPartSerializerBuilder uriRelativity(UriRelativity value) {
		super.uriRelativity(value);
		return this;
	}

	@Override /* SerializerBuilder */
	public UonPartSerializerBuilder sortCollections(boolean value) {
		super.sortCollections(value);
		return this;
	}

	@Override /* SerializerBuilder */
	public UonPartSerializerBuilder sortMaps(boolean value) {
		super.sortMaps(value);
		return this;
	}

	@Override /* SerializerBuilder */
	public UonPartSerializerBuilder abridged(boolean value) {
		super.abridged(value);
		return this;
	}

	@Override /* SerializerBuilder */
	public UonPartSerializerBuilder listener(Class<? extends SerializerListener> value) {
		super.listener(value);
		return this;
	}

	@Override /* ContextBuilder */
	public UonPartSerializerBuilder beansRequireDefaultConstructor(boolean value) {
		super.beansRequireDefaultConstructor(value);
		return this;
	}

	@Override /* ContextBuilder */
	public UonPartSerializerBuilder beansRequireSerializable(boolean value) {
		super.beansRequireSerializable(value);
		return this;
	}

	@Override /* ContextBuilder */
	public UonPartSerializerBuilder beansRequireSettersForGetters(boolean value) {
		super.beansRequireSettersForGetters(value);
		return this;
	}

	@Override /* ContextBuilder */
	public UonPartSerializerBuilder beansRequireSomeProperties(boolean value) {
		super.beansRequireSomeProperties(value);
		return this;
	}

	@Override /* ContextBuilder */
	public UonPartSerializerBuilder beanMapPutReturnsOldValue(boolean value) {
		super.beanMapPutReturnsOldValue(value);
		return this;
	}

	@Override /* ContextBuilder */
	public UonPartSerializerBuilder beanConstructorVisibility(Visibility value) {
		super.beanConstructorVisibility(value);
		return this;
	}

	@Override /* ContextBuilder */
	public UonPartSerializerBuilder beanClassVisibility(Visibility value) {
		super.beanClassVisibility(value);
		return this;
	}

	@Override /* ContextBuilder */
	public UonPartSerializerBuilder beanFieldVisibility(Visibility value) {
		super.beanFieldVisibility(value);
		return this;
	}

	@Override /* ContextBuilder */
	public UonPartSerializerBuilder methodVisibility(Visibility value) {
		super.methodVisibility(value);
		return this;
	}

	@Override /* ContextBuilder */
	public UonPartSerializerBuilder useJavaBeanIntrospector(boolean value) {
		super.useJavaBeanIntrospector(value);
		return this;
	}

	@Override /* ContextBuilder */
	public UonPartSerializerBuilder useInterfaceProxies(boolean value) {
		super.useInterfaceProxies(value);
		return this;
	}

	@Override /* ContextBuilder */
	public UonPartSerializerBuilder ignoreUnknownBeanProperties(boolean value) {
		super.ignoreUnknownBeanProperties(value);
		return this;
	}

	@Override /* ContextBuilder */
	public UonPartSerializerBuilder ignoreUnknownNullBeanProperties(boolean value) {
		super.ignoreUnknownNullBeanProperties(value);
		return this;
	}

	@Override /* ContextBuilder */
	public UonPartSerializerBuilder ignorePropertiesWithoutSetters(boolean value) {
		super.ignorePropertiesWithoutSetters(value);
		return this;
	}

	@Override /* ContextBuilder */
	public UonPartSerializerBuilder ignoreInvocationExceptionsOnGetters(boolean value) {
		super.ignoreInvocationExceptionsOnGetters(value);
		return this;
	}

	@Override /* ContextBuilder */
	public UonPartSerializerBuilder ignoreInvocationExceptionsOnSetters(boolean value) {
		super.ignoreInvocationExceptionsOnSetters(value);
		return this;
	}

	@Override /* ContextBuilder */
	public UonPartSerializerBuilder sortProperties(boolean value) {
		super.sortProperties(value);
		return this;
	}

	@Override /* ContextBuilder */
	public UonPartSerializerBuilder notBeanPackages(String...values) {
		super.notBeanPackages(values);
		return this;
	}

	@Override /* ContextBuilder */
	public UonPartSerializerBuilder notBeanPackages(Collection<String> values) {
		super.notBeanPackages(values);
		return this;
	}

	@Override /* ContextBuilder */
	public UonPartSerializerBuilder notBeanPackages(boolean append, String...values) {
		super.notBeanPackages(append, values);
		return this;
	}

	@Override /* ContextBuilder */
	public UonPartSerializerBuilder notBeanPackages(boolean append, Collection<String> values) {
		super.notBeanPackages(append, values);
		return this;
	}

	@Override /* ContextBuilder */
	public UonPartSerializerBuilder notBeanPackagesRemove(String...values) {
		super.notBeanPackagesRemove(values);
		return this;
	}

	@Override /* ContextBuilder */
	public UonPartSerializerBuilder notBeanPackagesRemove(Collection<String> values) {
		super.notBeanPackagesRemove(values);
		return this;
	}

	@Override /* ContextBuilder */
	public UonPartSerializerBuilder notBeanClasses(Class<?>...values) {
		super.notBeanClasses(values);
		return this;
	}

	@Override /* ContextBuilder */
	public UonPartSerializerBuilder notBeanClasses(Collection<Class<?>> values) {
		super.notBeanClasses(values);
		return this;
	}

	@Override /* ContextBuilder */
	public UonPartSerializerBuilder notBeanClasses(boolean append, Class<?>...values) {
		super.notBeanClasses(append, values);
		return this;
	}

	@Override /* ContextBuilder */
	public UonPartSerializerBuilder notBeanClasses(boolean append, Collection<Class<?>> values) {
		super.notBeanClasses(append, values);
		return this;
	}

	@Override /* ContextBuilder */
	public UonPartSerializerBuilder notBeanClassesRemove(Class<?>...values) {
		super.notBeanClassesRemove(values);
		return this;
	}

	@Override /* ContextBuilder */
	public UonPartSerializerBuilder notBeanClassesRemove(Collection<Class<?>> values) {
		super.notBeanClassesRemove(values);
		return this;
	}

	@Override /* ContextBuilder */
	public UonPartSerializerBuilder beanFilters(Class<?>...values) {
		super.beanFilters(values);
		return this;
	}

	@Override /* ContextBuilder */
	public UonPartSerializerBuilder beanFilters(Collection<Class<?>> values) {
		super.beanFilters(values);
		return this;
	}

	@Override /* ContextBuilder */
	public UonPartSerializerBuilder beanFilters(boolean append, Class<?>...values) {
		super.beanFilters(append, values);
		return this;
	}

	@Override /* ContextBuilder */
	public UonPartSerializerBuilder beanFilters(boolean append, Collection<Class<?>> values) {
		super.beanFilters(append, values);
		return this;
	}

	@Override /* ContextBuilder */
	public UonPartSerializerBuilder beanFiltersRemove(Class<?>...values) {
		super.beanFiltersRemove(values);
		return this;
	}

	@Override /* ContextBuilder */
	public UonPartSerializerBuilder beanFiltersRemove(Collection<Class<?>> values) {
		super.beanFiltersRemove(values);
		return this;
	}

	@Override /* ContextBuilder */
	public UonPartSerializerBuilder pojoSwaps(Class<?>...values) {
		super.pojoSwaps(values);
		return this;
	}

	@Override /* ContextBuilder */
	public UonPartSerializerBuilder pojoSwaps(Collection<Class<?>> values) {
		super.pojoSwaps(values);
		return this;
	}

	@Override /* ContextBuilder */
	public UonPartSerializerBuilder pojoSwaps(boolean append, Class<?>...values) {
		super.pojoSwaps(append, values);
		return this;
	}

	@Override /* ContextBuilder */
	public UonPartSerializerBuilder pojoSwaps(boolean append, Collection<Class<?>> values) {
		super.pojoSwaps(append, values);
		return this;
	}

	@Override /* ContextBuilder */
	public UonPartSerializerBuilder pojoSwapsRemove(Class<?>...values) {
		super.pojoSwapsRemove(values);
		return this;
	}

	@Override /* ContextBuilder */
	public UonPartSerializerBuilder pojoSwapsRemove(Collection<Class<?>> values) {
		super.pojoSwapsRemove(values);
		return this;
	}

	@Override /* ContextBuilder */
	public UonPartSerializerBuilder implClasses(Map<String,Class<?>> values) {
		super.implClasses(values);
		return this;
	}

	@Override /* ContextBuilder */
	public <T> UonPartSerializerBuilder implClass(Class<T> interfaceClass, Class<? extends T> implClass) {
		super.implClass(interfaceClass, implClass);
		return this;
	}

	@Override /* ContextBuilder */
	public UonPartSerializerBuilder includeProperties(Map<String,String> values) {
		super.includeProperties(values);
		return this;
	}

	@Override /* ContextBuilder */
	public UonPartSerializerBuilder includeProperties(String beanClassName, String properties) {
		super.includeProperties(beanClassName, properties);
		return this;
	}

	@Override /* ContextBuilder */
	public UonPartSerializerBuilder includeProperties(Class<?> beanClass, String properties) {
		super.includeProperties(beanClass, properties);
		return this;
	}

	@Override /* ContextBuilder */
	public UonPartSerializerBuilder excludeProperties(Map<String,String> values) {
		super.excludeProperties(values);
		return this;
	}

	@Override /* ContextBuilder */
	public UonPartSerializerBuilder excludeProperties(String beanClassName, String properties) {
		super.excludeProperties(beanClassName, properties);
		return this;
	}

	@Override /* ContextBuilder */
	public UonPartSerializerBuilder excludeProperties(Class<?> beanClass, String properties) {
		super.excludeProperties(beanClass, properties);
		return this;
	}

	@Override /* ContextBuilder */
	public UonPartSerializerBuilder beanDictionary(Class<?>...values) {
		super.beanDictionary(values);
		return this;
	}

	@Override /* ContextBuilder */
	public UonPartSerializerBuilder beanDictionary(Collection<Class<?>> values) {
		super.beanDictionary(values);
		return this;
	}

	@Override /* ContextBuilder */
	public UonPartSerializerBuilder beanDictionary(boolean append, Class<?>...values) {
		super.beanDictionary(append, values);
		return this;
	}

	@Override /* ContextBuilder */
	public UonPartSerializerBuilder beanDictionary(boolean append, Collection<Class<?>> values) {
		super.beanDictionary(append, values);
		return this;
	}

	@Override /* ContextBuilder */
	public UonPartSerializerBuilder beanDictionaryRemove(Class<?>...values) {
		super.beanDictionaryRemove(values);
		return this;
	}

	@Override /* ContextBuilder */
	public UonPartSerializerBuilder beanDictionaryRemove(Collection<Class<?>> values) {
		super.beanDictionaryRemove(values);
		return this;
	}

	@Override /* ContextBuilder */
	public UonPartSerializerBuilder beanTypePropertyName(String value) {
		super.beanTypePropertyName(value);
		return this;
	}

	@Override /* ContextBuilder */
	public UonPartSerializerBuilder defaultParser(Class<?> value) {
		super.defaultParser(value);
		return this;
	}

	@Override /* ContextBuilder */
	public UonPartSerializerBuilder locale(Locale value) {
		super.locale(value);
		return this;
	}

	@Override /* ContextBuilder */
	public UonPartSerializerBuilder timeZone(TimeZone value) {
		super.timeZone(value);
		return this;
	}

	@Override /* ContextBuilder */
	public UonPartSerializerBuilder mediaType(MediaType value) {
		super.mediaType(value);
		return this;
	}

	@Override /* ContextBuilder */
	public UonPartSerializerBuilder debug() {
		super.debug();
		return this;
	}

	@Override /* ContextBuilder */
	public UonPartSerializerBuilder set(String name, Object value) {
		super.set(name, value);
		return this;
	}

	@Override /* ContextBuilder */
	public UonPartSerializerBuilder set(boolean append, String name, Object value) {
		super.set(append, name, value);
		return this;
	}

	@Override /* ContextBuilder */
	public UonPartSerializerBuilder set(Map<String,Object> properties) {
		super.set(properties);
		return this;
	}

	@Override /* ContextBuilder */
	public UonPartSerializerBuilder add(Map<String,Object> properties) {
		super.add(properties);
		return this;
	}

	@Override /* ContextBuilder */
	public UonPartSerializerBuilder addTo(String name, Object value) {
		super.addTo(name, value);
		return this;
	}

	@Override /* ContextBuilder */
	public UonPartSerializerBuilder addTo(String name, String key, Object value) {
		super.addTo(name, key, value);
		return this;
	}

	@Override /* ContextBuilder */
	public UonPartSerializerBuilder removeFrom(String name, Object value) {
		super.removeFrom(name, value);
		return this;
	}

	@Override /* ContextBuilder */
	public UonPartSerializerBuilder apply(PropertyStore copyFrom) {
		super.apply(copyFrom);
		return this;
	}
}