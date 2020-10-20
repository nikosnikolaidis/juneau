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

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

import java.io.*;
import java.lang.annotation.*;
import java.lang.reflect.*;
import java.util.*;

import org.apache.juneau.*;
import org.apache.juneau.parser.*;
import org.apache.juneau.serializer.*;
import org.apache.juneau.transform.*;

/**
 * Annotation for specifying config properties defined in {@link BeanContext} and {@link BeanTraverseContext}.
 *
 * <p>
 * Used primarily for specifying bean configuration properties on REST classes and methods.
 */
@Documented
@Target({TYPE,METHOD})
@Retention(RUNTIME)
@Inherited
@PropertyStoreApply(BeanConfigApply.class)
public @interface BeanConfig {

	/**
	 * Optional rank for this config.
	 *
	 * <p>
	 * Can be used to override default ordering and application of config annotations.
	 */
	int rank() default 0;

	//-----------------------------------------------------------------------------------------------------------------
	// BeanContext
	//-----------------------------------------------------------------------------------------------------------------

	/**
	 * Dynamically applies {@link Bean @Bean} annotations to specified classes.
	 *
	 * <p>
	 * Provides an alternate approach for applying annotations using {@link Bean#on() @Bean.on} to specify the names
	 * to apply the annotation to.
	 *
	 * <ul class='seealso'>
	 * 	<li class='link'>{@doc DynamicallyAppliedAnnotations}
	 * </ul>
	 */
	Bean[] applyBean() default {};

	/**
	 * Dynamically applies {@link Beanc @Beanc} annotations to specified constructors.
	 *
	 * <p>
	 * Provides an alternate approach for applying annotations using {@link Beanc#on() @Beanc.on} to specify the names
	 * to apply the annotation to.
	 *
	 * <ul class='seealso'>
	 * 	<li class='link'>{@doc DynamicallyAppliedAnnotations}
	 * </ul>
	 */
	Beanc[] applyBeanc() default {};

	/**
	 * Dynamically applies {@link Beanp @Beanp} annotations to specified methods.
	 *
	 * <p>
	 * Provides an alternate approach for applying annotations using {@link Beanp#on() @Beanp.on} to specify the names
	 * to apply the annotation to.
	 *
	 * <ul class='seealso'>
	 * 	<li class='link'>{@doc DynamicallyAppliedAnnotations}
	 * </ul>
	 */
	Beanp[] applyBeanp() default {};

	/**
	 * Dynamically applies {@link BeanIgnore @BeanIgnore} annotations to specified classes/methods/fields/constructors.
	 *
	 * <p>
	 * Provides an alternate approach for applying annotations using {@link BeanIgnore#on() @BeanIgnore.on} to specify the names
	 * to apply the annotation to.
	 *
	 * <ul class='seealso'>
	 * 	<li class='link'>{@doc DynamicallyAppliedAnnotations}
	 * </ul>
	 */
	BeanIgnore[] applyBeanIgnore() default {};

	/**
	 * Dynamically applies {@link Example @Example} annotations to specified classes/methods/fields.
	 *
	 * <p>
	 * Provides an alternate approach for applying annotations using {@link Example#on() @Example.on} to specify the names
	 * to apply the annotation to.
	 *
	 * <ul class='seealso'>
	 * 	<li class='link'>{@doc DynamicallyAppliedAnnotations}
	 * </ul>
	 */
	Example[] applyExample() default {};

	/**
	 * Dynamically applies {@link Marshalled Marshalled} annotations to specified classes.
	 *
	 * <p>
	 * Provides an alternate approach for applying annotations using {@link Marshalled#on() Marshalled.on} to specify the names
	 * to apply the annotation to.
	 *
	 * <ul class='seealso'>
	 * 	<li class='link'>{@doc DynamicallyAppliedAnnotations}
	 * </ul>
	 */
	Marshalled[] applyMarshalled() default {};

	/**
	 * Dynamically applies {@link NameProperty @NameProperty} annotations to specified methods/fields.
	 *
	 * <p>
	 * Provides an alternate approach for applying annotations using {@link NameProperty#on() @NameProperty.on} to specify the names
	 * to apply the annotation to.
	 *
	 * <ul class='seealso'>
	 * 	<li class='link'>{@doc DynamicallyAppliedAnnotations}
	 * </ul>
	 */
	NameProperty[] applyNameProperty() default {};

	/**
	 * Dynamically applies {@link ParentProperty @ParentProperty} annotations to specified methods/fields.
	 *
	 * <p>
	 * Provides an alternate approach for applying annotations using {@link ParentProperty#on() @ParentProperty.on} to specify the names
	 * to apply the annotation to.
	 *
	 * <ul class='seealso'>
	 * 	<li class='link'>{@doc DynamicallyAppliedAnnotations}
	 * </ul>
	 */
	ParentProperty[] applyParentProperty() default {};

	/**
	 * Dynamically applies {@link Swap @Swap} annotations to specified classes/methods/fields.
	 *
	 * <p>
	 * Provides an alternate approach for applying annotations using {@link Swap#on() @Swap.on} to specify the names
	 * to apply the annotation to.
	 *
	 * <ul class='seealso'>
	 * 	<li class='link'>{@doc DynamicallyAppliedAnnotations}
	 * </ul>
	 */
	Swap[] applySwap() default {};

	/**
	 * Dynamically applies {@link Uri @Uri} annotations to specified classes/methods/fields.
	 *
	 * <p>
	 * Provides an alternate approach for applying annotations using {@link Uri#on() @URI.on} to specify the names
	 * to apply the annotation to.
	 *
	 * <ul class='seealso'>
	 * 	<li class='link'>{@doc DynamicallyAppliedAnnotations}
	 * </ul>
	 */
	Uri[] applyURI() default {};

	/**
	 * Configuration property:  Minimum bean class visibility.
	 *
	 * <p>
	 * Classes are not considered beans unless they meet the minimum visibility requirements.
	 *
	 * <p>
	 * For example, if the visibility is <c>PUBLIC</c> and the bean class is <jk>protected</jk>, then the class
	 * will not be interpreted as a bean class and be serialized as a string.
	 * <br>Use this setting to reduce the visibility requirement.
	 *
	 * <ul class='notes'>
	 * 	<li>
	 * 		Possible values:
	 * 		<ul>
	 * 			<li><js>"PUBLIC"</js> (default)
	 * 			<li><js>"PROTECTED"</js>
	 * 			<li><js>"DEFAULT"</js>
	 * 			<li><js>"PRIVATE"</js>
	 * 		</ul>
	 * 	<li>
	 * 		Supports {@doc DefaultVarResolver} (e.g. <js>"$C{myConfigVar}"</js>).
	 * </ul>
	 *
	 * <ul class='seealso'>
	 * 	<li class='jf'>{@link BeanContext#BEAN_beanClassVisibility}
	 * </ul>
	 */
	String beanClassVisibility() default "";

