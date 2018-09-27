package com.github.kmandalas.mongodb;

import com.github.kmandalas.mongodb.application.Application;
import com.github.kmandalas.mongodb.object.TreeNode;
import com.github.kmandalas.mongodb.service.NodeService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class GraphLookupTestsIT {

  @Autowired
  NodeService nodeService;

  @Test
  public void testSubTreeRetrieval() throws Exception {
    TreeNode node = nodeService.getSubTree(25080022, 23978341);
  }

}
