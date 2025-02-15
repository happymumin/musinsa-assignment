package com.musinsa.coordinator

import com.musinsa.coordinator.config.Env
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling
import java.util.*

@EnableScheduling
@SpringBootApplication
class Application

fun main(args: Array<String>) {
    init()
    runApplication<Application>(*args) {
        setDefaultProperties(mapOf("spring.profiles.default" to Env.DEV))
    }
}

fun init() {
    TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"))
}


