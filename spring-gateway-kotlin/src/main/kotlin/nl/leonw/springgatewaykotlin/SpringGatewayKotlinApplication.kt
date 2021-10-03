package nl.leonw.springgatewaykotlin

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SpringGatewayKotlinApplication

fun main(args: Array<String>) {
	runApplication<SpringGatewayKotlinApplication>(*args)
}
