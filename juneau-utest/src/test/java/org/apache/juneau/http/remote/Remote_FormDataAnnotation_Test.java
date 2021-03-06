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
package org.apache.juneau.http.remote;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.runners.MethodSorters.*;
import static org.apache.juneau.assertions.Assertions.*;
import static org.apache.juneau.http.HttpParts.*;

import java.io.*;
import java.math.*;
import java.util.*;
import java.util.concurrent.atomic.*;

import org.apache.http.*;
import org.apache.juneau.collections.*;
import org.apache.juneau.http.annotation.*;
import org.apache.juneau.http.annotation.Header;
import org.apache.juneau.http.part.*;
import org.apache.juneau.jsonschema.annotation.*;
import org.apache.juneau.rest.RestRequest;
import org.apache.juneau.rest.annotation.*;
import org.apache.juneau.rest.client.*;
import org.apache.juneau.rest.mock.*;
import org.apache.juneau.testutils.*;
import org.apache.juneau.uon.*;
import org.apache.juneau.urlencoding.*;
import org.apache.juneau.utils.*;
import org.junit.*;

@FixMethodOrder(NAME_ASCENDING)
public class Remote_FormDataAnnotation_Test {

	public static class Bean {
		public int f;
		public static Bean create() {
			Bean b = new Bean();
			b.f = 1;
			return b;
		}
	}

	public static class Bean2 extends Bean {
		public static Bean2 create() {
			Bean2 b = new Bean2();
			b.f = 1;
			return b;
		}
		@Override
		public String toString() {
			return UrlEncodingSerializer.DEFAULT.toString(this);
		}
	}

	//-----------------------------------------------------------------------------------------------------------------
	// Basic tests
	//-----------------------------------------------------------------------------------------------------------------

	@Rest
	public static class A {
		@RestPost
		public String a(@FormData("*") OMap m, @Header("Content-Type") String ct) {
			assertEquals(ct,"application/x-www-form-urlencoded");
			return m.toString();
		}
	}

	@Remote
	public static interface A1 {
		@RemoteOp(path="a") String postX1(@FormData("x") int b);
		@RemoteOp(path="a") String postX2(@FormData("x") float b);
		@RemoteOp(path="a") String postX3(@FormData("x") Bean b);
		@RemoteOp(path="a") String postX4(@FormData("*") Bean b);
		@RemoteOp(path="a") String postX5(@FormData Bean b);
		@RemoteOp(path="a") String postX6(@FormData("x") Bean[] b);
		@RemoteOp(path="a") String postX7(@FormData(n="x",cf="uon") Bean[] b);
		@RemoteOp(path="a") String postX8(@FormData("x") List<Bean> b);
		@RemoteOp(path="a") String postX9(@FormData(n="x",cf="uon") List<Bean> b);
		@RemoteOp(path="a") String postX10(@FormData("x") Map<String,Bean> b);
		@RemoteOp(path="a") String postX11(@FormData("*") Map<String,Bean> b);
		@RemoteOp(path="a") String postX12(@FormData Map<String,Bean> b);
		@RemoteOp(path="a") String postX13(@FormData(n="x",f="uon") Map<String,Bean> b);
		@RemoteOp(path="a") String postX14(@FormData(f="uon") Map<String,Bean> b);
		@RemoteOp(path="a") String postX15(@FormData("*") Reader b);
		@RemoteOp(path="a") String postX16(@FormData Reader b);
		@RemoteOp(path="a") String postX17(@FormData("*") InputStream b);
		@RemoteOp(path="a") String postX18(@FormData InputStream b);
		@RemoteOp(path="a") String postX19(@FormData("*") PartList b);
		@RemoteOp(path="a") String postX20(@FormData PartList b);
		@RemoteOp(path="a") String postX21(@FormData NameValuePair b);
		@RemoteOp(path="a") String postX22(@FormData String b);
		@RemoteOp(path="a") String postX23(@FormData InputStream b);
		@RemoteOp(path="a") String postX24(@FormData Reader b);
		@RemoteOp(path="a") String postX25(@FormData Bean2 b);
		@RemoteOp(path="a") String postX26(@FormData List<NameValuePair> b);
	}

	@Test
	public void a01_objectTypes() throws Exception {
		A1 x = MockRestClient.build(A.class).getRemote(A1.class);
		assertEquals("{x:'1'}",x.postX1(1));
		assertEquals("{x:'1.0'}",x.postX2(1));
		assertEquals("{x:'f=1'}",x.postX3(Bean.create()));
		assertEquals("{f:'1'}",x.postX4(Bean.create()));
		assertEquals("{f:'1'}",x.postX5(Bean.create()));
		assertEquals("{x:'f=1,f=1'}",x.postX6(new Bean[]{Bean.create(),Bean.create()}));
		assertEquals("{x:'@((f=1),(f=1))'}",x.postX7(new Bean[]{Bean.create(),Bean.create()}));
		assertEquals("{x:'f=1,f=1'}",x.postX8(AList.of(Bean.create(),Bean.create())));
		assertEquals("{x:'@((f=1),(f=1))'}",x.postX9(AList.of(Bean.create(),Bean.create())));
		assertEquals("{x:'k1=f\\\\=1'}",x.postX10(AMap.of("k1",Bean.create())));
		assertEquals("{k1:'f=1'}",x.postX11(AMap.of("k1",Bean.create())));
		assertEquals("{k1:'f=1'}",x.postX12(AMap.of("k1",Bean.create())));
		assertEquals("{x:'k1=f\\\\=1'}",x.postX13(AMap.of("k1",Bean.create())));
		assertEquals("{k1:'f=1'}",x.postX14(AMap.of("k1",Bean.create())));
		assertEquals("{x:'1'}",x.postX15(new StringReader("x=1")));
		assertEquals("{x:'1'}",x.postX16(new StringReader("x=1")));
		assertEquals("{x:'1'}",x.postX17(new StringInputStream("x=1")));
		assertEquals("{x:'1'}",x.postX18(new StringInputStream("x=1")));
		assertEquals("{foo:'bar'}",x.postX19(parts("foo","bar")));
		assertEquals("{foo:'bar'}",x.postX20(parts("foo","bar")));
		assertEquals("{foo:'bar'}",x.postX21(part("foo","bar")));
		assertEquals("{foo:'bar'}",x.postX22("foo=bar"));
		assertEquals("{}",x.postX22(null));
		assertEquals("{foo:'bar'}",x.postX23(new ByteArrayInputStream("foo=bar".getBytes())));
		assertEquals("{foo:'bar'}",x.postX24(new StringReader("foo=bar")));
		assertEquals("{f:'1'}",x.postX25(Bean2.create()));
		assertEquals("{foo:'bar'}",x.postX26(AList.of(part("foo","bar"))));
	}

