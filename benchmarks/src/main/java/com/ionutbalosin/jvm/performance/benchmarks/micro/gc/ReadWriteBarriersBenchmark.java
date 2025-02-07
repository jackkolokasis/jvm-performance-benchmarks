/*
 * JVM Performance Benchmarks
 *
 * Copyright (C) 2019 - 2023 Ionut Balosin
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.ionutbalosin.jvm.performance.benchmarks.micro.gc;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.CompilerControl;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;

/*
 * Test the overhead of read/write barriers while iterating through an array of Integers and
 * exchanging the values between two array entries (i.e., array[i] <-> array[j]).
 *
 * Deprecated: using micro-benchmarks to gauge the performance of the Garbage Collectors might result in misleading conclusions.
 */
@Deprecated
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 5, time = 10, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 10, timeUnit = TimeUnit.SECONDS)
@Fork(value = 2)
@State(Scope.Benchmark)
public class ReadWriteBarriersBenchmark {

  // $ java -jar */*/benchmarks.jar ".*ReadWriteBarriersBenchmark.*"
  // Recommended command line options:
  // - JVM options:
  //   {-XX:+UseSerialGC, -XX:+UseParallelGC, -XX:+UseG1GC, -XX:+UseShenandoahGC, -XX:+UseZGC
  //    -XX:+UnlockExperimentalVMOptions -XX:+UseEpsilonGC}
  // - JMH options: -prof gc

  private final Random random = new Random(16384);

  private Integer[] array;
  private int index;

  @Param({"262144"})
  private int size;

  @Setup()
  public void setup() {
    array = new Integer[size];
    for (int i = 0; i < size; i++) {
      array[i] = random.nextInt();
    }
    index = 0;
  }

  @Benchmark
  public void gc() {
    test();
  }

  @CompilerControl(CompilerControl.Mode.DONT_INLINE)
  private void test() {
    int lSize = size;
    int mask = lSize - 1;

    for (int i = 0; i < lSize; i++) {
      Integer aux = array[i];
      array[i] = array[(i + index) & mask];
      array[(i + index) & mask] = aux;
    }

    index++;
  }
}
