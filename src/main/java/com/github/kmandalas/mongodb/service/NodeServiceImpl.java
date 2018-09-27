package com.github.kmandalas.mongodb.service;

import com.github.kmandalas.mongodb.document.Node;
import com.github.kmandalas.mongodb.exception.NotFoundException;
import com.github.kmandalas.mongodb.object.TreeNode;
import com.github.kmandalas.mongodb.repository.NodeRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NodeServiceImpl implements NodeService {

  @Autowired
  private NodeRepository nodeRepository;

  @Override
  public TreeNode getFullTree(int changesetId) throws Exception {
    return null;
  }

  @Override
  public TreeNode getSubTree(int changesetId, int nodeId) throws Exception {
    List<Node> nodes = nodeRepository.getSubTree(changesetId, nodeId).orElseThrow(NotFoundException::new);

    List<TreeNode> flatList = nodes.stream().
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

    return (NodeService.assembleTree(flatList, nodeId));
  }

}
