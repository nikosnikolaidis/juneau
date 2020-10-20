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
package org.apache.juneau.annotation;

import java.lang.annotation.*;

import org.apache.juneau.*;
import org.apache.juneau.transform.*;

/**
 * A concrete implementation of the {@link Bean} annotation.
 *
 * <ul class='seealso'>
 * 	<li class='jm'>{@link BeanContextBuilder#annotations(Annotation...)}
 * </ul>
 */
public class BeanAnnotation extends TargetedAnnotation.OnClass implements Bean {

	private Class<?>[]
		dictionary = new Class[0];
	private Class<?>
		implClass = Object.class,
		interfaceClass = Object.class,
		stopClass = Object.class;
	private Class<? extends BeanInterceptor<?>>
		interceptor = BeanInterceptor.Default.class;
	private Class<? extends PropertyNamer>
		propertyNamer = PropertyNamerDefault.class;
	private String
		bpi = "",
		bpx = "",
		bpro = "",
		bpwo = "",
		example = "",
		typeName = "",
		typePropertyName = "";
	boolean
		fluentSetters = false,
		sort = false;

	/**
	 * Constructor.
	 *
	 * @param on The initial value for the <c>on</c> property.
	 */
	public BeanAnnotation(String...on) {
		on(on);
	}

	/**
	 * Constructor.
	 *
	 * @param on The initial value for the <c>on</c> property.
	 */
	public BeanAnnotation(Class<?>...on) {
		on(on);
	}

	@Override
	public String bpi() {
		return bpi;
	}

	/**
	 * Sets the <c>bpi</c> property on this annotation.
	 *
	 * @param value The new value for this property.
	 * @return This object (for method chaining).
	 */
	public BeanAnnotation bpi(String value) {
		this.bpi = value;
		return this;
	}

	@Override
	public String bpro() {
		return bpro;
	}

	/**
	 * Sets the <c>bpro</c> property on this annotation.
	 *
	 * @param value The new value for this property.
	 * @return This object (for method chaining).
	 */
	public BeanAnnotation bpro(String value) {
		this.bpro = value;
		return this;
	}

	@Override
	public String bpwo() {
		return bpwo;
	}

	/**
	 * Sets the <c>bpwo</c> property on this annotation.
	 *
	 * @param value The new value for this property.
	 * @return This object (for method chaining).
	 */
	public BeanAnnotation bpwo(String value) {
		this.bpwo = value;
		return this;
	}

	@Override
	public String bpx() {
		return bpx;
	}

	/**
	 * Sets the <c>bpx</c> property on this annotation.
	 *
	 * @param value The new value for this property.
	 * @return This object (for method chaining).
	 */
	public BeanAnnotation bpx(String value) {
		this.bpx = value;
		return this;
	}

	@Override
	public Class<?>[] dictionary() {
		return dictionary;
	}

	/**
	 * Sets the <c>dictionary</c> property on this annotation.
	 *
	 * @param value The new value for this property.
	 * @return This object (for method chaining).
	 */
	public BeanAnnotation dictionary(Class<?>...value) {
		this.dictionary = value;
		return this;
	}

	@Override
	public String example() {
		return example;
	}

	/**
	 * Sets the <c>example</c> property on this annotation.
	 *
	 * @param value The new value for this property.
	 * @return This object (for method chaining).
	 */
	public BeanAnnotation example(String value) {
		this.example = value;
		return this;
	}

	@Override
	public boolean fluentSetters() {
		return fluentSetters;
	}

	/**
	 * Sets the <c>fluentSetters</c> property on this annotation.
	 *
	 * @param value The new value for this property.
	 * @return This object (for method chaining).
	 */
	public BeanAnnotation fluentSetters(boolean value) {
		this.fluentSetters = value;
		return this;
	}

	@Override
	public Class<?> implClass() {
		return implClass;
	}

	/**
	 * Sets the <c>implClass</c> property on this annotation.
	 *
	 * @param value The new value for this property.
	 * @return This object (for method chaining).
	 */
	public BeanAnnotation implClass(Class<?> value) {
		this.implClass = value;
		return this;
	}

	@Override
	public Class<?> interfaceClass() {
		return interfaceClass;
	}

	/**
	 * Sets the <c>interfaceClass</c> property on this annotation.
	 *
	 * @param value The new value for this property.
	 * @return This object (for method chaining).
	 */
	public BeanAnnotation interfaceClass(Class<?> value) {
		this.interfaceClass = value;
		return this;
	}

	@Override
	public Class<? extends BeanInterceptor<?>> interceptor() {
		return interceptor;
	}

	/**
	 * Sets the <c>propertyFilter</c> property on this annotation.
	 *
	 * @param value The new value for this property.
	 * @return This object (for method chaining).
	 */
	public BeanAnnotation interceptor(Class<? extends BeanInterceptor<?>> value) {
		this.interceptor = value;
		return this;
	}

	@Override
	public Class<? extends PropertyNamer> propertyNamer() {
		return propertyNamer;
	}

	/**
	 * Sets the <c>propertyNamer</c> property on this annotation.
	 *
	 * @param value The new value for this property.
	 * @return This object (for method chaining).
	 */
	public BeanAnnotation propertyNamer(Class<? extends PropertyNamer> value) {
		this.propertyNamer = value;
		return this;
	}

	@Override
	public boolean sort() {
		return sort;
	}

	/**
	 * Sets the <c>sort</c> property on this annotation.
	 *
	 * @param value The new value for this property.
	 * @return This object (for method chaining).
	 */
	public BeanAnnotation sort(boolean value) {
		this.sort = value;
		return this;
	}

	@Override
	public Class<?> stopClass() {
		return stopClass;
	}

	/**
	 * Sets the <c>stopClass</c> property on this annotation.
	 *
	 * @param value The new value for this property.
	 * @return This object (for method chaining).
	 */
	public BeanAnnotation stopClass(Class<?> value) {
		this.stopClass = value;
		return this;
	}

	@Override
	public String typeName() {
		return typeName;
	}

	/**
	 * Sets the <c>typeName</c> property on this annotation.
	 *
	 * @param value The new value for this property.
	 * @return This object (for method chaining).
	 */
	public BeanAnnotation typeName(String value) {
		this.typeName = value;
		return this;
	}

	@Override
	public String typePropertyName() {
		return typePropertyName;
	}

	/**
	 * Sets the <c>typePropertyName</c> property on this annotation.
	 *
	 * @param value The new value for this property.
	 * @return This object (for method chaining).
	 */
	public BeanAnnotation typePropertyName(String value) {
		this.typePropertyName = value;
		return this;
	}

	// <FluentSetters>

	@Override /* GENERATED - TargetedAnnotation */
	public BeanAnnotation on(String...value) {
		super.on(value);
		return this;
	}

	@Override /* GENERATED - OnClass */
	public BeanAnnotation on(java.lang.Class<?>...value) {
		super.on(value);
		return this;
	}

	@Override /* GENERATED - OnClass */
	public BeanAnnotation onClass(java.lang.Class<?>...value) {
		super.onClass(value);
		return this;
	}

	// </FluentSetters>
}
