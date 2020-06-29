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

import static org.apache.juneau.assertions.Assertions.*;
import static org.apache.juneau.httppart.HttpPartSchema.*;
import static org.apache.juneau.AddFlag.*;

import java.io.*;
import java.util.*;

import org.apache.http.*;
import org.apache.juneau.collections.*;
import org.apache.juneau.http.*;
import org.apache.juneau.rest.*;
import org.apache.juneau.rest.annotation.*;
import org.apache.juneau.rest.client2.RestClient_Test.*;
import org.apache.juneau.rest.mock2.*;
import org.apache.juneau.testutils.*;
import org.apache.juneau.uon.*;
import org.junit.*;

public class RestClient_FormData_Test {

	@Rest
	public static class A extends BasicRest {
		@RestMethod
		public Reader postFormData(org.apache.juneau.rest.RestRequest req) {
			return new StringReader(req.getFormData().asQueryString());
		}
	}

	//-----------------------------------------------------------------------------------------------------------------
	// Basic tests
	//-----------------------------------------------------------------------------------------------------------------

	@Test
	public void a01_formData() throws Exception {
		client().formData("foo","bar").formData("foo",new StringBuilder("baz")).build().post("/formData").run().assertBody().is("foo=bar&foo=baz");
		client().build().post("/formData").formData("foo","bar").formData("foo",new StringBuilder("baz")).run().assertBody().is("foo=bar&foo=baz");

		client().formData(pair("foo", "bar")).build().post("/formData").formData(pair("foo", "baz")).run().assertBody().is("foo=bar&foo=baz");

		client().formData(pair("foo", "bar")).build().post("/formData").formData(APPEND,"foo","baz").run().assertBody().is("foo=bar&foo=baz");
		client().formData(pair("foo", "bar")).build().post("/formData").formData(PREPEND,"foo","baz").run().assertBody().is("foo=baz&foo=bar");
		client().formData(pair("foo", "bar")).build().post("/formData").formData(REPLACE,"foo","baz").run().assertBody().is("foo=baz");
	}

	@Test
	public void a02_formDatas() throws Exception {
		client().formDatas(pair("foo","bar")).build().post("/formData").run().assertBody().is("foo=bar");
		client().formDatas(OMap.of("foo","bar")).build().post("/formData").run().assertBody().is("foo=bar");
		client().formDatas(AMap.of("foo","bar")).build().post("/formData").run().assertBody().is("foo=bar");
		client().formDatas(pairs("foo","bar","foo","baz")).build().post("/formData").run().assertBody().is("foo=bar&foo=baz");
		client().formDatas(pair("foo","bar"), pair("foo","baz")).build().post("/formData").run().assertBody().is("foo=bar&foo=baz");
		client().formDatas((Object)new NameValuePair[]{pair("foo","bar")}).build().post("/formData").run().assertBody().is("foo=bar");

		client().build().post("/formData").formDatas(pair("foo","bar")).run().assertBody().is("foo=bar");
		client().build().post("/formData").formDatas(OMap.of("foo","bar")).run().assertBody().is("foo=bar");
		client().build().post("/formData").formDatas(AMap.of("foo","bar")).run().assertBody().is("foo=bar");
		client().build().post("/formData").formDatas(pairs("foo","bar","foo","baz")).run().assertBody().is("foo=bar&foo=baz");
		client().build().post("/formData").formDatas(pair("foo","bar"), pair("foo","baz")).run().assertBody().is("foo=bar&foo=baz");
		client().build().post("/formData").formDatas((Object)new NameValuePair[]{pair("foo","bar")}).run().assertBody().is("foo=bar");

		client().build().post("/formData").formDatas(ABean.get()).run().assertBody().is("f=1");

		client().formDatas(pair("foo","bar"), null).build().post("/formData").run().assertBody().is("foo=bar");
		client().build().post("/formData").formDatas(pair("foo","bar"), null).run().assertBody().is("foo=bar");
		client().formDatas(pair("foo",null)).build().post("/formData").run().assertBody().is("");
		client().formDatas(pair(null,"foo")).build().post("/formData").run().assertBody().is("null=foo");
		client().formDatas(pair(null,null)).build().post("/formData").run().assertBody().is("");

		client().build().post("/formData").formDatas(pair("foo",null)).run().assertBody().is("");
		client().build().post("/formData").formDatas(pair(null,"foo")).run().assertBody().is("null=foo");
		client().build().post("/formData").formDatas(pair(null,null)).run().assertBody().is("");

		client().formDatas(SerializedHeader.create().name("foo").value("bar")).build().post("/formData").run().assertBody().is("foo=bar");

		assertThrown(()->{client().build().post("/formData").formDatas("bad");}).is("Invalid type passed to formDatas(): java.lang.String");
		assertThrown(()->{client().formDatas(pair("foo","bar"), "baz");}).is("Invalid type passed to formData():  java.lang.String");
	}

