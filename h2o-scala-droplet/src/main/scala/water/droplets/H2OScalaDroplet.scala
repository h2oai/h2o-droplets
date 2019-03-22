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

import java.util.concurrent.TimeUnit

import water._
import water.fvec.NFSFileVec
import water.parser.ParseDataset

/**
  * H2O Scala bootstrap example.
  *
  * The example implements a tiny Scala library which provides a
  * method putting given greetings message into K/V store.
  */
object H2OScalaDroplet {

  val MSG = "Hello %s!"

  case class StringHolder(msg: String) extends Keyed[StringHolder] {
    def hello(name: String): String =
      String.format(msg, name)

    override def checksum_impl(): Long = msg.hashCode
  }

  def hello(): Key[StringHolder] = {
    val vkey = Key.make("hello.key").asInstanceOf[Key[StringHolder]]
    val value: StringHolder = StringHolder(MSG)
    DKV.put(vkey, value)

    vkey
  }

  /** Application Entry Point */
  def main(args: Array[String]): Unit = { // Run H2O and build a cloud of 1 member
    H2OApp.main(args)
    if (H2O.ARGS.client) {
      H2O.waitForCloudSize(1, TimeUnit.SECONDS.toMillis(10))
      val key = Key.make("fromJava")
      val vec = NFSFileVec.make("data/iris.csv")
      val fr = ParseDataset.parse(key, vec._key)
      val removed = DKV.get(fr._key)
      println("Retrieve after removal " + removed)
    }
  }
}
