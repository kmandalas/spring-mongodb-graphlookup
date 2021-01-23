package com.github.kmandalas.mongodb.repository;

import com.github.kmandalas.mongodb.document.Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.GraphLookupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;

@Repository
public class NodeRepositoryImpl implements NodeGraphLookupRepository {

  private final MongoTemplate mongoTemplate;

  public NodeRepositoryImpl(MongoTemplate mongoTemplate) {
    this.mongoTemplate = mongoTemplate;
  }

  @Override
  public Optional<List<Node>> getSubTree(int treeId, int nodeId) throws Exception {
    final Criteria byNodeId = new Criteria("nodeId").is(nodeId);
    final Criteria byTreeId = new Criteria("treeId").is(treeId);
    final MatchOperation matchStage = Aggregation.match(byTreeId.andOperator(byNodeId));

    GraphLookupOperation graphLookupOperation = GraphLookupOperation.builder()
            .from("nodes")
            .startWith("$nodeId")
            .connectFrom("nodeId")
            .connectTo("parentId")
            .restrict(new Criteria("treeId").is(treeId))
            .as("children");

    Aggregation aggregation = Aggregation.newAggregation(matchStage, graphLookupOperation);
    // Document document = aggregation.toDocument("node", Aggregation.DEFAULT_CONTEXT);
    // TODO: pretty-print the aggregation command
    List<Node> results = mongoTemplate.aggregate(aggregation, "nodes", Node.class).getMappedResults();
    return CollectionUtils.isEmpty(results) ? Optional.empty() : Optional.of(results);
  }

}