	/**
	 * Configuration property:  Minimum bean constructor visibility.
	 *
	 * <p>
	 * Only look for constructors with the specified minimum visibility.
	 *
	 * <p>
	 * This setting affects the logic for finding no-arg constructors for bean.
	 * <br>Normally, only <jk>public</jk> no-arg constructors are used.
	 * <br>Use this setting if you want to reduce the visibility requirement.
	 *
	 * <ul class='notes'>
	 * 	<li>
	 * 		Possible values:
	 * 		<ul>
	 * 			<li><js>"PUBLIC"</js> (default)
	 * 			<li><js>"PROTECTED"</js>
	 * 			<li><js>"DEFAULT"</js>
	 * 			<li><js>"PRIVATE"</js>
	 * 		</ul>
	 * 	<li>
	 * 		Supports {@doc DefaultVarResolver} (e.g. <js>"$C{myConfigVar}"</js>).
	 * </ul>
	 *
	 * <ul class='seealso'>
	 * 	<li class='jf'>{@link BeanContext#BEAN_beanConstructorVisibility}
	 * </ul>
	 */
	String beanConstructorVisibility() default "";

	/**
	 * Configuration property:  Minimum bean field visibility.
	 *
	 * <p>
	 * Only look for bean fields with the specified minimum visibility.
	 *
	 * <p>
	 * This affects which fields on a bean class are considered bean properties.
	 * <br>Normally only <jk>public</jk> fields are considered.
	 * <br>Use this setting if you want to reduce the visibility requirement.
	 *
	 * <ul class='notes'>
	 * 	<li>
	 * 		Possible values:
	 * 		<ul>
	 * 			<li><js>"PUBLIC"</js> (default)
	 * 			<li><js>"PROTECTED"</js>
	 * 			<li><js>"DEFAULT"</js>
	 * 			<li><js>"PRIVATE"</js>
	 * 		</ul>
	 * 	<li>
	 * 		Supports {@doc DefaultVarResolver} (e.g. <js>"$C{myConfigVar}"</js>).
	 * </ul>
	 *
	 * <ul class='seealso'>
	 * 	<li class='jf'>{@link BeanContext#BEAN_beanFieldVisibility}
	 * </ul>
	 */
	String beanFieldVisibility() default "";

	/**
	 * Configuration property:  BeanMap.put() returns old property value.
	 *
	 * <p>
	 * If <js>"true"</js>, then the {@link BeanMap#put(String,Object) BeanMap.put()} method will return old property
	 * values.
	 * <br>Otherwise, it returns <jk>null</jk>.
	 *
	 * <ul class='notes'>
	 * 	<li>
	 * 		Possible values:
	 * 		<ul>
	 * 			<li><js>"true"</js>
	 * 			<li><js>"false"</js> (default because it introduces a slight performance penalty during serialization)
	 * 		</ul>
	 * 	<li>
	 * 		Supports {@doc DefaultVarResolver} (e.g. <js>"$C{myConfigVar}"</js>).
	 * </ul>
	 *
	 * <ul class='seealso'>
	 * 	<li class='jf'>{@link BeanContext#BEAN_beanMapPutReturnsOldValue}
	 * </ul>
	 */
	String beanMapPutReturnsOldValue() default "";

	/**
	 * Configuration property:  Minimum bean method visibility.
	 *
	 * <p>
	 * Only look for bean methods with the specified minimum visibility.
	 *
	 * <p>
	 * This affects which methods are detected as getters and setters on a bean class.
	 * <br>Normally only <jk>public</jk> getters and setters are considered.
	 * <br>Use this setting if you want to reduce the visibility requirement.
	 *
	 * <ul class='notes'>
	 * 	<li>
	 * 		Possible values:
	 * 		<ul>
	 * 			<li><js>"PUBLIC"</js> (default)
	 * 			<li><js>"PROTECTED"</js>
	 * 			<li><js>"DEFAULT"</js>
	 * 			<li><js>"PRIVATE"</js>
	 * 		</ul>
	 * 	<li>
	 * 		Supports {@doc DefaultVarResolver} (e.g. <js>"$C{myConfigVar}"</js>).
	 * </ul>
	 *
	 * <ul class='seealso'>
	 * 	<li class='jf'>{@link BeanContext#BEAN_beanMethodVisibility}
	 * </ul>
	 */
	String beanMethodVisibility() default "";

	/**
	 * Configuration property:  Beans require no-arg constructors.
	 *
	 * <p>
	 * If <js>"true"</js>, a Java class must implement a default no-arg constructor to be considered a bean.
	 * <br>Otherwise, the bean will be serialized as a string using the {@link Object#toString()} method.
	 *
	 * <ul class='notes'>
	 * 	<li>
	 * 		Possible values:
	 * 		<ul>
	 * 			<li><js>"true"</js>
	 * 			<li><js>"false"</js> (default)
	 * 		</ul>
	 * 	<li>
	 * 		Supports {@doc DefaultVarResolver} (e.g. <js>"$C{myConfigVar}"</js>).
	 * 	<li>
	 * 		The {@link Bean @Bean} annotation can be used on a class to override this setting when <js>"true"</js>.
	 * </ul>
	 *
	 * <ul class='seealso'>
	 * 	<li class='jf'>{@link BeanContext#BEAN_beansRequireDefaultConstructor}
	 * </ul>
	 */
	String beansRequireDefaultConstructor() default "";

	/**
	 * Configuration property:  Beans require Serializable interface.
	 *
	 * <p>
	 * If <js>"true"</js>, a Java class must implement the {@link Serializable} interface to be considered a bean.
	 * <br>Otherwise, the bean will be serialized as a string using the {@link Object#toString()} method.
	 *
	 * <ul class='notes'>
	 * 	<li>
	 * 		Possible values:
	 * 		<ul>
	 * 			<li><js>"true"</js>
	 * 			<li><js>"false"</js> (default)
	 * 		</ul>
	 * 	<li>
	 * 		Supports {@doc DefaultVarResolver} (e.g. <js>"$C{myConfigVar}"</js>).
	 * 	<li>
	 * 		The {@link Bean @Bean} annotation can be used on a class to override this setting when <js>"true"</js>.
	 * </ul>
	 *
	 * <ul class='seealso'>
	 * 	<li class='jf'>{@link BeanContext#BEAN_beansRequireSerializable}
	 * </ul>
	 */
	String beansRequireSerializable() default "";

