package com.github.kmandalas.mongodb.repository;

import com.github.kmandalas.mongodb.documents.Node;

import java.util.List;
import java.util.Optional;

public interface NodeGraphLookupRepository {

  Optional<List<Node>> getSubTree(int changesetId, int nodeId) throws Exception;

}
