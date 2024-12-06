package com.example.connection_group.service;

import com.example.connection_group.models.ConnectionGroup;
import com.example.connection_group.models.VirtualNode;
import com.example.connection_group.models.CreateConnectionGroupRequest;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.springframework.stereotype.Service;

@Service
public class ConnectionGroupServiceImpl implements ConnectionGroupService {

  public ConcurrentMap<String, ConnectionGroup> getConnectionGroupMap() {
    return connectionGroupMap;
  }

  public ConcurrentMap<String, VirtualNode> getVirtualNodeMap() {
    return virtualNodeMap;
  }

  private final ConcurrentMap<String, ConnectionGroup> connectionGroupMap;
  private final ConcurrentMap<String, VirtualNode> virtualNodeMap;

  public ConnectionGroupServiceImpl() {
    this.connectionGroupMap = new ConcurrentHashMap<>();
    this.virtualNodeMap = new ConcurrentHashMap<>();
  }

  public String createConnectionGroup(CreateConnectionGroupRequest nodeRequest) {
    if (connectionGroupMap.containsKey(nodeRequest.getConnectionGroupName())) {
      throw new IllegalArgumentException(
          "Connection Group already exists: " + nodeRequest.getConnectionGroupName());
    }

    if (nodeRequest.getNodes().contains(nodeRequest.getRootNode())) {
      throw new IllegalArgumentException("Root node must not be present in the nodes list.");
    }

    if (virtualNodeMap.containsKey(nodeRequest.getRootNode())) {
      throw new IllegalArgumentException("Root Node already exists: " + nodeRequest.getRootNode());
    }
    for (String vNode : nodeRequest.getNodes()) {
      if (virtualNodeMap.containsKey(vNode)) {
        throw new IllegalArgumentException("Virtual Node already exists: " + vNode);
      }
    }

    // Preprocess the edges into a parent map
    Map<String, String> parentMap = preprocessParentMap(nodeRequest.getConnectedComponents());
    ConnectionGroup connectionGroup = new ConnectionGroup();
    connectionGroup.setName(nodeRequest.getConnectionGroupName());
    Set<VirtualNode> vNodes = new HashSet<>();

    VirtualNode parentNode = createVirtualNode(nodeRequest.getConnectionGroupName(),
        nodeRequest.getRootNode(), null);
    virtualNodeMap.put(parentNode.getName(), parentNode);
    vNodes.add(parentNode);

    // Create nodes with hierarchical relationships
    for (String vNode : nodeRequest.getNodes()) {
      String parent = findParent(parentMap, vNode);
      if (parent == null && !Objects.equals(vNode, nodeRequest.getRootNode())) {
        throw new IllegalArgumentException(
            "Invalid hierarchy: Node without a parent must be the root node.");
      }
      VirtualNode newNode = createVirtualNode(nodeRequest.getConnectionGroupName(), vNode,
          parent);
      virtualNodeMap.put(newNode.getName(), newNode);
      vNodes.add(newNode);
    }

    connectionGroup.setVNodes(vNodes);
    connectionGroupMap.put(connectionGroup.getName(), connectionGroup);
    return "Created Successfully";
  }

  public String getConnectionGroup(String vNode) {
    if (!virtualNodeMap.containsKey(vNode)) {
      throw new IllegalArgumentException("virtualNode does not exist");
    }

    return virtualNodeMap.get(vNode).getConnectionGroupName();
  }


  public String deleteNode(String nodeName) {
    if (!virtualNodeMap.containsKey(nodeName)) {
      throw new IllegalArgumentException("Node does not exist");
    }

    VirtualNode node = virtualNodeMap.get(nodeName);
    String connectionGroupName = node.getConnectionGroupName();
    ConnectionGroup connectionGroup = connectionGroupMap.get(connectionGroupName);

    // Check if the node is the root node
    if (node.getParent() == null) {
      // Root node deletion: delete the entire connection group
      for (VirtualNode vNode : connectionGroup.getvNodes()) {
        virtualNodeMap.remove(vNode.getName());
      }
      connectionGroupMap.remove(connectionGroupName);
      return "Root node deleted: Connection group and all its nodes removed successfully.";
    }

    // Reassign children to the node's parent
    String parentName = node.getParent();
    if (parentName != null && virtualNodeMap.containsKey(parentName)) {
      VirtualNode parent = virtualNodeMap.get(parentName);
      parent.getChildren().remove(nodeName); // Remove the deleted node from parent's children
      parent.getChildren().addAll(node.getChildren()); // Reassign children to the parent
    }

    // Update children's parent to the node's parent
    for (String childName : node.getChildren()) {
      if (virtualNodeMap.containsKey(childName)) {
        VirtualNode child = virtualNodeMap.get(childName);
        child.setParent(parentName);
      }
    }
    // Remove the node
    connectionGroup.getvNodes().remove(node);
    virtualNodeMap.remove(nodeName);
    return "Node deleted successfully.";
  }


  private VirtualNode createVirtualNode(String connectionGroupId, String nodeName,
      String parentNode) {
    VirtualNode virtualNode = new VirtualNode();

    virtualNode.setConnectionGroupName(connectionGroupId);
    virtualNode.setName(nodeName);
    virtualNode.setParent(parentNode);

    // If there's a parent, add this node as a child
    if (parentNode != null && virtualNodeMap.containsKey(parentNode)) {
      VirtualNode parent = virtualNodeMap.get(parentNode);
      parent.addChild(nodeName);
    }

    return virtualNode;
  }

  private String findParent(Map<String, String> parentMap, String nodeName) {
    return parentMap.getOrDefault(nodeName, null);
  }

  //Map containing {child: parent} pairs
  private Map<String, String> preprocessParentMap(List<List<String>> edges) {
    Map<String, String> parentMap = new HashMap<>();
    for (List<String> edge : edges) {
      if (edge.size() != 2) {
        throw new IllegalArgumentException(
            "Invalid edge format. Each edge must contain exactly 2 nodes.");
      }
      String parent = edge.get(0);
      String child = edge.get(1);

      if (parentMap.containsKey(child)) {
        throw new IllegalArgumentException(
            "Invalid hierarchy: A node cannot have multiple parents.");
      }

      parentMap.put(child, parent);
    }
    return parentMap;
  }

}
