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
package org.apache.juneau.rest.params;

import static org.apache.juneau.internal.ClassUtils.*;
import static org.apache.juneau.internal.StringUtils.*;

import java.lang.reflect.*;

import org.apache.juneau.*;
import org.apache.juneau.http.annotation.*;
import org.apache.juneau.httppart.*;
import org.apache.juneau.internal.*;
import org.apache.juneau.reflect.*;
import org.apache.juneau.rest.*;
import org.apache.juneau.rest.annotation.*;
import org.apache.juneau.rest.util.*;

/**
 * Resolves method parameters and parameter types annotated with {@link Path} on {@link RestOp}-annotated Java methods.
 *
 * <p>
 * The parameter value is resolved using <c><jv>call</jv>.{@link RestCall#getRestRequest() getRestRequest}().{@link RestRequest#getPathMatch() getPathMatch}().{@link RequestPath#get(HttpPartParserSession,HttpPartSchema,String,Type,Type...) get}(<jv>parserSession<jv>, <jv>schema</jv>, <jv>name</jv>, <jv>type</jv>)</c>
 * with a {@link HttpPartSchema schema} derived from the {@link Path} annotation.
 */
public class PathParam implements RestOperationParam {
	private final HttpPartParser partParser;
	private final HttpPartSchema schema;
	private final String name;
	private final Type type;

	/**
	 * Static creator.
	 *
	 * @param paramInfo The Java method parameter being resolved.
	 * @param ps The configuration properties of the {@link RestContext}.
	 * @param pathMatcher Path matcher for the specified method.
	 * @return A new {@link PathParam}, or <jk>null</jk> if the parameter is not annotated with {@link Path}.
	 */
	public static PathParam create(ParamInfo paramInfo, PropertyStore ps, UrlPathMatcher pathMatcher) {
		if (paramInfo.hasAnnotation(Path.class) || paramInfo.getParameterType().hasAnnotation(Path.class))
			return new PathParam(paramInfo, ps, pathMatcher);
		return null;
	}

	/**
	 * Constructor.
	 *
	 * @param paramInfo The Java method parameter being resolved.
	 * @param ps The configuration properties of the {@link RestContext}.
	 * @param pathMatcher Path matcher for the specified method.
	 */
	protected PathParam(ParamInfo paramInfo, PropertyStore ps, UrlPathMatcher pathMatcher) {
		this.name = getName(paramInfo, pathMatcher);
		this.type = paramInfo.getParameterType().innerType();
		this.schema = HttpPartSchema.create(Path.class, paramInfo);
		this.partParser = castOrCreate(HttpPartParser.class, schema.getParser(), true, ps);
	}

	private String getName(ParamInfo paramInfo, UrlPathMatcher pathMatcher) {
		String p = null;
		for (Path h : paramInfo.getAnnotations(Path.class))
			p = firstNonEmpty(h.name(), h.n(), h.value(), p);
		for (Path h : paramInfo.getParameterType().getAnnotations(Path.class))
			p = firstNonEmpty(h.name(), h.n(), h.value(), p);
		if (p != null)
			return p;
		if (pathMatcher != null) {
			int idx = 0;
			int i = paramInfo.getIndex();
			MethodInfo mi = paramInfo.getMethod();

			for (int j = 0; j < i; j++)
				if (mi.getParam(i).getLastAnnotation(Path.class) != null)
					idx++;

			String[] vars = pathMatcher.getVars();
			if (vars.length <= idx)
				throw new ParameterException(paramInfo, "Number of attribute parameters exceeds the number of URL pattern variables");

			// Check for {#} variables.
			String idxs = String.valueOf(idx);
			for (int j = 0; j < vars.length; j++)
				if (StringUtils.isNumeric(vars[j]) && vars[j].equals(idxs))
					return vars[j];

			return pathMatcher.getVars()[idx];
		}
		throw new ParameterException(paramInfo, "@Path used without name or value");
	}

	@Override /* RestOperationParam */
	public Object resolve(RestCall call) throws Exception {
		RestRequest req = call.getRestRequest();
		HttpPartParserSession ps = partParser == null ? req.getPartParser() : partParser.createPartSession(req.getParserSessionArgs());
		return call.getRestRequest().getPathMatch().get(ps, schema, name, type);
	}
}
