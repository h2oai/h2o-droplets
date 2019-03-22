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
package water.droplets;

import water.*;
import water.fvec.Frame;
import water.fvec.NFSFileVec;
import water.fvec.Vec;
import water.parser.ParseDataset;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * H2O bootstrap example.
 *
 * The example implements a library which provides a
 * method putting given greetings message into K/V store.
 */
public class H2OJavaDroplet {

  public static final String MSG = "Hello %s!";

  /** Simple Iced-object which will be serialized over network */
  public static final class StringHolder extends Iced {
    final String msg;

    public StringHolder(String msg) {
      this.msg = msg;
    }

    public String hello(String name) {
      return String.format(msg, name);

    }
  }

  /**
   * Creates a key and value holding a simple message in {@link water.droplets.H2OJavaDroplet.StringHolder}.
   *
   * @return key referencing stored value.
   */
  public static final Key hello() {
    Key vkey = Key.make("hello.key");
    StringHolder value = new StringHolder(MSG);
    DKV.put(vkey, value);

    return vkey;
  }

  /** Application Entry Point */
  public static void main(String[] args) {

    // Run H2O and build a cloud of 1 member
    H2OApp.main(args);

    if (H2O.ARGS.client) {
      H2O.waitForCloudSize(1, TimeUnit.SECONDS.toMillis(10));
      final Key key = Key.make("fromJava");
      try {
      final NFSFileVec lazy = NFSFileVec.make("data/iris.csv");
      final Frame fr = ParseDataset.parse(key, lazy._key);

      final Value removed = DKV.get(fr._key);
      System.out.println("Retrieve after removal " + removed);

      } catch (IOException e) {
        e.printStackTrace();
      }

    }
  }
}

