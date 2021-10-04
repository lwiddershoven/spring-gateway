package nl.leonw.springdiscovery

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer

@SpringBootApplication
@EnableEurekaServer
class SpringDiscoveryApplication

fun main(args: Array<String>) {
	runApplication<SpringDiscoveryApplication>(*args)
}
