# Spring gateway demo project

## Purpose

The goal is for me to get to know spring cloud gateway, and compare it to graphql from a development perspective.
A side-goal is to play a bit more with Kotlin, and compare Kotlin and Java 17 implementations.


## Architecture

The "goal" is to provide a sort of API gateway to the outside world. This is done by using spring-cloud-gateway, and independently with spring-graphql (which is not final yet). Setting aside the relative strengths and weaknesses of an entity based REST vs GraphQL pattern, it is interesting to see what the code looks like.

The gateways are going to have both a Java and a Kotlin implementation, because I'm really curious how much the difference still is now Java records and text blocks have been introduced. I expect the biggest difference to be in error handling (the kotlin `when` with pattern matching is probably more convenient than multiple catch blocks), but we shall see.

## Spring

Spring apps are going to be generated with start.spring.io on the now current version.
