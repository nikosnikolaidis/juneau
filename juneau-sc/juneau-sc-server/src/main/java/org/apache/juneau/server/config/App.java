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
package org.apache.juneau.server.config;

import org.apache.juneau.microservice.jetty.JettyMicroservice;
import org.apache.juneau.server.config.rest.LoadConfigResource;

/**
 * Entry-point class.
 */
public class App {

    /**
     * Entry-point method.
     *
     * @param args Command-line arguments.
     * @throws Exception Error occurred.
     */
    public static void main(String[] args) throws Exception {
        JettyMicroservice
                .create()
                .args(args)
                .servlet(LoadConfigResource.class)
                .build()
                .start()
                .startConsole()
                .join();
    }
}
// ter o xml do jetty padr??o na aplica????o
// posibilitar a remo????o do bin.xml