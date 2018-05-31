package com.github.kmandalas.mongodb.application;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.github.kmandalas.mongodb.repositories.NodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@EnableMongoRepositories(basePackageClasses = {NodeRepository.class})
public class Application implements CommandLineRunner {

  @Autowired
  private NodeRepository nodeRepository;

  public static void main(final String args[]) {
    SpringApplication.run(Application.class, args);
  }

  public static List<Integer> setParents(ArrayNode an) {
    List<Integer> parents = new ArrayList<>();
    if (an != null) {
      for (JsonNode anAn : an) {
        int parent = anAn.asInt();
        parents.add(parent);
      }
    }
    return parents;
  }

  @Override
  public void run(String... strings) throws Exception {
  }
}
