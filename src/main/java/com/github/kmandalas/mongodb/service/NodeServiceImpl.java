package com.github.kmandalas.mongodb.service;

import com.github.kmandalas.mongodb.document.Node;
import com.github.kmandalas.mongodb.exception.NotFoundException;
import com.github.kmandalas.mongodb.object.TreeNode;
import com.github.kmandalas.mongodb.repository.NodeRepository;
import lombok.extern.java.Log;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Predicate;
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

    @Transactional
    @Override
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

    @Override
    @Transactional
    public void deleteSubTree(int treeId, int nodeId) throws NotFoundException {
        List<Node> nodes = nodeRepository.findDistinctByTreeId(treeId).orElseThrow(NotFoundException::new);
        Set<Node> deleted = nodes.stream()
                .filter(n -> n.getTreeId() == nodeId)
                .collect(Collectors.toSet());
        DeleteUpdateResult deletesUpdates = determineDeletesUpdates(new HashSet<>(nodes), new DeleteUpdateResult(new HashSet<>(), deleted));
        nodeRepository.deleteById(deletesUpdates.forDelete);
        nodeRepository.saveAll(deletesUpdates.forUpdate);
    }

    private DeleteUpdateResult determineDeletesUpdates(
            Set<Node> nodes,
            DeleteUpdateResult deleteUpdateResult) {

        Set<Node> currentForDelete = new HashSet<>();
        Set<Node> currentForUpdate = new HashSet<>();

        deleteUpdateResult
                .forDelete
                .forEach(node -> {
                    Predicate<Node> forDeletePredicate =
                            n -> n.getParentId().contains(node.getNodeId()) && n.getParentId().size() == 1;

                    Predicate<Node> forUpdatePredicate = n ->
                            n.getParentId().contains(node.getNodeId()) && n.getParentId().size() > 1;

                    Set<Node> forDelete = nodes
                            .stream()
                            .filter(forDeletePredicate)
                            .collect(Collectors.toSet());

                    currentForDelete.addAll(forDelete);

                    Set<Node> forUpdate = nodes
                            .stream()
                            .filter(forUpdatePredicate)
                            .peek(n -> n.getParentId().remove(node.getNodeId()))
                            .collect(Collectors.toSet());

                    currentForUpdate.addAll(forUpdate);
                });

        Set<Node> finalForDelete = new HashSet<>();
        Set<Node> finalForUpdate = new HashSet<>();

        finalForDelete.addAll(deleteUpdateResult.forDelete);
        finalForDelete.addAll(currentForDelete);

        finalForUpdate.addAll(deleteUpdateResult.forUpdate);
        finalForUpdate.addAll(currentForUpdate);
        finalForUpdate.removeAll(finalForDelete);

        DeleteUpdateResult result = new DeleteUpdateResult(finalForUpdate, finalForDelete);

        if (finalForDelete.size() == deleteUpdateResult.forDelete.size()) {
            return result;
        } else {
            Set<Node> updatedNodes = new HashSet<>(nodes);
            updatedNodes.removeAll(result.forUpdate); //todo: forDelete?
            updatedNodes.addAll(result.forUpdate);
            return determineDeletesUpdates(updatedNodes, result);
        }

    }

    private class DeleteUpdateResult {
        Set<Node> forUpdate;
        Set<Node> forDelete;
        DeleteUpdateResult(Set<Node> forUpdate, Set<Node> forDelete) {
            this.forUpdate = forUpdate;
            this.forDelete = forDelete;
        }
    }

}