	/**
	 * Configuration property:  Beans require setters for getters.
	 *
	 * <p>
	 * If <js>"true"</js>, only getters that have equivalent setters will be considered as properties on a bean.
	 * <br>Otherwise, they will be ignored.
	 *
	 * <ul class='notes'>
	 * 	<li>
	 * 		Possible values:
	 * 		<ul>
	 * 			<li><js>"true"</js>
	 * 			<li><js>"false"</js> (default)
	 * 		</ul>
	 * 	<li>
	 * 		Supports {@doc DefaultVarResolver} (e.g. <js>"$C{myConfigVar}"</js>).
	 * </ul>
	 *
	 * <ul class='seealso'>
	 * 	<li class='jf'>{@link BeanContext#BEAN_beansRequireSettersForGetters}
	 * </ul>
	 */
	String beansRequireSettersForGetters() default "";

	/**
	 * Configuration property:  Beans require at least one property.
	 *
	 * <p>
	 * If <js>"true"</js>, then a Java class must contain at least 1 property to be considered a bean.
	 * <br>Otherwise, the bean will be serialized as a string using the {@link Object#toString()} method.
	 *
	 * <ul class='notes'>
	 * 	<li>
	 * 		Possible values:
	 * 		<ul>
	 * 			<li><js>"true"</js> (default)
	 * 			<li><js>"false"</js>
	 * 		</ul>
	 * 	<li>
	 * 		Supports {@doc DefaultVarResolver} (e.g. <js>"$C{myConfigVar}"</js>).
	 * </ul>
	 *
	 * <ul class='seealso'>
	 * 	<li class='jf'>{@link BeanContext#BEAN_beansRequireSomeProperties}
	 * </ul>
	 */
	String beansRequireSomeProperties() default "";

	/**
	 * Configuration property:  Bean type property name.
	 *
	 * <p>
	 * This specifies the name of the bean property used to store the dictionary name of a bean type so that the
	 * parser knows the data type to reconstruct.
	 *
	 * <ul class='notes'>
	 * 	<li>
	 * 		Default value: <js>"_type"</js>.
	 * 	<li>
	 * 		Supports {@doc DefaultVarResolver} (e.g. <js>"$C{myConfigVar}"</js>).
	 * </ul>

	 * <ul class='seealso'>
	 * 	<li class='jf'>{@link BeanContext#BEAN_typePropertyName}
	 * </ul>
	 */
	String typePropertyName() default "";

	/**
	 * Configuration property:  Bean property includes.
	 *
	 * Shortcut for specifying the {@link Bean#bpi()} annotation for all serializers.
	 *
	 * <p>
	 * The typical use case is when you're rendering summary and details views of the same bean in a resource and
	 * you want to expose or hide specific properties depending on the level of detail you want.
	 *
	 * <p>
	 * In the example below, our 'summary' view is a list of beans where we only want to show the ID property,
	 * and our detail view is a single bean where we want to expose different fields:
	 * <p class='bcode w800'>
	 * 	<jc>// Our bean</jc>
	 * 	<jk>public class</jk> MyBean {
	 *
	 * 		<jc>// Summary properties</jc>
	 * 		<ja>@Html</ja>(link=<js>"servlet:/mybeans/{id}"</js>)
	 * 		<jk>public</jk> String <jf>id</jf>;
	 *
	 * 		<jc>// Detail properties</jc>
	 * 		<jk>public</jk> String <jf>a</jf>, <jf>b</jf>;
	 * 	}
	 *
	 * 	<jc>// Only render "id" property.</jc>
	 * 	<ja>@RestMethod</ja>(name=<jsf>GET</jsf>, path=<js>"/mybeans"</js>)
	 * 	<ja>@BeanConfig</ja>(bpi=<js>"MyBean: id"</js>)
	 * 	<jk>public</jk> List&lt;MyBean&gt; getBeanSummary() {...}
	 *
	 * 	<jc>// Only render "a" and "b" properties.</jc>
	 * 	<ja>@RestMethod</ja>(name=<jsf>GET</jsf>, path=<js>"/mybeans/{id}"</js>)
	 * 	<ja>@BeanConfig</ja>(bpi=<js>"MyBean: a,b"</js>)
	 * 	<jk>public</jk> MyBean getBeanDetails(<ja>@Path</ja> String id) {...}
	 * </p>
	 *
	 * <ul class='notes'>
	 * 	<li>
	 * 		The format of each value is: <js>"Key: comma-delimited-tokens"</js>.
	 * 	<li>
	 * 		Keys can be fully-qualified or short class names or <js>"*"</js> to represent all classes.
	 * 	<li>
	 * 		Values are comma-delimited lists of bean property names.
	 * 	<li>
	 * 		Properties apply to specified class and all subclasses.
	 * 	<li>
	 * 		Semicolons can be used as an additional separator for multiple values:
	 * 		<p class='bcode w800'>
	 * 	<jc>// Equivalent</jc>
	 * 	bpi={<js>"Bean1: foo"</js>,<js>"Bean2: bar,baz"</js>}
	 * 	bpi=<js>"Bean1: foo; Bean2: bar,baz"</js>
	 * 		</p>
	 * 	<li>
	 * 		Supports {@doc DefaultVarResolver} (e.g. <js>"$C{myConfigVar}"</js>).
	 * </ul>
	 *
	 * <ul class='seealso'>
	 * 	<li class='jm'>{@link BeanContextBuilder#bpi(Class, String)}
	 * 	<li class='jm'>{@link BeanContextBuilder#bpi(String, String)}
	 * 	<li class='jm'>{@link BeanContextBuilder#bpi(Map)}
	 * </ul>
	 */
	String[] bpi() default {};

