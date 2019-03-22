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

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{BeforeAndAfter, FlatSpec, Matchers}
import water.{DKV, H2O}
import water.droplets.H2OScalaDroplet.StringHolder

/**
 * The test verify implementation of
 * `water.droplets.H2OScalaDroplet` class.
 */
@RunWith(classOf[JUnitRunner])
class H2OScalaDropletSpec extends FlatSpec with Matchers with BeforeAndAfter {

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
    val vkey = H2OScalaDroplet.hello()

    // Get POJO holding hello message
    val helloHolder = DKV.get(vkey).get[StringHolder]()

    // Verify the message
    helloHolder.hello("H2O") shouldEqual "Hello H2O!"
  }
}
