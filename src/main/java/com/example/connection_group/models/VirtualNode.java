package com.example.connection_group.models;

import java.util.HashSet;
import java.util.Set;

public class VirtualNode {

  private String name;
  private String connectionGroupName;
  private String parent;
  private final Set<String> children;

  public VirtualNode() {
    this.children = new HashSet<>();
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getConnectionGroupName() {
    return connectionGroupName;
  }

  public void setConnectionGroupName(String connectionGroupName) {
    this.connectionGroupName = connectionGroupName;
  }

  public String getParent() {
    return parent;
  }

  public void setParent(String parent) {
    this.parent = parent;
  }

  public Set<String> getChildren() {
    return children;
  }

  public void addChild(String child) {
    this.children.add(child);
  }
}