	/**
	 * Configuration property:  Bean property excludes.
	 *
	 * Shortcut for specifying the {@link Bean#bpx()} annotation for all serializers.
	 *
	 * <p>
	 * Same as {@link #bpi()} except you specify a list of bean property names that you want to exclude from
	 * serialization.
	 *
	 * <p>
	 * In the example below, our 'summary' view is a list of beans where we want to exclude some properties:
	 * <p class='bcode w800'>
	 * 	<jc>// Our bean</jc>
	 * 	<jk>public class</jk> MyBean {
	 *
	 * 		<jc>// Summary properties</jc>
	 * 		<ja>@Html</ja>(link=<js>"servlet:/mybeans/{id}"</js>)
	 * 		<jk>public</jk> String <jf>id</jf>;
	 *
	 * 		<jc>// Detail properties</jc>
	 * 		<jk>public</jk> String <jf>a</jf>, <jf>b</jf>;
	 * 	}
	 *
	 * 	<jc>// Don't show "a" and "b" properties.</jc>
	 * 	<ja>@RestMethod</ja>(name=<jsf>GET</jsf>, path=<js>"/mybeans"</js>)
	 * 	<ja>@BeanConfig</ja>(bpx=<js>"MyBean: a,b"</js>)
	 * 	<jk>public</jk> List&lt;MyBean&gt; getBeanSummary() {...}
	 *
	 * 	<jc>// Render all properties.</jc>
	 * 	<ja>@RestMethod</ja>(name=<jsf>GET</jsf>, path=<js>"/mybeans/{id}"</js>)
	 * 	<jk>public</jk> MyBean getBeanDetails(<ja>@Path</ja> String id) {...}
	 * </p>
	 *
	 * <ul class='notes'>
	 * 	<li>
	 * 		The format of each value is: <js>"Key: comma-delimited-tokens"</js>.
	 * 	<li>
	 * 		Keys can be fully-qualified or short class names or <js>"*"</js> to represent all classes.
	 * 	<li>
	 * 		Values are comma-delimited lists of bean property names.
	 * 	<li>
	 * 		Properties apply to specified class and all subclasses.
	 * 	<li>
	 * 		Semicolons can be used as an additional separator for multiple values:
	 * 		<p class='bcode w800'>
	 * 	<jc>// Equivalent</jc>
	 * 	bpx={<js>"Bean1: foo"</js>,<js>"Bean2: bar,baz"</js>}
	 * 	bpx=<js>"Bean1: foo; Bean2: bar,baz"</js>
	 * 		</p>
	 * 	<li>
	 * 		Supports {@doc DefaultVarResolver} (e.g. <js>"$C{myConfigVar}"</js>).
	 * </ul>
	 *
	 * <ul class='seealso'>
	 * 	<li class='jm'>{@link BeanContextBuilder#bpx(Class, String)}
	 * 	<li class='jm'>{@link BeanContextBuilder#bpx(String, String)}
	 * 	<li class='jm'>{@link BeanContextBuilder#bpx(Map)}
	 * </ul>
	 */
	String[] bpx() default {};

	/**
	 * Configuration property:  Read-only bean properties.
	 *
	 * Shortcut for specifying the {@link Bean#bpwo()} annotation for all parsers.
	 *
	 * <p>
	 * Specifies one or more properties on a bean that are read-only despite having valid getters.
	 * Serializers will serialize such properties as usual, but parsers will silently ignore them.
	 *
	 * <h5 class='section'>Example:</h5>
	 * <p class='bcode w800'>
	 * 	<jc>// Ignore 'a' and 'b' properties if they're present in the HTTP request body.</jc>
	 * 	<ja>@RestMethod</ja>(name=<jsf>POST</jsf>, path=<js>"/mybeans"</js>)
	 * 	<ja>@BeanConfig</ja>(bpro=<js>"MyBean: a,b"</js>)
	 * 	<jk>public void</jk> postMyBeans(List&lt;MyBean&gt; l) {...}
	 * </p>
	 *
	 * <ul class='notes'>
	 * 	<li>
	 * 		The format of each value is: <js>"Key: comma-delimited-tokens"</js>.
	 * 	<li>
	 * 		Keys can be fully-qualified or short class names or <js>"*"</js> to represent all classes.
	 * 	<li>
	 * 		Values are comma-delimited lists of bean property names.
	 * 	<li>
	 * 		Properties apply to specified class and all subclasses.
	 * 	<li>
	 * 		Semicolons can be used as an additional separator for multiple values:
	 * 		<p class='bcode w800'>
	 * 	<jc>// Equivalent</jc>
	 * 	bpro={<js>"Bean1: foo"</js>,<js>"Bean2: bar,baz"</js>}
	 * 	bpro=<js>"Bean1: foo; Bean2: bar,baz"</js>
	 * 		</p>
	 * 	<li>
	 * 		Supports {@doc DefaultVarResolver} (e.g. <js>"$C{myConfigVar}"</js>).
	 * </ul>
	 *
	 * <ul class='seealso'>
	 * 	<li class='jm'>{@link BeanContextBuilder#bpro(Class, String)}
	 * 	<li class='jm'>{@link BeanContextBuilder#bpro(String, String)}
	 * 	<li class='jm'>{@link BeanContextBuilder#bpro(Map)}
	 * </ul>
	 */
	String[] bpro() default {};

	/**
	 * Configuration property:  Write-only bean properties.
	 *
	 * Shortcut for specifying the {@link Bean#bpwo()} annotation for all serializers.
	 *
	 * <p>
	 * Specifies one or more properties on a bean that are write-only despite having valid setters.
	 * Parsers will parse such properties as usual, but serializers will silently ignore them.
	 *
	 * <h5 class='section'>Example:</h5>
	 * <p class='bcode w800'>
	 * 	<jc>// Ignore 'a' and 'b' properties when serializing the response body.</jc>
	 * 	<ja>@RestMethod</ja>(name=<jsf>GET</jsf>, path=<js>"/mybeans"</js>)
	 * 	<ja>@BeanConfig</ja>(bpwo=<js>"MyBean: a,b"</js>)
	 * 	<jk>public</jk> List&lt;MyBean&gt; getMyBeans() {...}
	 * </p>
	 *
	 * <ul class='notes'>
	 * 	<li>
	 * 		The format of each value is: <js>"Key: comma-delimited-tokens"</js>.
	 * 	<li>
	 * 		Keys can be fully-qualified or short class names or <js>"*"</js> to represent all classes.
	 * 	<li>
	 * 		Values are comma-delimited lists of bean property names.
	 * 	<li>
	 * 		Properties apply to specified class and all subclasses.
	 * 	<li>
	 * 		Semicolons can be used as an additional separator for multiple values:
	 * 		<p class='bcode w800'>
	 * 	<jc>// Equivalent</jc>
	 * 	bpwo={<js>"Bean1: foo"</js>,<js>"Bean2: bar,baz"</js>}
	 * 	bpwo=<js>"Bean1: foo; Bean2: bar,baz"</js>
	 * 		</p>
	 * 	<li>
	 * 		Supports {@doc DefaultVarResolver} (e.g. <js>"$C{myConfigVar}"</js>).
	 * </ul>
	 *
	 * <ul class='seealso'>
	 * 	<li class='jm'>{@link BeanContextBuilder#bpwo(Class, String)}
	 * 	<li class='jm'>{@link BeanContextBuilder#bpwo(String, String)}
	 * 	<li class='jm'>{@link BeanContextBuilder#bpwo(Map)}
	 * </ul>
	 */
	String[] bpwo() default {};

