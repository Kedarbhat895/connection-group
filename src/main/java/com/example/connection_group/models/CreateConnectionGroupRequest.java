package com.example.connection_group.models;


import java.util.List;
import java.util.Set;


public class CreateConnectionGroupRequest {
  private String connectionGroupName;
  private String rootNode;

  private Set<String> nodes;
  private List<List<String>> connectedComponents;

  private String userName;

  public String getUserName() {
    return userName;
  }

  public CreateConnectionGroupRequest(String connectionGroupName, Set<String> nodes,
      String userName) {
    this.connectionGroupName = connectionGroupName;
    this.nodes = nodes;
    this.userName = userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public CreateConnectionGroupRequest() {
  }

  public void setConnectionGroupName(String connectionGroupName) {
    this.connectionGroupName = connectionGroupName;
  }

  public void setRootNode(String rootNode) {
    this.rootNode = rootNode;
  }

  public void setNodes(Set<String> nodes) {
    this.nodes = nodes;
  }

  public void setConnectedComponents(List<List<String>> connectedComponents) {
    this.connectedComponents = connectedComponents;
  }

  public List<List<String>> getConnectedComponents() {
    return connectedComponents;
  }

  public String getConnectionGroupName() {
    return connectionGroupName;
  }

  public Set<String> getNodes() {
    return nodes;
  }

  public String getRootNode() {
    return rootNode;
  }
}
