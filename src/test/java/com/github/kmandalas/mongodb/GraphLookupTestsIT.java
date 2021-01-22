package com.github.kmandalas.mongodb;

import com.github.kmandalas.mongodb.application.Application;
import com.github.kmandalas.mongodb.object.TreeNode;
import com.github.kmandalas.mongodb.service.NodeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class)
class GraphLookupTestsIT {

    @Autowired
    NodeService nodeService;

    @Test
    void testSubTreeRetrieval() throws Exception {
        TreeNode node = nodeService.getSubTree(1001, 5);
        assertThat(node).isNotNull();
    }

}
