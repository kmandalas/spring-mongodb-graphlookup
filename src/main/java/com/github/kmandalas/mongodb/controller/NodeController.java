package com.github.kmandalas.mongodb.controller;

import com.github.kmandalas.mongodb.object.TreeNode;
import com.github.kmandalas.mongodb.service.NodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NodeController {

    @Autowired
    NodeService nodeService;

    @GetMapping(value = "/app/{treeId}")
    public ResponseEntity<TreeNode> getFullTree(@PathVariable("treeId") int treeId) throws Exception {
        return ResponseEntity.ok(nodeService.getFullTree(treeId));
    }

    @GetMapping(value = "/app/{treeId}/st/{nodeId}")
    public ResponseEntity<TreeNode> getSubtree(@PathVariable("treeId") int treeId, @PathVariable("nodeId") int nodeId) throws Exception {
        return ResponseEntity.ok(nodeService.getSubTree(treeId, nodeId));
    }

    @DeleteMapping(value = "/app/{treeId}/{nodeId}")
	public String deleteNodes(@PathVariable("treeId") int treeId, @PathVariable("nodeId") int nodeId) throws Exception {
    	nodeService.deleteNodes(treeId, nodeId);
    	return "OK";
	}

}
