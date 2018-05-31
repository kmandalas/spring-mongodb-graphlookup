package com.github.kmandalas.mongodb.application;

import com.github.kmandalas.mongodb.repositories.NodeRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories(basePackageClasses = {NodeRepository.class})
public class Application {

  public static void main(final String args[]) {
    SpringApplication.run(Application.class, args);
  }

}
