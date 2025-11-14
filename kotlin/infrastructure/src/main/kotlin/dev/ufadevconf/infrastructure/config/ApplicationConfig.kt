package dev.ufadevconf.infrastructure.config

import dev.ufadevconf.application.PickupPointApplicationService
import dev.ufadevconf.domain.PickupPointRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ApplicationConfig {

    @Bean
    fun pickupPointApplicationService(repository: PickupPointRepository): PickupPointApplicationService =
        PickupPointApplicationService(repository)
}
