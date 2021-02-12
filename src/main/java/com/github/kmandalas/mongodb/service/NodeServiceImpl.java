package com.github.kmandalas.mongodb.service;

import com.github.kmandalas.mongodb.document.Node;
import com.github.kmandalas.mongodb.exception.NotFoundException;
import com.github.kmandalas.mongodb.object.TreeNode;
import com.github.kmandalas.mongodb.repository.NodeRepository;
import lombok.extern.java.Log;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

@Log
@Service
public class NodeServiceImpl implements NodeService {

    private final NodeRepository nodeRepository;

    public NodeServiceImpl(NodeRepository nodeRepository) {
        this.nodeRepository = nodeRepository;
    }

    @Override
    public TreeNode getFullTree(int treeId) {
        List<Node> nodes = nodeRepository.findDistinctByTreeId(treeId).orElseThrow(NotFoundException::new);

        List<TreeNode> treeNodes = new ArrayList<>();
        for (Node node : nodes) {
            TreeNode treeNode = new TreeNode();
            BeanUtils.copyProperties(node, treeNode, "id", "children");

            treeNodes.add(treeNode);
        }

        return NodeService.assembleTree(treeNodes, NodeService.DEFAULT_ROOT_NODE_ID);
    }

    @Override
	@Transactional(readOnly = true)
    public TreeNode getSubTree(int treeId, String nodeId, Long maxDepth) {
        List<Node> nodes = nodeRepository.getSubTree(treeId, nodeId, null).orElseThrow(NotFoundException::new);

        List<TreeNode> flatList = nodes.stream()
                .map(Node::getDescendants)
                .flatMap(Collection::stream)
                .map(node -> {
                    TreeNode tr = new TreeNode();
                    BeanUtils.copyProperties(node, tr, "id");
                    return tr;
                })
                .collect(Collectors.toList());

        TreeNode root = new TreeNode();
        BeanUtils.copyProperties(nodes.get(0), root, "id", "children");
        flatList.add(root);

        return (NodeService.assembleTree(flatList, nodeId));
    }

	@Override
    @Transactional(rollbackFor = Exception.class)
	public void deleteNodes(int treeId, String nodeId)  {
		// ... perform validations etc.
		List<Node> nodes = nodeRepository.getSubTree(treeId, nodeId, 1L).orElseThrow(NotFoundException::new);
		var target = nodes.get(0);
		if (!CollectionUtils.isEmpty(target.getDescendants())) {
			target.getDescendants().forEach(n -> n.setParentId(target.getParentId()));
			nodeRepository.saveAll(target.getDescendants());
		}

		nodeRepository.delete(target);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void create(TreeNode treeNode) {
    	// ... check if parent exists etc.
    	Node node = new Node();
    	node.setTreeId(treeNode.getTreeId());
    	node.setParentId(treeNode.getParentId());
    	node.setName(treeNode.getName());
    	node.setVersionId(treeNode.getVersionId());
    	node.setEntityType(treeNode.getEntityType());
    	node.setNodeId(UUID.randomUUID().toString()); // set a unique nodeId based on your policy

    	nodeRepository.save(node);
    	// iterate children and persist them as well...
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void move(int treeId, String nodeId, String newParentNodeId) {
		// ... perform validations etc.
		var node = nodeRepository.findDistinctByTreeIdAndNodeId(treeId, nodeId).orElseThrow(NotFoundException::new);
		node.setParentId(List.of(newParentNodeId));
		nodeRepository.save(node);
	}

}
