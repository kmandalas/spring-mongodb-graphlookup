package com.github.kmandalas.mongodb.service;

import com.github.kmandalas.mongodb.document.Node;
import com.github.kmandalas.mongodb.exception.NotFoundException;
import com.github.kmandalas.mongodb.object.TreeNode;
import com.github.kmandalas.mongodb.repository.NodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NodeServiceImpl implements NodeService {

  @Autowired
  private NodeRepository nodeRepository;

  @Override
  public TreeNode getFullTree(int changesetId) throws Exception {
    List<Node> nodes = nodeRepository.findDistinctByChangesetId(changesetId);

    return (NodeService.assembleTree(nodes, NodeService.DEFAULT_ROOT_NODE_ID));
  }

  @Override
  public TreeNode getSubTree(int changesetId, int nodeId) throws Exception {
    List<Node> nodes = nodeRepository.getSubTree(changesetId, nodeId).orElseThrow(NotFoundException::new);

    return (NodeService.assembleTree(nodes, nodeId));
  }

}
