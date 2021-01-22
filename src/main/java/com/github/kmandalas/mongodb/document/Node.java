
package com.github.kmandalas.mongodb.document;

import com.github.kmandalas.mongodb.enumeration.EntityType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "node")
@Getter
@Setter
@NoArgsConstructor
public class Node {

  @Id
  private String id;

  private int nodeId;

  private int versionId;

  private String name;

  private EntityType entityType;

  private int treeId;

  private List<Integer> parentId;

  @Transient
  private List<Node> children;

}
