
package com.github.kmandalas.mongodb.documents;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "node")
public class Node {

  @Id
  private String id;

  private int nodeId;
  private String name;
  private EntityType type;
  private String modelType;
  private int[] includedIn;
  private boolean isMds;
  private List<Integer> parentId;
  private int changesetId;
  private int tenantId;

  public Node() {
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public int getNodeId() {
    return nodeId;
  }

  public void setNodeId(int nodeId) {
    this.nodeId = nodeId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public EntityType getType() {
    return type;
  }

  public void setType(EntityType type) {
    this.type = type;
  }

  public String getModelType() {
    return modelType;
  }

  public void setModelType(String modelType) {
    this.modelType = modelType;
  }

  public int[] getIncludedIn() {
    return includedIn;
  }

  public void setIncludedIn(int[] includedIn) {
    this.includedIn = includedIn;
  }

  public boolean isMds() {
    return isMds;
  }

  public void setMds(boolean mds) {
    isMds = mds;
  }

  public List<Integer> getParentId() {
    return parentId;
  }

  public void setParentId(List<Integer> parentId) {
    this.parentId = parentId;
  }

  public int getChangesetId() {
    return changesetId;
  }

  public void setChangesetId(int changesetId) {
    this.changesetId = changesetId;
  }

  public int getTenantId() {
    return tenantId;
  }

  public void setTenantId(int tenantId) {
    this.tenantId = tenantId;
  }
}
