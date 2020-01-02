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
package org.apache.juneau.jena;

import java.util.*;

import org.apache.juneau.*;
import org.apache.juneau.jena.annotation.*;
import org.apache.juneau.reflect.*;
import org.apache.juneau.xml.*;

/**
 * Metadata on classes specific to the RDF serializers and parsers pulled from the {@link Rdf @Rdf} annotation on the
 * class.
 */
public class RdfClassMeta extends ExtendedClassMeta {

	private final Rdf rdf;
	private final RdfCollectionFormat collectionFormat;
	private final Namespace namespace;

	/**
	 * Constructor.
	 *
	 * @param cm The class that this annotation is defined on.
	 * @param mp RDF metadata provider (for finding information about other artifacts).
	 */
	public RdfClassMeta(ClassMeta<?> cm, RdfMetaProvider mp) {
		super(cm);
		ClassInfo ci = cm.getInfo();
		this.rdf = mp.getAnnotation(Rdf.class, cm.getInnerClass());
		if (rdf != null) {
			collectionFormat = rdf.collectionFormat();
		} else {
			collectionFormat = RdfCollectionFormat.DEFAULT;
		}
		List<Rdf> rdfs = ci.getAnnotations(Rdf.class, mp);
		List<RdfSchema> schemas = ci.getAnnotations(RdfSchema.class, mp);
		this.namespace = RdfUtils.findNamespace(rdfs, schemas);
	}

	/**
	 * Returns the {@link Rdf @Rdf} annotation defined on the class.
	 *
	 * @return The value of the annotation, or <jk>null</jk> if annotation is not specified.
	 */
	protected Rdf getAnnotation() {
		return rdf;
	}

	/**
	 * Returns the {@link Rdf#collectionFormat() @Rdf(collectionFormat)} annotation defined on the class.
	 *
	 * @return The value of the annotation, or <jk>null</jk> if annotation is not
	 * specified.
	 */
	protected RdfCollectionFormat getCollectionFormat() {
		return collectionFormat;
	}

	/**
	 * Returns the RDF namespace associated with this class.
	 *
	 * <p>
	 * Namespace is determined in the following order of {@link Rdf#prefix() @Rdf(prefix)} annotation:
	 * <ol>
	 * 	<li>Class.
	 * 	<li>Package.
	 * 	<li>Superclasses.
	 * 	<li>Superclass packages.
	 * 	<li>Interfaces.
	 * 	<li>Interface packages.
	 * </ol>
	 *
	 * @return The namespace associated with this class, or <jk>null</jk> if no namespace is associated with it.
	 */
	protected Namespace getNamespace() {
		return namespace;
	}
}
