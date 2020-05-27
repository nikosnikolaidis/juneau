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
package org.apache.juneau.rest.annotation;

import static org.junit.runners.MethodSorters.*;

import org.apache.juneau.html.annotation.*;
import org.apache.juneau.rest.*;
import org.apache.juneau.rest.mock2.*;
import org.junit.*;

@FixMethodOrder(NAME_ASCENDING)
@SuppressWarnings({"serial"})
public class HtmlDocConfigHeaderTest {

	//=================================================================================================================
	// Basic tests
	//=================================================================================================================

	@Rest()
	@HtmlDocConfig(header={"a01a","a01b"})
	public static class A extends BasicRestServlet {
		@RestMethod(path="/a01")
		public Object a01() {
			return "OK";
		}
		@RestMethod(path="/a02")
		@HtmlDocConfig(header={"a02a","a02b"})
		public Object a02() {
			return "OK";
		}
		@RestMethod(path="/a03")
		@HtmlDocConfig(header={"INHERIT","a03a","a03b"})
		public Object a03() {
			return "OK";
		}
		@RestMethod(path="/a04")
		@HtmlDocConfig(header={"a04a","INHERIT","a04b"})
		public Object a04() {
			return "OK";
		}
		@RestMethod(path="/a05")
		@HtmlDocConfig(header={"a05a","a05b","INHERIT"})
		public Object a05() {
			return "OK";
		}
	}
	static MockRestClient a = MockRestClient.build(A.class);

	@Test
	public void a01() throws Exception {
		a.get("/a01").accept("text/html").run().assertBody().contains("<header>a01a a01b</header>");
	}
	@Test
	public void a02() throws Exception {
		a.get("/a02").accept("text/html").run().assertBody().contains("<header>a02a a02b</header>");
	}
	@Test
	public void a03() throws Exception {
		a.get("/a03").accept("text/html").run().assertBody().contains("<header>a01a a01b a03a a03b</header>");
	}
	@Test
	public void a04() throws Exception {
		a.get("/a04").accept("text/html").run().assertBody().contains("<header>a04a a01a a01b a04b</header>");
	}
	@Test
	public void a05() throws Exception {
		a.get("/a05").accept("text/html").run().assertBody().contains("<header>a05a a05b a01a a01b</header>");
	}

	//=================================================================================================================
	// Inheritance
	//=================================================================================================================

	@Rest()
	@HtmlDocConfig(header={"b01a","b01b","INHERIT"})
	public static class B extends A {
		@RestMethod(path="/b01")
		public Object b01() {
			return "OK";
		}
		@RestMethod(path="/b02")
		@HtmlDocConfig(header={"b02a","b02b"})
		public Object b02() {
			return "OK";
		}
		@RestMethod(path="/b03")
		@HtmlDocConfig(header={"INHERIT","b03a","b03b"})
		public Object b03() {
			return "OK";
		}
		@RestMethod(path="/b04")
		@HtmlDocConfig(header={"b04a","INHERIT","b04b"})
		public Object b04() {
			return "OK";
		}
		@RestMethod(path="/b05")
		@HtmlDocConfig(header={"b05a","b05b","INHERIT"})
		public Object b05() {
			return "OK";
		}
	}
	static MockRestClient b = MockRestClient.build(B.class);

	@Test
	public void b01() throws Exception {
		b.get("/b01").accept("text/html").run().assertBody().contains("<header>b01a b01b a01a a01b</header>");
	}
	@Test
	public void b02() throws Exception {
		b.get("/b02").accept("text/html").run().assertBody().contains("<header>b02a b02b</header>");
	}
	@Test
	public void b03() throws Exception {
		b.get("/b03").accept("text/html").run().assertBody().contains("<header>b01a b01b a01a a01b b03a b03b</header>");
	}
	@Test
	public void b04() throws Exception {
		b.get("/b04").accept("text/html").run().assertBody().contains("<header>b04a b01a b01b a01a a01b b04b</header>");
	}
	@Test
	public void b05() throws Exception {
		b.get("/b05").accept("text/html").run().assertBody().contains("<header>b05a b05b b01a b01b a01a a01b</header>");
	}
}