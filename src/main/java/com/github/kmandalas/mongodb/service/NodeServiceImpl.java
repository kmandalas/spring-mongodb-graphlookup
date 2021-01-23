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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Log
@Service
public class NodeServiceImpl implements NodeService {

    private final NodeRepository nodeRepository;

    public NodeServiceImpl(NodeRepository nodeRepository) {
        this.nodeRepository = nodeRepository;
    }

    @Override
    public TreeNode getFullTree(int treeId) throws Exception {
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
    public TreeNode getSubTree(int treeId, int nodeId) throws Exception {
        List<Node> nodes = nodeRepository.getSubTree(treeId, nodeId).orElseThrow(NotFoundException::new);

        List<TreeNode> flatList = nodes.stream()
                .map(Node::getChildren)
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

	@Transactional(rollbackFor = Exception.class)
	public void deleteNodes(int treeId, int nodeId) throws Exception {
		List<Node> nodes = nodeRepository.getSubTree(treeId, nodeId).orElseThrow(NotFoundException::new);
		var target = nodes.get(0);
		if (!CollectionUtils.isEmpty(target.getChildren())) {
			target.getChildren().forEach(n -> {
				n.setParentId(target.getParentId());
			});

			nodeRepository.saveAll(target.getChildren());
		}

		nodeRepository.delete(target);
	}

}