	@Test
	public void a03_formDataPairs() throws Exception {
		List<String> l1 = AList.of("bar1","bar2"), l2 = AList.of("qux1","qux2");

		client().formDataPairs("foo","bar","baz","qux").build().post("/formData").run().assertBody().is("foo=bar&baz=qux");
		client().formDataPairs("foo",l1,"baz",l2).build().post("/formData").run().assertBody().urlDecode().is("foo=bar1,bar2&baz=qux1,qux2");

		client().build().post("/formData").formDataPairs("foo","bar","baz","qux").run().assertBody().is("foo=bar&baz=qux");
		client().build().post("/formData").formDataPairs("foo",l1,"baz",l2).run().assertBody().urlDecode().is("foo=bar1,bar2&baz=qux1,qux2");

		assertThrown(()->{client().formDataPairs("foo","bar","baz");}).is("Odd number of parameters passed into formDataPairs()");
		assertThrown(()->{client().build().post("").formDataPairs("foo","bar","baz");}).is("Odd number of parameters passed into formDataPairs()");
	}

	@Test
	public void a04_formData_withSchema() throws Exception {
		List<String> l1 = AList.of("bar","baz"), l2 = AList.of("qux","quux");

		client().formData("foo",l1, T_ARRAY_PIPES).build().post("/formData").run().assertBody().urlDecode().is("foo=bar|baz");
		client().build().post("/formData").formData("foo",l1, T_ARRAY_PIPES).run().assertBody().urlDecode().is("foo=bar|baz");

		client().formData("foo",l1, T_ARRAY_PIPES).build().post("/formData").formData("foo",l2, T_ARRAY_PIPES).run().assertBody().urlDecode().is("foo=bar|baz&foo=qux|quux");
		client().formData("foo",l1, T_ARRAY_PIPES).build().post("/formData").formData(APPEND,"foo",l2, T_ARRAY_PIPES).run().assertBody().urlDecode().is("foo=bar|baz&foo=qux|quux");
		client().formData("foo",l1, T_ARRAY_PIPES).build().post("/formData").formData(PREPEND,"foo",l2, T_ARRAY_PIPES).run().assertBody().urlDecode().is("foo=qux|quux&foo=bar|baz");
		client().formData("foo",l1, T_ARRAY_PIPES).build().post("/formData").formData(REPLACE,"foo",l2, T_ARRAY_PIPES).run().assertBody().urlDecode().is("foo=qux|quux");

		client().formData("foo",l1, T_ARRAY_PIPES, UonSerializer.DEFAULT).build().post("/formData").run().assertBody().urlDecode().is("foo=@(bar,baz)");
	}

	@Test
	public void a05_formData_withSupplier() throws Exception {
		TestSupplier s = TestSupplier.of(null);

		RestClient x1 = client().formData("foo", s).build();
		s.set(OList.of("foo","bar"));
		x1.post("/formData").run().assertBody().is("foo=foo%2Cbar").assertBody().urlDecode().is("foo=foo,bar");
		s.set(OList.of("bar","baz"));
		x1.post("/formData").run().assertBody().is("foo=bar%2Cbaz").assertBody().urlDecode().is("foo=bar,baz");

		RestClient x2 = client().build();
		s.set(OList.of("foo","bar"));
		x2.post("/formData").formData("foo", s).run().assertBody().is("foo=foo%2Cbar").assertBody().urlDecode().is("foo=foo,bar");
		s.set(OList.of("bar","baz"));
		x2.post("/formData").formData("foo", s).run().assertBody().is("foo=bar%2Cbaz").assertBody().urlDecode().is("foo=bar,baz");

		s = TestSupplier.of(OList.of("foo","bar"));
		RestClient x = client().formData("foo", s, T_ARRAY_PIPES, new K12a()).build();
		x.post("/formData").run().assertBody().is("foo=x%5B%27foo%27%2C%27bar%27%5D").assertBody().urlDecode().is("foo=x['foo','bar']");
		s.set(OList.of("bar","baz"));
		x.post("/formData").run().assertBody().is("foo=x%5B%27bar%27%2C%27baz%27%5D").assertBody().urlDecode().is("foo=x['bar','baz']");
	}

	@Test
	public void a06_formData_withSupplierAndSchema() throws Exception {
		TestSupplier s = TestSupplier.of(null);

		RestClient x1 = client().formData("foo", s, T_ARRAY_PIPES).build();
		s.set(AList.of("foo","bar"));
		x1.post("/formData").run().assertBody().is("foo=foo%7Cbar").assertBody().urlDecode().is("foo=foo|bar");
		s.set(AList.of("bar","baz"));
		x1.post("/formData").run().assertBody().is("foo=bar%7Cbaz").assertBody().urlDecode().is("foo=bar|baz");

		RestClient x2 = client().build();
		s.set(AList.of("foo","bar"));
		x2.post("/formData").formData("foo", s, T_ARRAY_PIPES).run().assertBody().urlDecode().is("foo=foo|bar");
		s.set(AList.of("bar","baz"));
		x2.post("/formData").formData("foo", s, T_ARRAY_PIPES).run().assertBody().urlDecode().is("foo=bar|baz");
	}

	//------------------------------------------------------------------------------------------------------------------
	// Helper methods.
	//------------------------------------------------------------------------------------------------------------------

	private static NameValuePair pair(String name, Object val) {
		return BasicNameValuePair.of(name, val);
	}

	private static NameValuePairs pairs(Object...pairs) {
		return NameValuePairs.of(pairs);
	}

	private static RestClientBuilder client() {
		return MockRestClient.create(A.class).simpleJson();
	}
}