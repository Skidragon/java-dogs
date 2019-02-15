package com.lambdaschool.restdogs;


import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j // Lambok autocreates S1f4j-based logs
@Configuration //Indicates that a class declares one or more @Bean
public class SeedDatabase {

    @Bean
    // CommandLine Runner - Spring Boot Runs all Beans at startup
    public CommandLineRunner initDB(DogRepository dogRepo) {
        return args -> {
            log.info("Seeding " + dogRepo.save(new Dog("Springer", 50, false)));
            log.info("Seeding " + dogRepo.save(new Dog("Bulldog", 50, true)));
            log.info("Seeding " + dogRepo.save(new Dog("Collie", 50, false)));
            log.info("Seeding " + dogRepo.save(new Dog("Boston Terrie", 35, true)));
            log.info("Seeding " + dogRepo.save(new Dog("Corgie", 35, true)));
        };
    }
}