	//-----------------------------------------------------------------------------------------------------------------
	// @FormData(_default/allowEmptyValue)
	//-----------------------------------------------------------------------------------------------------------------

	@Rest
	public static class B {
		@RestOp
		public String post(@FormData("*") OMap m) {
			return m.toString();
		}
	}

	@Remote
	public static interface B1 {
		@RemoteOp(path="/") String postX1(@FormData(n="x",df="foo") String b);
		@RemoteOp(path="/") String postX2(@FormData(n="x",df="foo",aev=true) String b);
		@RemoteOp(path="/") String postX3(@FormData(n="x",df="") String b);
		@RemoteOp(path="/") String postX4(@FormData(n="x",df="",aev=true) String b);
	}

	@Test
	public void b01_default_allowEmptyValue() throws Exception {
		B1 x = remote(B.class,B1.class);
		assertEquals("{x:'foo'}",x.postX1(null));
		assertThrown(()->x.postX1("")).contains("Empty value not allowed");
		assertEquals("{x:'foo'}",x.postX2(null));
		assertEquals("{x:''}",x.postX2(""));
		assertEquals("{x:''}",x.postX3(null));
		assertThrown(()->x.postX3("")).contains("Empty value not allowed");
		assertEquals("{x:''}",x.postX4(null));
		assertEquals("{x:''}",x.postX4(""));
	}

	//-----------------------------------------------------------------------------------------------------------------
	// @FormData(collectionFormat)
	//-----------------------------------------------------------------------------------------------------------------

	@Rest
	public static class C {
		@RestPost
		public String a(@FormData("*") OMap m) {
			return m.toString();
		}
		@RestPost
		public Reader b(@Body Reader b) {
			return b;
		}
	}

	@Remote
	public static interface C1 {
		@RemoteOp(path="/a") String postX1(@FormData(n="x") String...b);
		@RemoteOp(path="/b") String postX2(@FormData(n="x") String...b);
		@RemoteOp(path="/a") String postX3(@FormData(n="x",cf="csv") String...b);
		@RemoteOp(path="/b") String postX4(@FormData(n="x",cf="csv") String...b);
		@RemoteOp(path="/a") String postX5(@FormData(n="x",cf="ssv") String...b);
		@RemoteOp(path="/b") String postX6(@FormData(n="x",cf="ssv") String...b);
		@RemoteOp(path="/a") String postX7(@FormData(n="x",cf="tsv") String...b);
		@RemoteOp(path="/b") String postX8(@FormData(n="x",cf="tsv") String...b);
		@RemoteOp(path="/a") String postX9(@FormData(n="x",cf="pipes") String...b);
		@RemoteOp(path="/b") String postX10(@FormData(n="x",cf="pipes") String...b);
		@RemoteOp(path="/a") String postX11(@FormData(n="x",cf="multi") String...b); // Not supported,but should be treated as csv.
		@RemoteOp(path="/b") String postX12(@FormData(n="x",cf="multi") String...b); // Not supported,but should be treated as csv.
		@RemoteOp(path="/a") String postX13(@FormData(n="x",cf="uon") String...b);
		@RemoteOp(path="/b") String postX14(@FormData(n="x",cf="uon") String...b);
	}

	@Test
	public void c01_collectionFormat() throws Exception {
		C1 x = remote(C.class,C1.class);
		assertEquals("{x:'foo,bar'}",x.postX1("foo","bar"));
		assertEquals("x=foo%2Cbar",x.postX2("foo","bar"));
		assertEquals("{x:'foo,bar'}",x.postX3("foo","bar"));
		assertEquals("x=foo%2Cbar",x.postX4("foo","bar"));
		assertEquals("{x:'foo bar'}",x.postX5("foo","bar"));
		assertEquals("x=foo+bar",x.postX6("foo","bar"));
		assertEquals("{x:'foo\\tbar'}",x.postX7("foo","bar"));
		assertEquals("x=foo%09bar",x.postX8("foo","bar"));
		assertEquals("{x:'foo|bar'}",x.postX9("foo","bar"));
		assertEquals("x=foo%7Cbar",x.postX10("foo","bar"));
		assertEquals("{x:'foo,bar'}",x.postX11("foo","bar"));
		assertEquals("x=foo%2Cbar",x.postX12("foo","bar"));
		assertEquals("{x:'@(foo,bar)'}",x.postX13("foo","bar"));
		assertEquals("x=%40%28foo%2Cbar%29",x.postX14("foo","bar"));
	}

	//-----------------------------------------------------------------------------------------------------------------
	// @FormData(maximum,exclusiveMaximum,minimum,exclusiveMinimum)
	//-----------------------------------------------------------------------------------------------------------------

	@Rest
	public static class D {
		@RestOp
		public String post(@FormData("*") OMap m) {
			return m.toString();
		}
	}

