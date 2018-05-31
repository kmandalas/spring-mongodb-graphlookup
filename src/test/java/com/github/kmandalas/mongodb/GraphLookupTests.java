package com.github.kmandalas.mongodb;

import com.github.kmandalas.mongodb.application.Application;
import com.github.kmandalas.mongodb.repositories.NodeRepository;
import org.bson.Document;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.GraphLookupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class GraphLookupTests {

  @Autowired
  NodeRepository nodeRepository;

  @Autowired
  MongoTemplate mongoTemplate;

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
