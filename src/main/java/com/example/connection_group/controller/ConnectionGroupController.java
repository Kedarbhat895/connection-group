package com.example.connection_group.controller;


import com.example.connection_group.models.CreateConnectionGroupRequest;
import com.example.connection_group.service.ConnectionGroupService;
import com.example.connection_group.service.ConnectionGroupServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

  private static final Logger log = LoggerFactory.getLogger(ConnectionGroupController.class);


  private final ConnectionGroupService connectionGroupService;

  @Autowired
  public ConnectionGroupController(ConnectionGroupServiceImpl connectionGroupService) {
    this.connectionGroupService = connectionGroupService;
    log.info("ConnectionGroupController initialized");  // Log controller initialization
  }

  @PostMapping()
  public ResponseEntity<String> createConnectionGroup(@RequestBody CreateConnectionGroupRequest nodeRequest) {
    log.info("Received request to create group");  // Log controller initialization
    return ResponseEntity.ok(connectionGroupService.createConnectionGroup(nodeRequest));
  }

  @GetMapping("/{vNode}")
  public ResponseEntity<String> getConnectionGroup(@PathVariable("vNode") String virtualNode) {
    return ResponseEntity.ok(connectionGroupService.getConnectionGroup(virtualNode));
  }

  @DeleteMapping("/{vNode}/{user}")
  public ResponseEntity<String> deleteNode(@PathVariable("vNode") String virtualNode, @PathVariable("user") String userName) {
    return ResponseEntity.ok(connectionGroupService.deleteNode(virtualNode, userName));
  }


}
