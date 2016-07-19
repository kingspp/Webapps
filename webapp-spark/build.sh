#!/usr/bin/env bash

mvn -f pom.xml clean compile assembly:single
java -jar target/webapp-spark-jar-with-dependencies.jar app