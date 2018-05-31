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

//    for (int c = 0; c < 20; c++) {
//      JsonNode myJson = Utils.readJson("static-tree.json");
//      List<Node> myNodeList = new ArrayList<>();
//
//      for (int i = 0; i < myJson.size(); i++) {
//        JsonNode cnode = myJson.get(i);
//        Node node = new Node();
//
//        node.setNodeId(cnode.get("id").asInt());
//        node.setName(cnode.get("name").asText());
//        node.setParentId(setParents((ArrayNode) cnode.get("parentId")));
//        node.setTenantId(cnode.get("tenantId").asInt());
//        node.setType(EntityType.Node);
//        node.setMds(cnode.get("isMds").asBoolean());
//        // node.setChangesetId(cnode.get("changesetId").asInt());
//        node.setChangesetId(c);
//
//        myNodeList.add(node);
//      }
//
//      nodeRepository.saveAll(myNodeList);
//    }
//
//    System.out.println("Import finished.");
  }
}
