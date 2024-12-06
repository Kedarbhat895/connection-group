package com.example.connection_group.models;


import java.util.Set;

public class ConnectionGroup {


  private String name;
  private Set<VirtualNode> vNodes;

  public void setName(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public Set<VirtualNode> getvNodes() {
    return vNodes;
  }

  public void setVNodes(Set<VirtualNode> vNodes) {
    this.vNodes = vNodes;
  }

}
