package com.github.kmandalas.mongodb.controller;

import com.github.kmandalas.mongodb.object.TreeNode;
import com.github.kmandalas.mongodb.service.NodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class NodeController {

    @Autowired
    NodeService nodeService;

    @GetMapping(value = "/app/{treeId}")
    public ResponseEntity<TreeNode> getFullTree(@PathVariable("treeId") int treeId) {
        return ResponseEntity.ok(nodeService.getFullTree(treeId));
    }

    @GetMapping(value = "/app/{treeId}/st/{nodeId}")
    public ResponseEntity<TreeNode> getSubtree(@PathVariable("treeId") int treeId, @PathVariable("nodeId") int nodeId) {
        return ResponseEntity.ok(nodeService.getSubTree(treeId, nodeId, null));
    }

    @DeleteMapping(value = "/app/{treeId}/{nodeId}")
	public ResponseEntity<Void> deleteNodes(@PathVariable("treeId") int treeId, @PathVariable("nodeId") int nodeId) {
    	nodeService.deleteNodes(treeId, nodeId);
    	return ResponseEntity.noContent().build();
	}

	@PostMapping(value = "/app/")
	public ResponseEntity<Void> create(@RequestBody TreeNode treeNode) {
		nodeService.create(treeNode);
		return ResponseEntity.ok().build();
	}

	@PutMapping(value = "/app/{treeId}/{nodeId}")
	public ResponseEntity<Void> move(@PathVariable("treeId") int treeId, @PathVariable("nodeId") int nodeId,
			@RequestParam int newParentNodeId) {
		nodeService.move(treeId, nodeId , newParentNodeId);
		return ResponseEntity.ok().build();
	}

}
