package com.github.kmandalas.mongodb;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.github.kmandalas.mongodb.application.Application;
import com.github.kmandalas.mongodb.application.Utils;
import com.github.kmandalas.mongodb.documents.EntityType;
import com.github.kmandalas.mongodb.documents.Node;
import com.github.kmandalas.mongodb.repositories.NodeRepository;
import org.bson.Document;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.GraphLookupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static com.github.kmandalas.mongodb.application.Application.setParents;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class GraphLookupTests {

  @Autowired
  NodeRepository nodeRepository;

  @Autowired
  MongoTemplate mongoTemplate;

  @Value("${populate}")
  private boolean populate;

  @Value("${changesets}")
  private int changesets;

  @Before
  public void populate() throws Exception {
    if (populate) {
      for (int c = 0; c < changesets; c++) {
        JsonNode myJson = Utils.readJson("static-tree.json");
        List<Node> myNodeList = new ArrayList<>();

        for (int i = 0; i < myJson.size(); i++) {
          JsonNode cnode = myJson.get(i);
          Node node = new Node();

          node.setNodeId(cnode.get("id").asInt());
          node.setName(cnode.get("name").asText());
          node.setParentId(setParents((ArrayNode) cnode.get("parentId")));
          node.setTenantId(cnode.get("tenantId").asInt());
          node.setType(EntityType.Node);
          node.setMds(cnode.get("isMds").asBoolean());
          // node.setChangesetId(cnode.get("changesetId").asInt());
          node.setChangesetId(c);

          myNodeList.add(node);
        }

        nodeRepository.saveAll(myNodeList);
      }

      System.out.println("Import finished.");
    } else {
      System.out.println("Import skipped.");
    }
  }

  @Test
  public void shouldRenderCorrectly() {

    final Criteria isDimension = new Criteria("parentId").size(0);
    final Criteria byChangesetId = new Criteria("changesetId").is(14);
    final MatchOperation matchStage = Aggregation.match(byChangesetId.andOperator(isDimension));

    GraphLookupOperation graphLookupOperation = GraphLookupOperation.builder() //
            .from("treeView") //
            .startWith("$nodeId") //
            .connectFrom("nodeId") //
            .connectTo("parentId") //
            .maxDepth(0) //
            .restrict(new Criteria("changesetId").is(14))
            .as("children");

    Aggregation aggregation = Aggregation.newAggregation(matchStage, graphLookupOperation);

    Document document = aggregation.toDocument("node", Aggregation.DEFAULT_CONTEXT);
    System.out.println(document.toJson());

    List<Document> results = mongoTemplate.aggregate(aggregation, "node", Document.class).getMappedResults();
    assertThat(results.size() == 14);
  }
}