	@Remote
	public static interface D1 {
		@RemoteOp(path="/") String postX1(@FormData(n="x",min="1",max="10") int b);
		@RemoteOp(path="/") String postX2(@FormData(n="x",min="1",max="10",emin=false,emax=false) int b);
		@RemoteOp(path="/") String postX3(@FormData(n="x",min="1",max="10",emin=true,emax=true) int b);
		@RemoteOp(path="/") String postX4(@FormData(n="x",min="1",max="10",emin=true,emax=true) int b);
		@RemoteOp(path="/") String postX5(@FormData(n="x",min="1",max="10") short b);
		@RemoteOp(path="/") String postX6(@FormData(n="x",min="1",max="10",emin=false,emax=false) short b);
		@RemoteOp(path="/") String postX7(@FormData(n="x",min="1",max="10",emin=true,emax=true) short b);
		@RemoteOp(path="/") String postX8(@FormData(n="x",min="1",max="10") long b);
		@RemoteOp(path="/") String postX9(@FormData(n="x",min="1",max="10",emin=false,emax=false) long b);
		@RemoteOp(path="/") String postX10(@FormData(n="x",min="1",max="10",emin=true,emax=true) long b);
		@RemoteOp(path="/") String postX11(@FormData(n="x",min="1",max="10") float b);
		@RemoteOp(path="/") String postX12(@FormData(n="x",min="1",max="10",emin=false,emax=false) float b);
		@RemoteOp(path="/") String postX13(@FormData(n="x",min="1",max="10",emin=true,emax=true) float b);
		@RemoteOp(path="/") String postX14(@FormData(n="x",min="1",max="10") double b);
		@RemoteOp(path="/") String postX15(@FormData(n="x",min="1",max="10",emin=false,emax=false) double b);
		@RemoteOp(path="/") String postX16(@FormData(n="x",min="1",max="10",emin=true,emax=true) double b);
		@RemoteOp(path="/") String postX17(@FormData(n="x",min="1",max="10") byte b);
		@RemoteOp(path="/") String postX18(@FormData(n="x",min="1",max="10",emin=false,emax=false) byte b);
		@RemoteOp(path="/") String postX19(@FormData(n="x",min="1",max="10",emin=true,emax=true) byte b);
		@RemoteOp(path="/") String postX20(@FormData(n="x",min="1",max="10") AtomicInteger b);
		@RemoteOp(path="/") String postX21(@FormData(n="x",min="1",max="10",emin=false,emax=false) AtomicInteger b);
		@RemoteOp(path="/") String postX22(@FormData(n="x",min="1",max="10",emin=true,emax=true) AtomicInteger b);
		@RemoteOp(path="/") String postX23(@FormData(n="x",min="1",max="10") BigDecimal b);
		@RemoteOp(path="/") String postX24(@FormData(n="x",min="1",max="10",emin=false,emax=false) BigDecimal b);
		@RemoteOp(path="/") String postX25(@FormData(n="x",min="1",max="10",emin=true,emax=true) BigDecimal b);
		@RemoteOp(path="/") String postX26(@FormData(n="x",min="1",max="10") Integer b);
		@RemoteOp(path="/") String postX27(@FormData(n="x",min="1",max="10",emin=false,emax=false) Integer b);
		@RemoteOp(path="/") String postX28(@FormData(n="x",min="1",max="10",emin=true,emax=true) Integer b);
		@RemoteOp(path="/") String postX29(@FormData(n="x",min="1",max="10") Short b);
		@RemoteOp(path="/") String postX30(@FormData(n="x",min="1",max="10",emin=false,emax=false) Short b);
		@RemoteOp(path="/") String postX31(@FormData(n="x",min="1",max="10",emin=true,emax=true) Short b);
		@RemoteOp(path="/") String postX32(@FormData(n="x",min="1",max="10") Long b);
		@RemoteOp(path="/") String postX33(@FormData(n="x",min="1",max="10",emin=false,emax=false) Long b);
		@RemoteOp(path="/") String postX34(@FormData(n="x",min="1",max="10",emin=true,emax=true) Long b);
		@RemoteOp(path="/") String postX35(@FormData(n="x",min="1",max="10") Float b);
		@RemoteOp(path="/") String postX36(@FormData(n="x",min="1",max="10",emin=false,emax=false) Float b);
		@RemoteOp(path="/") String postX37(@FormData(n="x",min="1",max="10",emin=true,emax=true) Float b);
		@RemoteOp(path="/") String postX38(@FormData(n="x",min="1",max="10") Double b);
		@RemoteOp(path="/") String postX39(@FormData(n="x",min="1",max="10",emin=false,emax=false) Double b);
		@RemoteOp(path="/") String postX40(@FormData(n="x",min="1",max="10",emin=true,emax=true) Double b);
		@RemoteOp(path="/") String postX41(@FormData(n="x",min="1",max="10") Byte b);
		@RemoteOp(path="/") String postX42(@FormData(n="x",min="1",max="10",emin=false,emax=false) Byte b);
		@RemoteOp(path="/") String postX43(@FormData(n="x",min="1",max="10",emin=true,emax=true) Byte b);
	}

