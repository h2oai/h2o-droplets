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

import water.DKV
import water.Iced
import water.Key

/**
 * H2O Scala bootstrap example.
 *
 * The example implements a tiny Scala library which provides a
 * method putting given greetings message into K/V store.
 */
object H2OJavaDroplet {

  val MSG = "Hello %s!";

  case class StringHolder(val msg: String) extends Iced {
    def hello(name:String) : String =
      String.format(msg, name)
  }

  def hello():Key = {
    val vkey  = Key.make("hello.key")
    val value = StringHolder(MSG)
    DKV.put(vkey, value)

    vkey
  }
}
