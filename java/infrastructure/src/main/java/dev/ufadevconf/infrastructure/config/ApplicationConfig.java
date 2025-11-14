package dev.ufadevconf.infrastructure.config;

import dev.ufadevconf.application.PickupPointApplicationService;
import dev.ufadevconf.domain.PickupPointRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {

    @Bean
    public PickupPointApplicationService pickupPointApplicationService(PickupPointRepository repository) {
        return new PickupPointApplicationService(repository);
    }
}
