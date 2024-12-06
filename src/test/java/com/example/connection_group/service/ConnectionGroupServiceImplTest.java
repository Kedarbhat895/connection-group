package com.example.connection_group.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.example.connection_group.models.VirtualNode;
import com.example.connection_group.models.CreateConnectionGroupRequest;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ConnectionGroupServiceImplTest {

  @InjectMocks
  private ConnectionGroupServiceImpl connectionGroupServiceImpl;

  private CreateConnectionGroupRequest nodeRequest;

  @BeforeEach
  void setUp() {
    nodeRequest = new CreateConnectionGroupRequest();
    nodeRequest.setConnectionGroupName("group1");
    nodeRequest.setRootNode("rootNode");
    nodeRequest.setNodes(new HashSet<>(Arrays.asList("childNode1", "childNode2")));
    nodeRequest.setConnectedComponents(Arrays.asList(
        Arrays.asList("rootNode", "childNode1"),
        Arrays.asList("rootNode", "childNode2")
    ));
  }

  @Test
  void testCreateConnectionGroup_Success() {
    connectionGroupServiceImpl.createConnectionGroup(nodeRequest);
    connectionGroupServiceImpl.getConnectionGroupMap().forEach((key, value) -> {
      assertEquals("group1", key);
      assertEquals(3, value.getvNodes().size());
    });

    Collection<VirtualNode> virtualNodes = connectionGroupServiceImpl.getVirtualNodeMap().values();
    for (VirtualNode virtualNode : virtualNodes) {
      switch (virtualNode.getName()) {
        case "rootNode" -> {
          assertEquals("group1", virtualNode.getConnectionGroupName());
          assertEquals("rootNode", virtualNode.getName());
          assertNull(virtualNode.getParent());
          assertEquals(2, virtualNode.getChildren().size());
        }
        case "childNode1" -> {
          assertEquals("group1", virtualNode.getConnectionGroupName());
          assertEquals("childNode1", virtualNode.getName());
          assertEquals("rootNode", virtualNode.getParent());
          assertEquals(0, virtualNode.getChildren().size());
        }
        case "childNode2" -> {
          assertEquals("group1", virtualNode.getConnectionGroupName());
          assertEquals("rootNode", virtualNode.getParent());
          assertEquals(0, virtualNode.getChildren().size());
        }
        default -> throw new IllegalArgumentException("Unexpected node: " + virtualNode.getName());
      }
    }
  }

  @Test
  void testCreateConnectionGroup_DuplicateConnectionGroup() {
    connectionGroupServiceImpl.createConnectionGroup(nodeRequest);
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
      connectionGroupServiceImpl.createConnectionGroup(nodeRequest);
    });
    assertEquals("Connection Group already exists: group1", exception.getMessage());
  }

  @Test
  void testCreateConnectionGroup_DuplicateVirtualNode() {
    connectionGroupServiceImpl.createConnectionGroup(nodeRequest);
    CreateConnectionGroupRequest duplicateNodeRequest = new CreateConnectionGroupRequest();
    duplicateNodeRequest.setConnectionGroupName("group2");
    duplicateNodeRequest.setRootNode("rootNode2");
    duplicateNodeRequest.setNodes(new HashSet<>(Arrays.asList("rootNode2", "childNode1")));
    duplicateNodeRequest.setConnectedComponents(List.of(
        Arrays.asList("rootNode2", "childNode1")
    ));

    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
      connectionGroupServiceImpl.createConnectionGroup(duplicateNodeRequest);
    });
    assertEquals("Virtual Node already exists: childNode1", exception.getMessage());
  }

  @Test
  void testCreateConnectionGroup_InvalidHierarchy() {
    nodeRequest.setConnectedComponents(List.of(
        Arrays.asList("childNode1", "childNode2")
    ));

    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
      connectionGroupServiceImpl.createConnectionGroup(nodeRequest);
    });
    assertEquals("Invalid hierarchy: Node without a parent must be the root node.", exception.getMessage());
  }
  @Test
  void testDeleteNode_Success() {
    connectionGroupServiceImpl.createConnectionGroup(nodeRequest);

    connectionGroupServiceImpl.deleteNode("childNode1");

    connectionGroupServiceImpl.getConnectionGroupMap().forEach((key, value) -> {
      assertEquals("group1", key);
      assertEquals(2, value.getvNodes().size());
    });

    Collection<VirtualNode> virtualNodes = connectionGroupServiceImpl.getVirtualNodeMap().values();
    for (VirtualNode virtualNode : virtualNodes) {
      switch (virtualNode.getName()) {
        case "rootNode" -> {
          assertEquals("group1", virtualNode.getConnectionGroupName());
          assertEquals("rootNode", virtualNode.getName());
          assertNull(virtualNode.getParent());
          assertEquals(1, virtualNode.getChildren().size());
        }
        case "childNode2" -> {
          assertEquals("group1", virtualNode.getConnectionGroupName());
          assertEquals("rootNode", virtualNode.getParent());
          assertEquals(0, virtualNode.getChildren().size());
        }
        default -> throw new IllegalArgumentException("Unexpected node: " + virtualNode.getName());
      }
    }
    assertThrows(IllegalArgumentException.class, () -> connectionGroupServiceImpl.getConnectionGroup("childNode1"));
  }


  @Test
  void testDeleteNode_RootNode() {
    connectionGroupServiceImpl.createConnectionGroup(nodeRequest);

    String result = connectionGroupServiceImpl.deleteNode("rootNode");

    assertEquals(0, connectionGroupServiceImpl.getConnectionGroupMap().size());
    assertEquals(0, connectionGroupServiceImpl.getVirtualNodeMap().size());

    assertEquals("Root node deleted: Connection group and all its nodes removed successfully.", result);
    assertThrows(IllegalArgumentException.class, () -> connectionGroupServiceImpl.getConnectionGroup("rootNode"));
  }

  @Test
  void testDeleteNode_ParentNode() {
    nodeRequest.setNodes(new HashSet<>(Arrays.asList("childNode1", "childNode2", "childNode3")));
    nodeRequest.setConnectedComponents(Arrays.asList(
        Arrays.asList("rootNode", "childNode1"),
        Arrays.asList("rootNode", "childNode2"),
        Arrays.asList("childNode1", "childNode3")
    ));
    connectionGroupServiceImpl.createConnectionGroup(nodeRequest);

    //delete parent of child3
    connectionGroupServiceImpl.deleteNode("childNode1");

    connectionGroupServiceImpl.getConnectionGroupMap().forEach((key, value) -> {
      assertEquals("group1", key);
      assertEquals(3, value.getvNodes().size());
    });

    Collection<VirtualNode> virtualNodes = connectionGroupServiceImpl.getVirtualNodeMap().values();
    for (VirtualNode virtualNode : virtualNodes) {
      switch (virtualNode.getName()) {
        case "rootNode" -> {
          assertEquals("group1", virtualNode.getConnectionGroupName());
          assertEquals("rootNode", virtualNode.getName());
          assertNull(virtualNode.getParent());
          assertEquals(2, virtualNode.getChildren().size());
        }
        case "childNode2", "childNode3" -> {
          assertEquals("group1", virtualNode.getConnectionGroupName());
          assertEquals("rootNode", virtualNode.getParent()); //parent of child3 is changed to root node
          assertEquals(0, virtualNode.getChildren().size());
        }
        default -> throw new IllegalArgumentException("Unexpected node: " + virtualNode.getName());
      }
    }
    assertThrows(IllegalArgumentException.class, () -> connectionGroupServiceImpl.getConnectionGroup("childNode1"));
  }
}