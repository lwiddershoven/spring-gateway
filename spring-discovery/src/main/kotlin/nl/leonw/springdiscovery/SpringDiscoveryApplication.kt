package nl.leonw.springdiscovery

import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Component

@SpringBootApplication
@EnableEurekaServer
class SpringDiscoveryApplication

fun main(args: Array<String>) {
	runApplication<SpringDiscoveryApplication>(*args)
}

@Component
class ShowAll(val config: Config) : CommandLineRunner {
	override fun run(vararg args: String?) {
		println("Config $config")
	}

}

@Configuration
@ConfigurationProperties(prefix = "config")
// Need to be _var_, not val! And have a initial value.
data class Config(var username: String = "", var password: String = "") {}