	/**
	 * Configuration property:  Debug mode.
	 *
	 * <p>
	 * Enables the following additional information during serialization:
	 * <ul class='spaced-list'>
	 * 	<li>
	 * 		When bean getters throws exceptions, the exception includes the object stack information
	 * 		in order to determine how that method was invoked.
	 * 	<li>
	 * 		Enables {@link Serializer#BEANTRAVERSE_detectRecursions}.
	 * </ul>
	 *
	 * <p>
	 * Enables the following additional information during parsing:
	 * <ul class='spaced-list'>
	 * 	<li>
	 * 		When bean setters throws exceptions, the exception includes the object stack information
	 * 		in order to determine how that method was invoked.
	 * </ul>
	 *
	 * <ul class='notes'>
	 * 	<li>
	 * 		Possible values:
	 * 		<ul>
	 * 			<li><js>"true"</js>
	 * 			<li><js>"false"</js> (default)
	 * 		</ul>
	 * 	<li>
	 * 		Supports {@doc DefaultVarResolver} (e.g. <js>"$C{myConfigVar}"</js>).
	 * </ul>
	 *
	 * <ul class='seealso'>
	 * 	<li class='jf'>{@link Context#CONTEXT_debug}
	 * </ul>
	 */
	String debug() default "";

	/**
	 * Configuration property:  Bean dictionary.
	 *
	 * <p>
	 * The list of classes that make up the bean dictionary in this bean context.
	 *
	 * <p>
	 * A dictionary is a name/class mapping used to find class types during parsing when they cannot be inferred
	 * through reflection.
	 * <br>The names are defined through the {@link Bean#typeName() @Bean(typeName)} annotation defined on the bean class.
	 * <br>For example, if a class <c>Foo</c> has a type-name of <js>"myfoo"</js>, then it would end up serialized
	 * as <js>"{_type:'myfoo',...}"</js>.
	 *
	 * <p>
	 * This setting tells the parsers which classes to look for when resolving <js>"_type"</js> attributes.
	 *
	 * <ul class='seealso'>
	 * 	<li class='jf'>{@link BeanContext#BEAN_beanDictionary}
	 * 	<li class='link'>{@doc BeanDictionaries}
	 * </ul>
	 */
	Class<?>[] dictionary() default {};

	/**
	 * Configuration property:  Add to bean dictionary.
	 *
	 * <ul class='seealso'>
	 * 	<li class='jf'>{@link BeanContext#BEAN_beanDictionary}
	 * </ul>
	 */
	Class<?>[] dictionary_replace() default {};

	/**
	 * Configuration property:  Remove from bean dictionary.
	 *
	 * <ul class='seealso'>
	 * 	<li class='jf'>{@link BeanContext#BEAN_beanDictionary_remove}
	 * </ul>
	 */
	Class<?>[] dictionary_remove() default {};

	/**
	 * Configuration property:  Find fluent setters.
	 *
	 * <p>
	 * When enabled, fluent setters are detected on beans.
	 *
	 * <p>
	 * Fluent setters must have the following attributes:
	 * <ul>
	 * 	<li>Public.
	 * 	<li>Not static.
	 * 	<li>Take in one parameter.
	 * 	<li>Return the bean itself.
	 * </ul>
	 *
	 * <ul class='notes'>
	 * 	<li>
	 * 		Possible values:
	 * 		<ul>
	 * 			<li><js>"true"</js>
	 * 			<li><js>"false"</js> (default)
	 * 		</ul>
	 * 	<li>
	 * 		Supports {@doc DefaultVarResolver} (e.g. <js>"$C{myConfigVar}"</js>).
	 * </ul>
	 *
	 * <ul class='seealso'>
	 * 	<li class='jf'>{@link BeanContext#BEAN_fluentSetters}
	 * </ul>
	 */
	String fluentSetters() default "";

	/**
	 * Configuration property:  Ignore invocation errors on getters.
	 *
	 * <p>
	 * If <js>"true"</js>, errors thrown when calling bean getter methods will silently be ignored.
	 * <br>Otherwise, a {@code BeanRuntimeException} is thrown.
	 *
	 * <ul class='notes'>
	 * 	<li>
	 * 		Possible values:
	 * 		<ul>
	 * 			<li><js>"true"</js>
	 * 			<li><js>"false"</js> (default)
	 * 		</ul>
	 * 	<li>
	 * 		Supports {@doc DefaultVarResolver} (e.g. <js>"$C{myConfigVar}"</js>).
	 * </ul>
	 *
	 * <ul class='seealso'>
	 * 	<li class='jf'>{@link BeanContext#BEAN_ignoreInvocationExceptionsOnGetters}
	 * </ul>
	 */
	String ignoreInvocationExceptionsOnGetters() default "";

	/**
	 * Configuration property:  Ignore invocation errors on setters.
	 *
	 * <p>
	 * If <js>"true"</js>, errors thrown when calling bean setter methods will silently be ignored.
	 * <br>Otherwise, a {@code BeanRuntimeException} is thrown.
	 *
	 * <ul class='notes'>
	 * 	<li>
	 * 		Possible values:
	 * 		<ul>
	 * 			<li><js>"true"</js>
	 * 			<li><js>"false"</js> (default)
	 * 		</ul>
	 * 	<li>
	 * 		Supports {@doc DefaultVarResolver} (e.g. <js>"$C{myConfigVar}"</js>).
	 * </ul>
	 *
	 * <ul class='seealso'>
	 * 	<li class='jf'>{@link BeanContext#BEAN_ignoreInvocationExceptionsOnSetters}
	 * </ul>
	 */
	String ignoreInvocationExceptionsOnSetters() default "";

	/**
	 * Configuration property:  Ignore properties without setters.
	 *
	 * <p>
	 * If <js>"true"</js>, trying to set a value on a bean property without a setter will silently be ignored.
	 * <br>Otherwise, a {@code RuntimeException} is thrown.
	 *
	 * <ul class='notes'>
	 * 	<li>
	 * 		Possible values:
	 * 		<ul>
	 * 			<li><js>"true"</js> (default)
	 * 			<li><js>"false"</js>
	 * 		</ul>
	 * 	<li>
	 * 		Supports {@doc DefaultVarResolver} (e.g. <js>"$C{myConfigVar}"</js>).
	 * </ul>
	 *
	 * <ul class='seealso'>
	 * 	<li class='jf'>{@link BeanContext#BEAN_ignorePropertiesWithoutSetters}
	 * </ul>
	 */
	String ignorePropertiesWithoutSetters() default "";

