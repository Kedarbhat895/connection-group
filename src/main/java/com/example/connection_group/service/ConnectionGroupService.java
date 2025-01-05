package com.example.connection_group.service;

import com.example.connection_group.models.CreateConnectionGroupRequest;

public interface ConnectionGroupService {

  String createConnectionGroup(CreateConnectionGroupRequest request);

  String deleteNode(String nodeName, String userName);

  String getConnectionGroup(String nodeName);
}
