package com.github.kmandalas.mongodb.controller;

import com.github.kmandalas.mongodb.service.NodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NodeController {

    @Autowired
    NodeService nodeService;

    @GetMapping(value = "/app/{changesetId}")
    public void getFullTree(@PathVariable("changesetId") int changesetId) throws Exception {
        nodeService.getFullTree(changesetId);
    }

    @GetMapping(value = "/app/{changesetId}/st/{nodeId}")
    public void getSubtree(@PathVariable("changesetId") int changesetId, @PathVariable("nodeId") int nodeId) throws Exception {
        nodeService.getSubTree(changesetId, nodeId);
    }

}