	@Test
	public void d01_min_max_emin_emax() throws Exception {
		D1 x = remote(D.class,D1.class);
		assertEquals("{x:'1'}",x.postX1(1));
		assertEquals("{x:'10'}",x.postX1(10));
		assertThrown(()->x.postX1(0)).contains("Minimum value not met");
		assertThrown(()->x.postX1(11)).contains("Maximum value exceeded");
		assertEquals("{x:'1'}",x.postX2(1));
		assertEquals("{x:'10'}",x.postX2(10));
		assertThrown(()->x.postX2(0)).contains("Minimum value not met");
		assertThrown(()->x.postX2(11)).contains("Maximum value exceeded");
		assertEquals("{x:'2'}",x.postX3(2));
		assertEquals("{x:'9'}",x.postX3(9));
		assertThrown(()->x.postX3(1)).contains("Minimum value not met");
		assertThrown(()->x.postX3(10)).contains("Maximum value exceeded");
		assertEquals("{x:'1'}",x.postX5((short)1));
		assertEquals("{x:'10'}",x.postX5((short)10));
		assertThrown(()->x.postX5((short)0)).contains("Minimum value not met");
		assertThrown(()->x.postX5((short)11)).contains("Maximum value exceeded");
		assertEquals("{x:'1'}",x.postX6((short)1));
		assertEquals("{x:'10'}",x.postX6((short)10));
		assertThrown(()->x.postX6((short)0)).contains("Minimum value not met");
		assertThrown(()->x.postX6((short)11)).contains("Maximum value exceeded");
		assertEquals("{x:'2'}",x.postX7((short)2));
		assertEquals("{x:'9'}",x.postX7((short)9));
		assertThrown(()->x.postX7((short)1)).contains("Minimum value not met");
		assertThrown(()->x.postX7((short)10)).contains("Maximum value exceeded");
		assertEquals("{x:'1'}",x.postX8(1l));
		assertEquals("{x:'10'}",x.postX8(10l));
		assertThrown(()->x.postX8(0l)).contains("Minimum value not met");
		assertThrown(()->x.postX8(11l)).contains("Maximum value exceeded");
		assertEquals("{x:'1'}",x.postX9(1l));
		assertEquals("{x:'10'}",x.postX9(10l));
		assertThrown(()->x.postX9(0l)).contains("Minimum value not met");
		assertThrown(()->x.postX9(11l)).contains("Maximum value exceeded");
		assertEquals("{x:'2'}",x.postX10(2l));
		assertEquals("{x:'9'}",x.postX10(9l));
		assertThrown(()->x.postX10(1l)).contains("Minimum value not met");
		assertThrown(()->x.postX10(10l)).contains("Maximum value exceeded");
		assertEquals("{x:'1.0'}",x.postX11(1f));
		assertEquals("{x:'10.0'}",x.postX11(10f));
		assertThrown(()->x.postX11(0.9f)).contains("Minimum value not met");
		assertThrown(()->x.postX11(10.1f)).contains("Maximum value exceeded");
		assertEquals("{x:'1.0'}",x.postX12(1f));
		assertEquals("{x:'10.0'}",x.postX12(10f));
		assertThrown(()->x.postX12(0.9f)).contains("Minimum value not met");
		assertThrown(()->x.postX12(10.1f)).contains("Maximum value exceeded");
		assertEquals("{x:'1.1'}",x.postX13(1.1f));
		assertEquals("{x:'9.9'}",x.postX13(9.9f));
		assertThrown(()->x.postX13(1f)).contains("Minimum value not met");
		assertThrown(()->x.postX13(10f)).contains("Maximum value exceeded");
		assertEquals("{x:'1.0'}",x.postX14(1d));
		assertEquals("{x:'10.0'}",x.postX14(10d));
		assertThrown(()->x.postX14(0.9d)).contains("Minimum value not met");
		assertThrown(()->x.postX14(10.1d)).contains("Maximum value exceeded");
		assertEquals("{x:'1.0'}",x.postX15(1d));
		assertEquals("{x:'10.0'}",x.postX15(10d));
		assertThrown(()->x.postX15(0.9d)).contains("Minimum value not met");
		assertThrown(()->x.postX15(10.1d)).contains("Maximum value exceeded");
		assertEquals("{x:'1.1'}",x.postX16(1.1d));
		assertEquals("{x:'9.9'}",x.postX16(9.9d));
		assertThrown(()->x.postX16(1d)).contains("Minimum value not met");
		assertThrown(()->x.postX16(10d)).contains("Maximum value exceeded");
		assertEquals("{x:'1'}",x.postX17((byte)1));
		assertEquals("{x:'10'}",x.postX17((byte)10));
		assertThrown(()->x.postX17((byte)0)).contains("Minimum value not met");
		assertThrown(()->x.postX17((byte)11)).contains("Maximum value exceeded");
		assertEquals("{x:'1'}",x.postX18((byte)1));
		assertEquals("{x:'10'}",x.postX18((byte)10));
		assertThrown(()->x.postX18((byte)0)).contains("Minimum value not met");
		assertThrown(()->x.postX18((byte)11)).contains("Maximum value exceeded");
		assertEquals("{x:'2'}",x.postX19((byte)2));
		assertEquals("{x:'9'}",x.postX19((byte)9));
		assertThrown(()->x.postX19((byte)1)).contains("Minimum value not met");
		assertThrown(()->x.postX19((byte)10)).contains("Maximum value exceeded");
		assertEquals("{x:'1'}",x.postX20(new AtomicInteger(1)));
		assertEquals("{x:'10'}",x.postX20(new AtomicInteger(10)));
		assertThrown(()->x.postX20(new AtomicInteger(0))).contains("Minimum value not met");
		assertThrown(()->x.postX20(new AtomicInteger(11))).contains("Maximum value exceeded");
		assertEquals("{x:'1'}",x.postX21(new AtomicInteger(1)));
		assertEquals("{x:'10'}",x.postX21(new AtomicInteger(10)));
		assertThrown(()->x.postX21(new AtomicInteger(0))).contains("Minimum value not met");
		assertThrown(()->x.postX21(new AtomicInteger(11))).contains("Maximum value exceeded");
		assertEquals("{x:'2'}",x.postX22(new AtomicInteger(2)));
		assertEquals("{x:'9'}",x.postX22(new AtomicInteger(9)));
		assertThrown(()->x.postX22(new AtomicInteger(1))).contains("Minimum value not met");
		assertThrown(()->x.postX22(new AtomicInteger(10))).contains("Maximum value exceeded");
		assertEquals("{x:'1'}",x.postX23(new BigDecimal(1)));
		assertEquals("{x:'10'}",x.postX23(new BigDecimal(10)));
		assertThrown(()->x.postX23(new BigDecimal(0))).contains("Minimum value not met");
		assertThrown(()->x.postX23(new BigDecimal(11))).contains("Maximum value exceeded");
		assertEquals("{x:'1'}",x.postX24(new BigDecimal(1)));
		assertEquals("{x:'10'}",x.postX24(new BigDecimal(10)));
		assertThrown(()->x.postX24(new BigDecimal(0))).contains("Minimum value not met");
		assertThrown(()->x.postX24(new BigDecimal(11))).contains("Maximum value exceeded");
		assertEquals("{x:'2'}",x.postX25(new BigDecimal(2)));
		assertEquals("{x:'9'}",x.postX25(new BigDecimal(9)));
		assertThrown(()->x.postX25(new BigDecimal(1))).contains("Minimum value not met");
		assertThrown(()->x.postX25(new BigDecimal(10))).contains("Maximum value exceeded");
		assertEquals("{x:'1'}",x.postX26(1));
		assertEquals("{x:'10'}",x.postX26(10));
		assertThrown(()->x.postX26(0)).contains("Minimum value not met");
		assertThrown(()->x.postX26(11)).contains("Maximum value exceeded");
		assertEquals("{}",x.postX26(null));
		assertEquals("{x:'1'}",x.postX27(1));
		assertEquals("{x:'10'}",x.postX27(10));
		assertThrown(()->x.postX27(0)).contains("Minimum value not met");
		assertThrown(()->x.postX27(11)).contains("Maximum value exceeded");
		assertEquals("{}",x.postX27(null));
		assertEquals("{x:'2'}",x.postX28(2));
		assertEquals("{x:'9'}",x.postX28(9));
		assertThrown(()->x.postX28(1)).contains("Minimum value not met");
		assertThrown(()->x.postX28(10)).contains("Maximum value exceeded");
		assertEquals("{}",x.postX28(null));
		assertEquals("{x:'1'}",x.postX29((short)1));
		assertEquals("{x:'10'}",x.postX29((short)10));
		assertThrown(()->x.postX29((short)0)).contains("Minimum value not met");
		assertThrown(()->x.postX29((short)11)).contains("Maximum value exceeded");
		assertEquals("{}",x.postX29(null));
		assertEquals("{x:'1'}",x.postX30((short)1));
		assertEquals("{x:'10'}",x.postX30((short)10));
		assertThrown(()->x.postX30((short)0)).contains("Minimum value not met");
		assertThrown(()->x.postX30((short)11)).contains("Maximum value exceeded");
		assertEquals("{}",x.postX30(null));
		assertEquals("{x:'2'}",x.postX31((short)2));
		assertEquals("{x:'9'}",x.postX31((short)9));
		assertThrown(()->x.postX31((short)1)).contains("Minimum value not met");
		assertThrown(()->x.postX31((short)10)).contains("Maximum value exceeded");
		assertEquals("{}",x.postX31(null));
		assertEquals("{x:'1'}",x.postX32(1l));
		assertEquals("{x:'10'}",x.postX32(10l));
		assertThrown(()->x.postX32(0l)).contains("Minimum value not met");
		assertThrown(()->x.postX32(11l)).contains("Maximum value exceeded");
		assertEquals("{}",x.postX32(null));
		assertEquals("{x:'1'}",x.postX33(1l));
		assertEquals("{x:'10'}",x.postX33(10l));
		assertThrown(()->x.postX33(0l)).contains("Minimum value not met");
		assertThrown(()->x.postX33(11l)).contains("Maximum value exceeded");
		assertEquals("{}",x.postX33(null));
		assertEquals("{x:'2'}",x.postX34(2l));
		assertEquals("{x:'9'}",x.postX34(9l));
		assertThrown(()->x.postX34(1l)).contains("Minimum value not met");
		assertThrown(()->x.postX34(10l)).contains("Maximum value exceeded");
		assertEquals("{}",x.postX34(null));
		assertEquals("{x:'1.0'}",x.postX35(1f));
		assertEquals("{x:'10.0'}",x.postX35(10f));
		assertThrown(()->x.postX35(0.9f)).contains("Minimum value not met");
		assertThrown(()->x.postX35(10.1f)).contains("Maximum value exceeded");
		assertEquals("{}",x.postX35(null));
		assertEquals("{x:'1.0'}",x.postX36(1f));
		assertEquals("{x:'10.0'}",x.postX36(10f));
		assertThrown(()->x.postX36(0.9f)).contains("Minimum value not met");
		assertThrown(()->x.postX36(10.1f)).contains("Maximum value exceeded");
		assertEquals("{}",x.postX36(null));
		assertEquals("{x:'1.1'}",x.postX37(1.1f));
		assertEquals("{x:'9.9'}",x.postX37(9.9f));
		assertThrown(()->x.postX37(1f)).contains("Minimum value not met");
		assertThrown(()->x.postX37(10f)).contains("Maximum value exceeded");
		assertEquals("{}",x.postX37(null));
		assertEquals("{x:'1.0'}",x.postX38(1d));
		assertEquals("{x:'10.0'}",x.postX38(10d));
		assertThrown(()->x.postX38(0.9d)).contains("Minimum value not met");
		assertThrown(()->x.postX38(10.1d)).contains("Maximum value exceeded");
		assertEquals("{}",x.postX38(null));
		assertEquals("{x:'1.0'}",x.postX39(1d));
		assertEquals("{x:'10.0'}",x.postX39(10d));
		assertThrown(()->x.postX39(0.9d)).contains("Minimum value not met");
		assertThrown(()->x.postX39(10.1d)).contains("Maximum value exceeded");
		assertEquals("{}",x.postX39(null));
		assertEquals("{x:'1.1'}",x.postX40(1.1d));
		assertEquals("{x:'9.9'}",x.postX40(9.9d));
		assertThrown(()->x.postX40(1d)).contains("Minimum value not met");
		assertThrown(()->x.postX40(10d)).contains("Maximum value exceeded");
		assertEquals("{}",x.postX40(null));
		assertEquals("{x:'1'}",x.postX41((byte)1));
		assertEquals("{x:'10'}",x.postX41((byte)10));
		assertThrown(()->x.postX41((byte)0)).contains("Minimum value not met");
		assertThrown(()->x.postX41((byte)11)).contains("Maximum value exceeded");
		assertEquals("{}",x.postX41(null));
		assertEquals("{x:'1'}",x.postX42((byte)1));
		assertEquals("{x:'10'}",x.postX42((byte)10));
		assertThrown(()->x.postX42((byte)0)).contains("Minimum value not met");
		assertThrown(()->x.postX42((byte)11)).contains("Maximum value exceeded");
		assertEquals("{}",x.postX42(null));
		assertEquals("{x:'2'}",x.postX43((byte)2));
		assertEquals("{x:'9'}",x.postX43((byte)9));
		assertThrown(()->x.postX43((byte)1)).contains("Minimum value not met");
		assertThrown(()->x.postX43((byte)10)).contains("Maximum value exceeded");
		assertEquals("{}",x.postX43(null));
	}

