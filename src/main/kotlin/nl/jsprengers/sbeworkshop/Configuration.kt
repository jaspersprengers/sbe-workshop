package nl.jsprengers.sbeworkshop

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class Configuration {

    @Bean
    fun configureMapper(): ObjectMapper {
        return ObjectMapper().registerKotlinModule()
    }
}
