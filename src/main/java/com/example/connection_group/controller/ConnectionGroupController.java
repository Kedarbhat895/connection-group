package com.example.connection_group.controller;


import com.example.connection_group.models.CreateConnectionGroupRequest;
import com.example.connection_group.service.ConnectionGroupService;
import com.example.connection_group.service.ConnectionGroupServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/connection-group")
public class ConnectionGroupController {

  private final ConnectionGroupService connectionGroupService;

  @Autowired
  public ConnectionGroupController(ConnectionGroupServiceImpl connectionGroupService) {
    this.connectionGroupService = connectionGroupService;
  }

  @PostMapping()
  public ResponseEntity<String> createConnectionGroup(@RequestBody CreateConnectionGroupRequest nodeRequest) {
    return ResponseEntity.ok(connectionGroupService.createConnectionGroup(nodeRequest));
  }

  @GetMapping("/{vNode}")
  public ResponseEntity<String> getConnectionGroup(@PathVariable("vNode") String virtualNode) {
    return ResponseEntity.ok(connectionGroupService.getConnectionGroup(virtualNode));
  }

  @DeleteMapping("/{vNode}")
  public ResponseEntity<String> deleteNode(@PathVariable("vNode") String virtualNode) {
    return ResponseEntity.ok(connectionGroupService.deleteNode(virtualNode));
  }


}
