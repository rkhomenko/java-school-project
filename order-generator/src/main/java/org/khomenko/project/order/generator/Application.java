package org.khomenko.project.order.generator;

import com.github.javafaker.Faker;
import lombok.extern.slf4j.Slf4j;

import org.khomenko.project.order.generator.init.DatabaseIniter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan("org.khomenko.project.core.data.models")
@EnableJpaRepositories("org.khomenko.project.core.data.repositories")
@Slf4j
public class Application implements ApplicationRunner {
    @Autowired
    private DatabaseIniter databaseIniter;

    @Bean
    Faker javaFaker() {
        return new Faker();
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(ApplicationArguments args) {
        if (args.containsOption("init")) {
            databaseIniter.init();
        }
    }
}