	//-----------------------------------------------------------------------------------------------------------------
	// @FormData(maxItems,minItems,uniqueItems)
	//-----------------------------------------------------------------------------------------------------------------

	@Rest
	public static class E {
		@RestOp
		public String post(@FormData("*") OMap m) {
			return m.toString();
		}
	}

	@Remote
	public static interface E1 {
		@RemoteOp(path="/") String postX1(@FormData(n="x",cf="pipes",mini=1,maxi=2) String...b);
		@RemoteOp(path="/") String postX2(@FormData(n="x",items=@Items(cf="pipes",mini=1,maxi=2)) String[]...b);
		@RemoteOp(path="/") String postX3(@FormData(n="x",cf="pipes",ui=false) String...b);
		@RemoteOp(path="/") String postX4(@FormData(n="x",items=@Items(cf="pipes",ui=false)) String[]...b);
		@RemoteOp(path="/") String postX5(@FormData(n="x",cf="pipes",ui=true) String...b);
		@RemoteOp(path="/") String postX6(@FormData(n="x",items=@Items(cf="pipes",ui=true)) String[]...b);
	}

	@Test
	public void e01_mini_maxi_ui() throws Exception {
		E1 x = remote(E.class,E1.class);
		assertEquals("{x:'1'}",x.postX1("1"));
		assertEquals("{x:'1|2'}",x.postX1("1","2"));
		assertThrown(()->x.postX1()).contains("Minimum number of items not met");
		assertThrown(()->x.postX1("1","2","3")).contains("Maximum number of items exceeded");
		assertEquals("{x:null}",x.postX1((String)null));
		assertEquals("{x:'1'}",x.postX2(new String[]{"1"}));
		assertEquals("{x:'1|2'}",x.postX2(new String[]{"1","2"}));
		assertThrown(()->x.postX2(new String[]{})).contains("Minimum number of items not met");
		assertThrown(()->x.postX2(new String[]{"1","2","3"})).contains("Maximum number of items exceeded");
		assertEquals("{x:null}",x.postX2(new String[]{null}));
		assertEquals("{x:'1|1'}",x.postX3("1","1"));
		assertEquals("{x:'1|1'}",x.postX4(new String[]{"1","1"}));
		assertEquals("{x:'1|2'}",x.postX5("1","2"));
		assertThrown(()->x.postX5("1","1")).contains("Duplicate items not allowed");
		assertEquals("{x:'1|2'}",x.postX6(new String[]{"1","2"}));
		assertThrown(()->x.postX6(new String[]{"1","1"})).contains("Duplicate items not allowed");
	}

