/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package water.droplets

import org.scalatest.{BeforeAndAfter, Matchers, FlatSpec}
import water.{H2O, DKV}
import water.droplets.H2OJavaDroplet.StringHolder

/**
 * The test verify implementation of
 * {@link water.droplets.H2OScalaDroplet} class.
 */
class H2OJavaDropletSpec extends FlatSpec with Matchers with BeforeAndAfter {

  // Init cloud before run
  before {
    // Setup cloud name
    val args = Array[String]("-name", "h2o_test_cloud")
    // Build a cloud of 1
    H2O.main(args)
    H2O.waitForCloudSize(1, 10*1000 /* ms */)
  }

  "H2OScalaDroplet.hello method" should "store a message into K/V" in {
    // Generate hello message and store it in K/V
    val vkey = H2OJavaDroplet.hello()

    // Get POJO holding hello message
    val helloHolder = DKV.get(vkey).get[StringHolder]()

    // Verify the message
    helloHolder.hello("H2O") shouldEqual "Hello H2O!"
  }
}
