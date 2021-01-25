package com.github.kmandalas.mongodb.service;

import com.github.kmandalas.mongodb.object.TreeNode;
import org.springframework.util.CollectionUtils;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public interface NodeService {

    int DEFAULT_ROOT_NODE_ID = -1;

    TreeNode getFullTree(int treeId);

    TreeNode getSubTree(int treeId, int nodeId, Long maxDepth);

    void deleteNodes(int treeId, int nodeId);

	void create(TreeNode treeNode);

	void move(int treeId, int nodeId, int newParentNodeId);

	static TreeNode assembleTree(final List<TreeNode> nodes, final int rootNodeId) {
        final Map<Integer, TreeNode> mapTmp = new LinkedHashMap<>();
        // Save all nodes to a map
        for (final TreeNode current : nodes) {
            mapTmp.put(current.getNodeId(), current);
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
