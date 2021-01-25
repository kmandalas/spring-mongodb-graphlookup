package com.github.kmandalas.mongodb.repository;

import com.github.kmandalas.mongodb.document.Node;

import java.util.List;
import java.util.Optional;

public interface NodeGraphLookupRepository {

	Optional<List<Node>> getSubTree(int treeId, int nodeId, Long maxDepth);

}