	/**
	 * Configuration property:  Ignore transient fields.
	 *
	 * <p>
	 * If <jk>true</jk>, methods and fields marked as <jk>transient</jk> will be ignored as bean properties.
	 *
	 * <ul class='notes'>
	 * 	<li>
	 * 		Possible values:
	 * 		<ul>
	 * 			<li><js>"true"</js> (default)
	 * 			<li><js>"false"</js>
	 * 		</ul>
	 * 	<li>
	 * 		Supports {@doc DefaultVarResolver} (e.g. <js>"$C{myConfigVar}"</js>).
	 * </ul>
	 *
	 * <ul class='seealso'>
	 * 	<li class='jf'>{@link BeanContext#BEAN_ignoreTransientFields}
	 * </ul>
	 */
	String ignoreTransientFields() default "";

	/**
	 * Configuration property:  Ignore unknown properties.
	 *
	 * <p>
	 * If <js>"true"</js>, trying to set a value on a non-existent bean property will silently be ignored.
	 * <br>Otherwise, a {@code RuntimeException} is thrown.
	 *
	 * <ul class='notes'>
	 * 	<li>
	 * 		Possible values:
	 * 		<ul>
	 * 			<li><js>"true"</js>
	 * 			<li><js>"false"</js> (default)
	 * 		</ul>
	 * 	<li>
	 * 		Supports {@doc DefaultVarResolver} (e.g. <js>"$C{myConfigVar}"</js>).
	 * </ul>
	 *
	 * <ul class='seealso'>
	 * 	<li class='jf'>{@link BeanContext#BEAN_ignoreUnknownBeanProperties}
	 * </ul>
	 */
	String ignoreUnknownBeanProperties() default "";

	/**
	 * Configuration property:  Ignore unknown properties with null values.
	 *
	 * <p>
	 * If <js>"true"</js>, trying to set a <jk>null</jk> value on a non-existent bean property will silently be ignored.
	 * <br>Otherwise, a {@code RuntimeException} is thrown.
	 *
	 * <ul class='notes'>
	 * 	<li>
	 * 		Possible values:
	 * 		<ul>
	 * 			<li><js>"true"</js> (default)
	 * 			<li><js>"false"</js>
	 * 		</ul>
	 * 	<li>
	 * 		Supports {@doc DefaultVarResolver} (e.g. <js>"$C{myConfigVar}"</js>).
	 * </ul>
	 *
	 * <ul class='seealso'>
	 * 	<li class='jf'>{@link BeanContext#BEAN_ignoreUnknownNullBeanProperties}
	 * </ul>
	 */
	String ignoreUnknownNullBeanProperties() default "";

	/**
	 * Configuration property:  Implementation classes.
	 *
	 * <p>
	 * For interfaces and abstract classes this method can be used to specify an implementation class for the
	 * interface/abstract class so that instances of the implementation class are used when instantiated (e.g. during a
	 * parse).
	 *
	 * <h5 class='section'>Example:</h5>
	 * <p class='bcode w800'>
	 * 	<ja>@BeanConfig</ja>(
	 * 		implClasses={
	 * 			<ja>@CC</ja>(key=MyInterface.<jk>class</jk>, value=MyInterfaceImpl.<jk>class</jk>)
	 * 		}
	 * 	)
	 * <p>
	 *
	 * <ul class='seealso'>
	 * 	<li class='jf'>{@link BeanContext#BEAN_implClasses}
	 * </ul>
	 */
	CC[] implClasses() default {};

	/**
	 * Identifies a set of interfaces.
	 *
	 * <p>
	 * When specified, only the list of properties defined on the interface class will be used during serialization
	 * of implementation classes.  Additional properties on subclasses will be ignored.
	 *
	 * <p class='bcode w800'>
	 * 	<jc>// Parent class or interface</jc>
	 * 	<jk>public abstract class</jk> A {
	 * 		<jk>public</jk> String <jf>foo</jf> = <js>"foo"</js>;
	 * 	}
	 *
	 * 	<jc>// Sub class</jc>
	 * 	<jk>public class</jk> A1 <jk>extends</jk> A {
	 * 		<jk>public</jk> String <jf>bar</jf> = <js>"bar"</js>;
	 * 	}
	 *
	 * 	<jc>// Apply it to a config</jc>
	 * 	<ja>@BeanConfig</ja>(
	 * 		interfaces={
	 * 			A.<jk>class</jk>
	 * 		}
	 * 	)
	 * </p>
	 *
	 * <p>
	 * This annotation can be used on the parent class so that it filters to all child classes, or can be set
	 * individually on the child classes.
	 *
	 * <ul class='notes'>
	 * 	<li>The {@link Bean#interfaceClass() @Bean(interfaceClass)} annotation is the equivalent annotation-based solution.
	 * </ul>
	 */
	Class<?>[] interfaces() default {};

	/**
	 * Configuration property:  Locale.
	 *
	 * <p>
	 * Specifies the default locale for serializer and parser sessions.
	 *
	 * <ul class='notes'>
	 * 	<li>
	 * 		Supports {@doc DefaultVarResolver} (e.g. <js>"$C{myConfigVar}"</js>).
	 * </ul>
	 *
	 * <ul class='seealso'>
	 * 	<li class='jf'>{@link Context#CONTEXT_locale}
	 * </ul>
	 */
	String locale() default "";

	/**
	 * Configuration property:  Media type.
	 *
	 * <p>
	 * Specifies the default media type value for serializer and parser sessions.
	 *
	 * <ul class='notes'>
	 * 	<li>
	 * 		Supports {@doc DefaultVarResolver} (e.g. <js>"$C{myConfigVar}"</js>).
	 * </ul>
	 *
	 * <ul class='seealso'>
	 * 	<li class='jf'>{@link Context#CONTEXT_mediaType}
	 * </ul>
	 */
	String mediaType() default "";

	/**
	 * Configuration property:  Bean class exclusions.
	 *
	 * <p>
	 * List of classes that should not be treated as beans even if they appear to be bean-like.
	 * <br>Not-bean classes are converted to <c>Strings</c> during serialization.
	 *
	 * <ul class='notes'>
	 * 	<li>
	 * 		Supports {@doc DefaultVarResolver} (e.g. <js>"$C{myConfigVar}"</js>).
	 * </ul>
	 *
	 * <ul class='seealso'>
	 * 	<li class='jf'>{@link BeanContext#BEAN_notBeanClasses}
	 * </ul>
	 */
	Class<?>[] notBeanClasses() default {};

	/**
	 * Configuration property:  Add to classes that should not be considered beans.
	 *
	 * <ul class='seealso'>
	 * 	<li class='jf'>{@link BeanContext#BEAN_notBeanClasses}
	 * </ul>
	 */
	Class<?>[] notBeanClasses_replace() default {};

	/**
	 * Configuration property:  Remove from classes that should not be considered beans.
	 *
	 * <ul class='seealso'>
	 * 	<li class='jf'>{@link BeanContext#BEAN_notBeanClasses}
	 * </ul>
	 */
	Class<?>[] notBeanClasses_remove() default {};

