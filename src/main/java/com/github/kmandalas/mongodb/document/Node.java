
package com.github.kmandalas.mongodb.document;

import com.github.kmandalas.mongodb.enumeration.EntityType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "nodes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Node {

  @Id
  private String id;

  private String nodeId;

  private int versionId;

  private String name;

  private EntityType entityType;

  private int treeId;

  private List<String> parentId;

  private List<Node> descendants;

}
