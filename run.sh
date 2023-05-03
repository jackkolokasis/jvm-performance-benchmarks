#!/usr/bin/env bash

export JAVA_HOME=/spare/kolokasis/nativeJVM/jdk17u/build/linux-x86_64-server-release/jdk

java -XX:+UseZGC -jar benchmarks/target/benchmarks.jar ".*ReadBarriersChainOfReferencesBenchmark.*"

#java -XX:+UseG1GC -jar benchmarks/target/benchmarks.jar ".*ReadWriteBarriersBenchmark.*"

#java -XX:+UseParallelGC -jar benchmarks/target/benchmarks.jar ".*.WriteBarriersLoopingOverArrayBenchmark.*"
