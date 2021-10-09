# Spring gateway demo project

## Purpose

The goal is for me to get to know spring cloud gateway, and compare it to graphql from a development perspective.
A side-goal is to play a bit more with Kotlin, and compare Kotlin and Java 17 implementations.


## Architecture

The "goal" is to provide a sort of API gateway to the outside world. This is done by using spring-cloud-gateway, and independently with spring-graphql (which is not final yet). Setting aside the relative strengths and weaknesses of an entity based REST vs GraphQL pattern, it is interesting to see what the code looks like.

The gateways are going to have both a Java and a Kotlin implementation, because I'm really curious how much the difference still is now Java records and text blocks have been introduced. I expect the biggest difference to be in error handling (the kotlin `when` with pattern matching is probably more convenient than multiple catch blocks), but we shall see.

## Spring

Spring apps are going to be generated with start.spring.io on the now current version. We use Java 17 for the  java apps, and 16 for Kotlin as the maven plugin cannot handle Java 17 in the pom.

## Arch

Images are unfortunately not build using mvn spring-boot:build-image. I had hoped that would work but the applications have the weirdest startup problems when running in Docker. They compile and build fine; it's just that they cannot parse classes or files correctly. Weird.

So, instead I'm using my own simple Dockerfile with _Amazon Corretto_ which has as an X86 build for you, and an ARM64 build for my Apple M1 Mac Mini.

If you think this was a painfull discovery you would be right. Automated magic - like x86 interpretation on the m1 mac - is awesome if it works but hell to find out what is wrong when it does not work for you.

## Run

First build the infrastructure components:
- spring-discovery: mvm clean verify ; docker build -t leonw/springdiscovery .

Then run the infrastructure docker-compose to start: 
- Hachicorp consul for proper properties management (spring cloud config would be another option) (http://localhost:8500)
- Hachicorp vault for proper secrets management (there is no real alternative here that is even remotely comparable) (http://localhost:8200)
- Your newly build  service discovery server (aka eureka-server) which we named spring-discovery (UI at localhost:8761)

We don't have logging, metrics or tracing services enabled as 9 docker containers is already quite much, in particular for Windows laptops with a multitude of virus scanners and crapware (like pause popups) installed.

Then run the apps:
- partner-service       :9010
- seller-service        :9011
- supplier-service      :9012
- spring-gateway-java   :9013
- spring-gateway-kotlin :9014
- spring-graphql-java   :9015
- spring-graphql-kotlin :9016




