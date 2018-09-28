package com.github.kmandalas.mongodb.service;

import com.github.kmandalas.mongodb.document.Node;
import com.github.kmandalas.mongodb.object.TreeNode;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public interface NodeService {

  static final int DEFAULT_ROOT_NODE_ID = -1; // a virtual root acting like the root of all forest of trees

  TreeNode getFullTree(int changesetId) throws Exception;

  TreeNode getSubTree(int changesetId, int nodeId) throws Exception;

  static TreeNode assembleTree(final List<Node> nodes, final int rootNodeId) {
    final List<TreeNode> flatList = nodes.stream().
            map(Node::getChildren).
            flatMap(Collection::stream)
            .map(node -> {
              TreeNode tr = new TreeNode();
              BeanUtils.copyProperties(node, tr, "id");
              return tr;
            }).collect(Collectors.toList());

    TreeNode root = new TreeNode();
    BeanUtils.copyProperties(nodes.get(0), root, "id", "children");
    flatList.add(root);

    final Map<Integer, TreeNode> mapTmp = new LinkedHashMap<>();
    // Save all nodes to a map
    for (final TreeNode current : nodes) {
      mapTmp.put(current.getMasterId(), current);
    }
    // Loop and assign parent/child relationships
    for (final TreeNode current : nodes) {
      final List<Integer> parents = current.getParentId();

      if (!CollectionUtils.isEmpty(parents)) {
        for (final Integer pid : parents) {
          final TreeNode parent = mapTmp.get(pid);
          if (parent != null) {
            parent.addChild(current);
            current.addParent(parent);
          }
        }
      }
    }

    return mapTmp.get(rootNodeId);
  }
}
