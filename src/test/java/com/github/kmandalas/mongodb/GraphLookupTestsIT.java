package com.github.kmandalas.mongodb;

import com.github.kmandalas.mongodb.document.Node;
import com.github.kmandalas.mongodb.enumeration.EntityType;
import com.github.kmandalas.mongodb.object.TreeNode;
import com.github.kmandalas.mongodb.repository.NodeRepository;
import com.github.kmandalas.mongodb.service.NodeService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

@SpringBootTest
@TestInstance(PER_CLASS)
class GraphLookupTestsIT {

    @Autowired
    NodeService nodeService;

    @Autowired
	NodeRepository nodeRepository;

    String parentUuid = UUID.randomUUID().toString();

    @BeforeAll
	@Transactional
	void populate() {
		Node treeNode = new Node();
		treeNode.setTreeId(1001);
		treeNode.setNodeId(parentUuid);
		treeNode.setName("oak");
		treeNode.setParentId(List.of("-1"));
		treeNode.setVersionId(new Random().nextInt());
		treeNode.setEntityType(EntityType.type_1);

		nodeRepository.save(treeNode);

		Node leafNode = new Node();
		leafNode.setTreeId(1001);
		leafNode.setNodeId(UUID.randomUUID().toString());
		leafNode.setName("leaf");
		leafNode.setParentId(List.of(parentUuid));
		leafNode.setVersionId(new Random().nextInt());
		leafNode.setEntityType(EntityType.type_5);

		nodeRepository.save(leafNode);
	}

	@DisplayName(value = "given an existing tree and nodeId, retrieve its descendants")
    @Test
    void testSubTreeRetrieval() {
        TreeNode node = nodeService.getSubTree(1001, parentUuid, null);
        assertThat(node).isNotNull();
    }

    // add your integration tests (IT) here

}