	//-----------------------------------------------------------------------------------------------------------------
	// @FormData(maxLength,minLength,enum,pattern)
	//-----------------------------------------------------------------------------------------------------------------

	@Rest
	public static class F {
		@RestOp
		public String post(@FormData("*") OMap m) {
			return m.toString();
		}
	}

	@Remote
	public static interface F1 {
		@RemoteOp(path="/") String postX1(@FormData(n="x",minl=2,maxl=3) String b);
		@RemoteOp(path="/") String postX2(@FormData(n="x",cf="pipes",items=@Items(minl=2,maxl=3)) String...b);
		@RemoteOp(path="/") String postX3(@FormData(n="x",e={"foo"}) String b);
		@RemoteOp(path="/") String postX4(@FormData(n="x",cf="pipes",items=@Items(e={"foo"})) String...b);
		@RemoteOp(path="/") String postX5(@FormData(n="x",p="foo\\d{1,3}") String b);
		@RemoteOp(path="/") String postX6(@FormData(n="x",cf="pipes",items=@Items(p="foo\\d{1,3}")) String...b);
	}

	@Test
	public void f01_minl_maxl_enum_pattern() throws Exception {
		F1 x = remote(F.class,F1.class);
		assertEquals("{x:'12'}",x.postX1("12"));
		assertEquals("{x:'123'}",x.postX1("123"));
		assertThrown(()->x.postX1("1")).contains("Minimum length of value not met");
		assertThrown(()->x.postX1("1234")).contains("Maximum length of value exceeded");
		assertEquals("{}",x.postX1(null));
		assertEquals("{x:'12|34'}",x.postX2("12","34"));
		assertEquals("{x:'123|456'}",x.postX2("123","456"));
		assertThrown(()->x.postX2("1","2")).contains("Minimum length of value not met");
		assertThrown(()->x.postX2("1234","5678")).contains("Maximum length of value exceeded");
		assertEquals("{x:'12|null'}",x.postX2("12",null));
		assertEquals("{x:'foo'}",x.postX3("foo"));
		assertThrown(()->x.postX3("bar")).contains("Value does not match one of the expected values");
		assertEquals("{}",x.postX3(null));
		assertEquals("{x:'foo'}",x.postX4("foo"));
		assertThrown(()->x.postX4("bar")).contains("Value does not match one of the expected values");
		assertEquals("{x:null}",x.postX4((String)null));
		assertEquals("{x:'foo123'}",x.postX5("foo123"));
		assertThrown(()->x.postX5("bar")).contains("Value does not match expected pattern");
		assertEquals("{}",x.postX5(null));
		assertEquals("{x:'foo123'}",x.postX6("foo123"));
		assertThrown(()->x.postX6("foo")).contains("Value does not match expected pattern");
		assertEquals("{x:null}",x.postX6((String)null));
	}

	//-----------------------------------------------------------------------------------------------------------------
	// @FormData(multipleOf)
	//-----------------------------------------------------------------------------------------------------------------

	@Rest
	public static class G {
		@RestOp
		public String post(@FormData("*") OMap m) {
			return m.toString();
		}
	}

	@Remote
	public static interface G1 {
		@RemoteOp(path="/") String postX1(@FormData(n="x",mo="2") int b);
		@RemoteOp(path="/") String postX2(@FormData(n="x",mo="2") short b);
		@RemoteOp(path="/") String postX3(@FormData(n="x",mo="2") long b);
		@RemoteOp(path="/") String postX4(@FormData(n="x",mo="2") float b);
		@RemoteOp(path="/") String postX5(@FormData(n="x",mo="2") double b);
		@RemoteOp(path="/") String postX6(@FormData(n="x",mo="2") byte b);
		@RemoteOp(path="/") String postX7(@FormData(n="x",mo="2") AtomicInteger b);
		@RemoteOp(path="/") String postX8(@FormData(n="x",mo="2") BigDecimal b);
		@RemoteOp(path="/") String postX9(@FormData(n="x",mo="2") Integer b);
		@RemoteOp(path="/") String postX10(@FormData(n="x",mo="2") Short b);
		@RemoteOp(path="/") String postX11(@FormData(n="x",mo="2") Long b);
		@RemoteOp(path="/") String postX12(@FormData(n="x",mo="2") Float b);
		@RemoteOp(path="/") String postX13(@FormData(n="x",mo="2") Double b);
		@RemoteOp(path="/") String postX14(@FormData(n="x",mo="2") Byte b);
	}

	@Test
	public void g01_multipleOf() throws Exception {
		G1 x = remote(G.class,G1.class);
		assertEquals("{x:'4'}",x.postX1(4));
		assertThrown(()->x.postX1(5)).contains("Multiple-of not met");
		assertEquals("{x:'4'}",x.postX2((short)4));
		assertThrown(()->x.postX2((short)5)).contains("Multiple-of not met");
		assertEquals("{x:'4'}",x.postX3(4));
		assertThrown(()->x.postX3(5)).contains("Multiple-of not met");
		assertEquals("{x:'4.0'}",x.postX4(4));
		assertThrown(()->x.postX4(5)).contains("Multiple-of not met");
		assertEquals("{x:'4.0'}",x.postX5(4));
		assertThrown(()->x.postX5(5)).contains("Multiple-of not met");
		assertEquals("{x:'4'}",x.postX6((byte)4));
		assertThrown(()->x.postX6((byte)5)).contains("Multiple-of not met");
		assertEquals("{x:'4'}",x.postX7(new AtomicInteger(4)));
		assertThrown(()->x.postX7(new AtomicInteger(5))).contains("Multiple-of not met");
		assertEquals("{x:'4'}",x.postX8(new BigDecimal(4)));
		assertThrown(()->x.postX8(new BigDecimal(5))).contains("Multiple-of not met");
		assertEquals("{x:'4'}",x.postX9(4));
		assertThrown(()->x.postX9(5)).contains("Multiple-of not met");
		assertEquals("{x:'4'}",x.postX10((short)4));
		assertThrown(()->x.postX10((short)5)).contains("Multiple-of not met");
		assertEquals("{x:'4'}",x.postX11(4l));
		assertThrown(()->x.postX11(5l)).contains("Multiple-of not met");
		assertEquals("{x:'4.0'}",x.postX12(4f));
		assertThrown(()->x.postX12(5f)).contains("Multiple-of not met");
		assertEquals("{x:'4.0'}",x.postX13(4d));
		assertThrown(()->x.postX13(5d)).contains("Multiple-of not met");
		assertEquals("{x:'4'}",x.postX14((byte)4));
		assertThrown(()->x.postX14((byte)5)).contains("Multiple-of not met");
	}

