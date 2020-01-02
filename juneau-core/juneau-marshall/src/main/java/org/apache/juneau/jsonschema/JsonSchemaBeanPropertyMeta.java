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
package org.apache.juneau.jsonschema;

import java.lang.reflect.*;

import org.apache.juneau.*;
import org.apache.juneau.jsonschema.annotation.*;
import org.apache.juneau.parser.*;

/**
 * Metadata on bean properties specific to the JSON-Schema pulled from the {@link Schema @Schema} annotation
 * on the bean property.
 */
public class JsonSchemaBeanPropertyMeta extends ExtendedBeanPropertyMeta {

	/**
	 * Default instance.
	 */
	public static final JsonSchemaBeanPropertyMeta DEFAULT = new JsonSchemaBeanPropertyMeta();

	private final ObjectMap schema;

	/**
	 * Constructor.
	 *
	 * @param bpm The metadata of the bean property of this additional metadata.
	 * @param mp JSON-schema metadata provider (for finding information about other artifacts).
	 */
	public JsonSchemaBeanPropertyMeta(BeanPropertyMeta bpm, JsonSchemaMetaProvider mp) {
		super(bpm);

		this.schema = new ObjectMap();

		Field field = bpm.getInnerField();
		Method getter = bpm.getGetter(), setter = bpm.getSetter();

		try {
			if (field != null)
				schema.appendAll(SchemaUtils.asMap(mp.getAnnotation(Schema.class, field)));
			if (getter != null)
				schema.appendAll(SchemaUtils.asMap(mp.getAnnotation(Schema.class, getter)));
			if (setter != null)
				schema.appendAll(SchemaUtils.asMap(mp.getAnnotation(Schema.class, setter)));
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}

	private JsonSchemaBeanPropertyMeta() {
		super(null);
		this.schema = ObjectMap.EMPTY_MAP;
	}

	/**
	 * Returns the schema information gathered from all the {@link Schema @Schema} annotations on the bean property.
	 *
	 * @return The schema information as a generic map.  Never <jk>null</jk>.
	 */
	protected ObjectMap getSchema() {
		return schema;
	}
}