	/**
	 * Configuration property:  Bean package exclusions.
	 *
	 * <p>
	 * When specified, the current list of ignore packages are appended to.
	 *
	 * <p>
	 * Any classes within these packages will be serialized to strings using {@link Object#toString()}.
	 *
	 * <p>
	 * Note that you can specify suffix patterns to include all subpackages.
	 *
	 * <ul class='notes'>
	 * 	<li>
	 * 		The default value excludes the following packages:
	 * 		<ul>
	 * 			<li><c>java.lang</c>
	 * 			<li><c>java.lang.annotation</c>
	 * 			<li><c>java.lang.ref</c>
	 * 			<li><c>java.lang.reflect</c>
	 * 			<li><c>java.io</c>
	 * 			<li><c>java.net</c>
	 * 			<li><c>java.nio.*</c>
	 * 			<li><c>java.util.*</c>
	 * 		</ul>
	 * 	<li>
	 * 		Supports {@doc DefaultVarResolver} (e.g. <js>"$C{myConfigVar}"</js>).
	 * </ul>
	 *
	 * <ul class='seealso'>
	 * 	<li class='jf'>{@link BeanContext#BEAN_notBeanPackages}
	 * </ul>
	 */
	String[] notBeanPackages() default {};

	/**
	 * Configuration property:  Add to packages whose classes should not be considered beans.
	 *
	 * <ul class='seealso'>
	 * 	<li class='jf'>{@link BeanContext#BEAN_notBeanPackages}
	 * </ul>
	 */
	String[] notBeanPackages_replace() default {};

	/**
	 * Configuration property:  Remove from packages whose classes should not be considered beans.
	 *
	 * <ul class='seealso'>
	 * 	<li class='jf'>{@link BeanContext#BEAN_notBeanPackages}
	 * </ul>
	 */
	String[] notBeanPackages_remove() default {};

	/**
	 * Configuration property:  Bean property namer.
	 *
	 * <p>
	 * The class to use for calculating bean property names.
	 *
	 * <p>
	 * Predefined classes:
	 * <ul>
	 * 	<li>{@link PropertyNamerDefault} (default)
	 * 	<li>{@link PropertyNamerDLC} - Dashed-lower-case names.
	 * 	<li>{@link PropertyNamerULC} - Dashed-upper-case names.
	 * </ul>
	 *
	 * <ul class='seealso'>
	 * 	<li class='jf'>{@link BeanContext#BEAN_propertyNamer}
	 * </ul>
	 */
	Class<? extends PropertyNamer> propertyNamer() default PropertyNamer.Null.class;

	/**
	 * Configuration property:  Sort bean properties.
	 *
	 * <p>
	 * When <jk>true</jk>, all bean properties will be serialized and access in alphabetical order.
	 * <br>Otherwise, the natural order of the bean properties is used which is dependent on the JVM vendor.
	 * <br>On IBM JVMs, the bean properties are ordered based on their ordering in the Java file.
	 * <br>On Oracle JVMs, the bean properties are not ordered (which follows the official JVM specs).
	 *
	 * <p>
	 * This property is disabled by default so that IBM JVM users don't have to use {@link Bean @Bean} annotations
	 * to force bean properties to be in a particular order and can just alter the order of the fields/methods
	 * in the Java file.
	 *
	 * <ul class='notes'>
	 * 	<li>
	 * 		Possible values:
	 * 		<ul>
	 * 			<li><js>"true"</js>
	 * 			<li><js>"false"</js> (default)
	 * 		</ul>
	 * 	<li>
	 * 		Supports {@doc DefaultVarResolver} (e.g. <js>"$C{myConfigVar}"</js>).
	 * </ul>
	 *
	 * <ul class='seealso'>
	 * 	<li class='jf'>{@link BeanContext#BEAN_sortProperties}
	 * </ul>
	 */
	String sortProperties() default "";

	/**
	 * Configuration property:  Java object swaps.
	 *
	 * <p>
	 * Swaps are used to "swap out" non-serializable classes with serializable equivalents during serialization,
	 * and "swap in" the non-serializable class during parsing.
	 *
	 * <p>
	 * An example of a swap would be a <c>Calendar</c> object that gets swapped out for an ISO8601 string.
	 *
	 * <p>
	 * Multiple swaps can be associated with a single class.
	 * <br>When multiple swaps are applicable to the same class, the media type pattern defined by
	 * {@link PojoSwap#forMediaTypes()} or {@link Swap#mediaTypes() @Swap(mediaTypes)} are used to come up with the best match.
	 *
	 * <ul class='seealso'>
	 * 	<li class='jf'>{@link BeanContext#BEAN_swaps}
	 * 	<li class='link'>{@doc PojoSwaps}
	 * 	<li class='link'>{@doc PerMediaTypePojoSwaps}
	 * 	<li class='link'>{@doc OneWayPojoSwaps}
	 * 	<li class='link'>{@doc SwapAnnotation}
	 * 	<li class='link'>{@doc AutoPojoSwaps}
	 * 	<li class='link'>{@doc SurrogateClasses}
	 * </ul>
	 */
	Class<?>[] swaps() default {};

	/**
	 * Configuration property:  Add to Java object swap classes.
	 *
	 * <ul class='seealso'>
	 * 	<li class='jf'>{@link BeanContext#BEAN_swaps}
	 * </ul>
	 */
	Class<?>[] swaps_replace() default {};

	/**
	 * Configuration property:  Remove from Java object swap classes.
	 *
	 * <ul class='seealso'>
	 * 	<li class='jf'>{@link BeanContext#BEAN_swaps}
	 * </ul>
	 */
	Class<?>[] swaps_remove() default {};

	/**
	 * Configuration property:  Time zone.
	 *
	 * <p>
	 * Specifies the default timezone for serializer and parser sessions.
	 *
	 * <ul class='notes'>
	 * 	<li>
	 * 		Supports {@doc DefaultVarResolver} (e.g. <js>"$C{myConfigVar}"</js>).
	 * </ul>
	 *
	 * <ul class='seealso'>
	 * 	<li class='jf'>{@link Context#CONTEXT_timeZone}
	 * </ul>
	 */
	String timeZone() default "";

	/**
	 * Configuration property:  Use enum names.
	 *
	 * <p>
	 * When enabled, enums are always serialized by name, not using {@link Object#toString()}.
	 *
	 * <ul class='notes'>
	 * 	<li>
	 *		Possible values:
	 * 		<ul>
	 * 			<li><js>"true"</js>
	 * 			<li><js>"false"</js> (default)
	 * 		</ul>
	 * 	<li>
	 * 		Supports {@doc DefaultVarResolver} (e.g. <js>"$C{myConfigVar}"</js>).
	 * </ul>
	 *
	 * <ul class='seealso'>
	 * 	<li class='jf'>{@link BeanContext#BEAN_useEnumNames}
	 * </ul>
	 */
	String useEnumNames() default "";