	//-----------------------------------------------------------------------------------------------------------------
	// @FormData(required)
	//-----------------------------------------------------------------------------------------------------------------

	@Rest
	public static class H {
		@RestOp
		public String post(@FormData("*") OMap m) {
			return m.toString();
		}
	}

	@Remote
	public static interface H1 {
		@RemoteOp(path="/") String postX1(@FormData(n="x") String b);
		@RemoteOp(path="/") String postX2(@FormData(n="x",r=false) String b);
		@RemoteOp(path="/") String postX3(@FormData(n="x",r=true) String b);
	}

	@Test
	public void h01_required() throws Exception {
		H1 x = remote(H.class,H1.class);
		assertEquals("{}",x.postX1(null));
		assertEquals("{}",x.postX2(null));
		assertEquals("{x:'1'}",x.postX3("1"));
		assertThrown(()->x.postX3(null)).contains("Required value not provided.");
	}

	//-----------------------------------------------------------------------------------------------------------------
	// @FormData(skipIfEmpty)
	//-----------------------------------------------------------------------------------------------------------------

	@Rest
	public static class I {
		@RestOp
		public String post(@FormData("*") OMap m) {
			return m.toString();
		}
	}

	@Remote
	public static interface I1 {
		@RemoteOp(path="/") String postX1(@FormData(n="x",aev=true) String b);
		@RemoteOp(path="/") String postX2(@FormData(n="x",aev=true,sie=false) String b);
		@RemoteOp(path="/") String postX3(@FormData(n="x",sie=true) String b);
	}

	@Test
	public void i01_skipIfEmpty() throws Exception {
		I1 x = remote(I.class,I1.class);
		assertEquals("{x:''}",x.postX1(""));
		assertEquals("{x:''}",x.postX2(""));
		assertEquals("{}",x.postX3(""));
	}

	//-----------------------------------------------------------------------------------------------------------------
	// @FormData(serializer)
	//-----------------------------------------------------------------------------------------------------------------

	@Rest
	public static class J {
		@RestOp
		public String post(@FormData("*") OMap m) {
			return m.toString();
		}
	}

	@Remote
	public static interface J1 {
		@RemoteOp(path="/") String postX1(@FormData(n="x",serializer=XPartSerializer.class) String b);
	}

	@Test
	public void j01_serializer() throws Exception {
		J1 x = remote(J.class,J1.class);
		assertEquals("{x:'xXx'}",x.postX1("X"));
	}

	//-----------------------------------------------------------------------------------------------------------------
	// RequestBean @FormData
	//-----------------------------------------------------------------------------------------------------------------

	@Rest(parsers=UrlEncodingParser.class)
	public static class K {
		@RestOp
		public String post(RestRequest req) throws Exception {
			return req.getFormParams().toString(true);
		}
	}

	//-----------------------------------------------------------------------------------------------------------------
	// RequestBean @FormData, Simple values
	//-----------------------------------------------------------------------------------------------------------------

	@Remote(path="/")
	public static interface K1 {
		@RemoteOp(path="/") String postX1(@Request K1a rb);
		@RemoteOp(path="/") String postX2(@Request(serializer=XSerializer.class) K1a rb);
	}

	public static class K1a {
		@FormData
		public String getA() {
			return "a1";
		}
		@FormData("b")
		public String getX1() {
			return "b1";
		}
		@FormData(n="c")
		public String getX2() {
			return "c1";
		}
		@FormData(n="e",aev=true)
		public String getX4() {
			return "";
		}
		@FormData("f")
		public String getX5() {
			return null;
		}
		@FormData("g")
		public String getX6() {
			return "true";
		}
		@FormData("h")
		public String getX7() {
			return "123";
		}
		@FormData(n="i1",sie=true)
		public String getX8() {
			return "foo";
		}
		@FormData(n="i2",sie=true)
		public String getX9() {
			return "";
		}
		@FormData(n="i3",sie=true)
		public String getX10() {
			return null;
		}
	}

	@Test
	public void k01_requestBean_simpleVals() throws Exception {
		K1 x1 = remote(K.class,K1.class);
		K1 x2 = client(K.class).partSerializer(UonSerializer.class).build().getRemote(K1.class);
		assertEquals("{a:'a1',b:'b1',c:'c1',e:'',g:'true',h:'123',i1:'foo'}",x1.postX1(new K1a()));
		assertEquals("{a:'a1',b:'b1',c:'c1',e:'',g:'\\'true\\'',h:'\\'123\\'',i1:'foo'}",x2.postX1(new K1a()));
		assertEquals("{a:'xa1x',b:'xb1x',c:'xc1x',e:'xx',g:'xtruex',h:'x123x',i1:'xfoox'}",x2.postX2(new K1a()));
	}

	//-----------------------------------------------------------------------------------------------------------------
	// RequestBean @FormData, Maps
	//-----------------------------------------------------------------------------------------------------------------

	@Remote(path="/")
	public static interface K2 {
		@RemoteOp(path="/") String postX1(@Request K2a rb);
		@RemoteOp(path="/") String postX2(@Request(serializer=XSerializer.class) K2a rb);
	}

	public static class K2a {
		@FormData
		public Map<String,Object> getA() {
			return AMap.of("a1","v1","a2",123,"a3",null,"a4","");
		}
		@FormData("*")
		public Map<String,Object> getB() {
			return AMap.of("b1","true","b2","123","b3","null");
		}
		@FormData(n="*",aev=true)
		public Map<String,Object> getC() {
			return AMap.of("c1","v1","c2",123,"c3",null,"c4","");
		}
		@FormData("*")
		public Map<String,Object> getD() {
			return null;
		}
	}

	@Test
	public void k02_requestBean_maps() throws Exception {
		K2 x1 = remote(K.class,K2.class);
		K2 x2 = client(K.class).partSerializer(UonSerializer.class).build().getRemote(K2.class);
		assertEquals("{a:'a1=v1,a2=123,a3=null,a4=',b1:'true',b2:'123',b3:'null',c1:'v1',c2:'123',c4:''}",x1.postX1(new K2a()));
		assertEquals("{a:'(a1=v1,a2=123,a3=null,a4=\\'\\')',b1:'\\'true\\'',b2:'\\'123\\'',b3:'\\'null\\'',c1:'v1',c2:'123',c4:''}",x2.postX1(new K2a()));
		assertEquals("{a:'x{a1:\\'v1\\',a2:123,a3:null,a4:\\'\\'}x',b1:'xtruex',b2:'x123x',b3:'xnullx',c1:'xv1x',c2:'x123x',c4:'xx'}",x2.postX2(new K2a()));
	}