	/**
	 * Configuration property:  Use interface proxies.
	 *
	 * <p>
	 * If <js>"true"</js>, then interfaces will be instantiated as proxy classes through the use of an
	 * {@link InvocationHandler} if there is no other way of instantiating them.
	 * <br>Otherwise, throws a {@link BeanRuntimeException}.
	 *
	 * <ul class='notes'>
	 * 	<li>
	 *		Possible values:
	 * 		<ul>
	 * 			<li><js>"true"</js> (default)
	 * 			<li><js>"false"</js>
	 * 		</ul>
	 * 	<li>
	 * 		Supports {@doc DefaultVarResolver} (e.g. <js>"$C{myConfigVar}"</js>).
	 * </ul>
	 *
	 * <ul class='seealso'>
	 * 	<li class='jf'>{@link BeanContext#BEAN_useInterfaceProxies}
	 * </ul>
	 */
	String useInterfaceProxies() default "";

	/**
	 * Configuration property:  Use Java Introspector.
	 *
	 * <p>
	 * Using the built-in Java bean introspector will not pick up fields or non-standard getters/setters.
	 * <br>Most {@link Bean @Bean} annotations will be ignored.
	 *
	 * <ul class='notes'>
	 * 	<li>
	 *		Possible values:
	 * 		<ul>
	 * 			<li><js>"true"</js>
	 * 			<li><js>"false"</js> (default)
	 * 		</ul>
	 * 	<li>
	 * 		Supports {@doc DefaultVarResolver} (e.g. <js>"$C{myConfigVar}"</js>).
	 * </ul>
	 *
	 * <ul class='seealso'>
	 * 	<li class='jf'>{@link BeanContext#BEAN_useJavaBeanIntrospector}
	 * </ul>
	 */
	String useJavaBeanIntrospector() default "";

	//-----------------------------------------------------------------------------------------------------------------
	// BeanTraverseContext
	//-----------------------------------------------------------------------------------------------------------------

	/**
	 * Configuration property:  Automatically detect POJO recursions.
	 *
	 * <p>
	 * Specifies that recursions should be checked for during traversal.
	 *
	 * <p>
	 * Recursions can occur when traversing models that aren't true trees but rather contain loops.
	 * <br>In general, unchecked recursions cause stack-overflow-errors.
	 * <br>These show up as {@link ParseException ParseExceptions} with the message <js>"Depth too deep.  Stack overflow occurred."</js>.
	 *
	 * <p>
	 * The behavior when recursions are detected depends on the value for {@link BeanTraverseContext#BEANTRAVERSE_ignoreRecursions}.
	 *
	 * <p>
	 * For example, if a model contains the links A-&gt;B-&gt;C-&gt;A, then the JSON generated will look like
	 * 	the following when <jsf>BEANTRAVERSE_ignoreRecursions</jsf> is <jk>true</jk>...
	 *
	 * <p class='bcode w800'>
	 * 	{A:{B:{C:<jk>null</jk>}}}
	 * </p>
	 *
	 * <ul class='notes'>
	 * 	<li>
	 * 		Checking for recursion can cause a small performance penalty.
	 * 	<li>
	 *		Possible values:
	 * 		<ul>
	 * 			<li><js>"true"</js>
	 * 			<li><js>"false"</js> (default)
	 * 		</ul>
	 * 	<li>
	 * 		Supports {@doc DefaultVarResolver} (e.g. <js>"$C{myConfigVar}"</js>).
	 * </ul>
	 *
	 * <ul class='seealso'>
	 * 	<li class='jf'>{@link BeanTraverseContext#BEANTRAVERSE_detectRecursions}
	 * </ul>
	 */
	String detectRecursions() default "";

	/**
	 * Configuration property:  Ignore recursion errors.
	 *
	 * <p>
	 * Used in conjunction with {@link BeanTraverseContext#BEANTRAVERSE_detectRecursions}.
	 * <br>Setting is ignored if <jsf>BEANTRAVERSE_detectRecursions</jsf> is <js>"false"</js>.
	 *
	 * <p>
	 * If <js>"true"</js>, when we encounter the same object when traversing a tree, we set the value to <jk>null</jk>.
	 * <br>Otherwise, a {@link BeanRecursionException} is thrown with the message <js>"Recursion occurred, stack=..."</js>.
	 *
	 * <ul class='notes'>
	 * 	<li>
	 *		Possible values:
	 * 		<ul>
	 * 			<li><js>"true"</js>
	 * 			<li><js>"false"</js> (default)
	 * 		</ul>
	 * 	<li>
	 * 		Supports {@doc DefaultVarResolver} (e.g. <js>"$C{myConfigVar}"</js>).
	 * </ul>
	 *
	 * <ul class='seealso'>
	 * 	<li class='jf'>{@link BeanTraverseContext#BEANTRAVERSE_ignoreRecursions}
	 * </ul>
	 */
	String ignoreRecursions() default "";

	/**
	 * Configuration property:  Initial depth.
	 *
	 * <p>
	 * The initial indentation level at the root.
	 * <br>Useful when constructing document fragments that need to be indented at a certain level.
	 *
	 * <ul class='notes'>
	 * 	<li>
	 * 		Format: integer
	 *	<li>
	 * 		Default value: <js>"0"</js>
	 * 	<li>
	 * 		Supports {@doc DefaultVarResolver} (e.g. <js>"$C{myConfigVar}"</js>).
	 * </ul>
	 *
	 * <ul class='seealso'>
	 * 	<li class='jf'>{@link BeanTraverseContext#BEANTRAVERSE_initialDepth}
	 * </ul>
	 */
	String initialDepth() default "";

	/**
	 * Configuration property:  Max traversal depth.
	 *
	 * <p>
	 * Abort traversal if specified depth is reached in the POJO tree.
	 * <br>If this depth is exceeded, an exception is thrown.
	 *
	 * <ul class='notes'>
	 * 	<li>
	 * 		Format: integer
	 * 	<li>
	 * 		Default value: <js>"100"</js>
	 * 	<li>
	 * 		Supports {@doc DefaultVarResolver} (e.g. <js>"$C{myConfigVar}"</js>).
	 * </ul>
	 *
	 * <ul class='seealso'>
	 * 	<li class='jf'>{@link BeanTraverseContext#BEANTRAVERSE_maxDepth}
	 * </ul>
	 */
	String maxDepth() default "";
}