	//-----------------------------------------------------------------------------------------------------------------
	// RequestBean @FormData, NameValuePairs
	//-----------------------------------------------------------------------------------------------------------------

	@Remote(path="/")
	public static interface K3 {
		@RemoteOp(path="/") String postX1(@Request K3a rb);
		@RemoteOp(path="/") String postX2(@Request(serializer=XSerializer.class) K3a rb);
	}

	public static class K3a {
		@FormData
		public PartList getA() {
			return parts("a1","v1","a2",123,"a3",null,"a4","");
		}
		@FormData("*")
		public PartList getB() {
			return parts("b1","true","b2","123","b3","null");
		}
		@FormData(n="*")
		public PartList getC() {
			return parts("c1","v1","c2",123,"c3",null,"c4","");
		}
		@FormData("*")
		public PartList getD() {
			return null;
		}
		@FormData
		public NameValuePair[] getE() {
			return parts("e1","v1","e2",123,"e3",null,"e4","").getAll();
		}
	}

	@Test
	public void k03_requestBean_nameValuePairs() throws Exception {
		K3 x1 = remote(K.class,K3.class);
		K3 x2 = client(K.class).partSerializer(UonSerializer.class).build().getRemote(K3.class);
		assertEquals("{a1:'v1',a2:'123',a4:'',b1:'true',b2:'123',b3:'null',c1:'v1',c2:'123',c4:'',e1:'v1',e2:'123',e4:''}",x1.postX1(new K3a()));
		assertEquals("{a1:'v1',a2:'123',a4:'',b1:'true',b2:'123',b3:'null',c1:'v1',c2:'123',c4:'',e1:'v1',e2:'123',e4:''}",x2.postX1(new K3a()));
		assertEquals("{a1:'v1',a2:'123',a4:'',b1:'true',b2:'123',b3:'null',c1:'v1',c2:'123',c4:'',e1:'v1',e2:'123',e4:''}",x2.postX2(new K3a()));
	}

	//-----------------------------------------------------------------------------------------------------------------
	// RequestBean @FormData,CharSequence
	//-----------------------------------------------------------------------------------------------------------------

	@Remote(path="/")
	public static interface K4 {
		String post(@Request C04_Bean rb);
	}

	public static class C04_Bean {
		@FormData("*")
		public StringBuilder getA() {
			return new StringBuilder("foo=bar&baz=qux");
		}
	}

	@Test
	public void k04_requestBean_charSequence() throws Exception {
		K4 x = remote(K.class,K4.class);
		assertEquals("{baz:'qux',foo:'bar'}",x.post(new C04_Bean()));
	}

	//-----------------------------------------------------------------------------------------------------------------
	// RequestBean @FormData, Reader
	//-----------------------------------------------------------------------------------------------------------------

	@Remote(path="/")
	public static interface K5 {
		String post(@Request K5a rb);
	}

	public static class K5a {
		@FormData("*")
		public Reader getA() {
			return new StringReader("foo=bar&baz=qux");
		}
	}

	@Test
	public void k05_requestBean_reader() throws Exception {
		K5 x = remote(K.class,K5.class);
		assertEquals("{baz:'qux',foo:'bar'}",x.post(new K5a()));
	}

	//-----------------------------------------------------------------------------------------------------------------
	// RequestBean @FormData, Collections
	//-----------------------------------------------------------------------------------------------------------------

	@Remote(path="/")
	public static interface K6 {
		@RemoteOp(path="/") String postX1(@Request K6a rb);
		@RemoteOp(path="/") String postX2(@Request(serializer=XSerializer.class) K6a rb);
	}

	public static class K6a {
		@FormData
		public List<Object> getA() {
			return AList.of("foo","","true","123","null",true,123,null);
		}
		@FormData("b")
		public List<Object> getX1() {
			return AList.of("foo","","true","123","null",true,123,null);
		}
		@FormData(n="c",serializer=ListSerializer.class)
		public List<Object> getX2() {
			return AList.of("foo","","true","123","null",true,123,null);
		}
		@FormData(n="d",aev=true)
		public List<Object> getX3() {
			return AList.create();
		}
		@FormData("e")
		public List<Object> getX4() {
			return null;
		}
		@FormData("f")
		public Object[] getX5() {
			return new Object[]{"foo","","true","123","null",true,123,null};
		}
		@FormData(n="g",serializer=ListSerializer.class)
		public Object[] getX6() {
			return new Object[]{"foo","","true","123","null",true,123,null};
		}
		@FormData(n="h",aev=true)
		public Object[] getX7() {
			return new Object[]{};
		}
		@FormData("i")
		public Object[] getX8() {
			return null;
		}
	}

	@Test
	public void k06_requestBean_collections() throws Exception {
		K6 x1 = remote(K.class,K6.class);
		K6 x2 = client(K.class).partSerializer(UonSerializer.class).build().getRemote(K6.class);
		assertEquals("{a:'foo,,true,123,null,true,123,null',b:'foo,,true,123,null,true,123,null',c:'foo||true|123|null|true|123|null',d:'',f:'foo,,true,123,null,true,123,null',g:'foo||true|123|null|true|123|null',h:''}",x1.postX1(new K6a()));
		assertEquals("{a:'@(foo,\\'\\',\\'true\\',\\'123\\',\\'null\\',true,123,null)',b:'@(foo,\\'\\',\\'true\\',\\'123\\',\\'null\\',true,123,null)',c:'foo||true|123|null|true|123|null',d:'@()',f:'@(foo,\\'\\',\\'true\\',\\'123\\',\\'null\\',true,123,null)',g:'foo||true|123|null|true|123|null',h:'@()'}",x2.postX1(new K6a()));
		assertEquals("{a:'fooXXtrueX123XnullXtrueX123Xnull',b:'fooXXtrueX123XnullXtrueX123Xnull',c:'foo||true|123|null|true|123|null',d:'',f:'fooXXtrueX123XnullXtrueX123Xnull',g:'foo||true|123|null|true|123|null',h:''}",x2.postX2(new K6a()));
	}

	//------------------------------------------------------------------------------------------------------------------
	// Helper methods.
	//------------------------------------------------------------------------------------------------------------------

	private static NameValuePair part(String name, Object val) {
		return basicPart(name,val);
	}

	private static PartList parts(Object...pairs) {
		return partList(pairs);
	}

	private static RestClientBuilder client(Class<?> c) {
		return MockRestClient.create(c);
	}

	private static <T> T remote(Class<?> rest,Class<T> t) {
		return MockRestClient.build(rest).getRemote(t);
	}
}
