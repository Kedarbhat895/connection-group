package com.example.connection_group.dao;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.example.connection_group.models.entity.ConnectionGroupAuditLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class AuditDao {


  private final DynamoDBMapper dynamoDBMapper;

  @Autowired
  public AuditDao(DynamoDBMapper dynamoDBMapper) {
    this.dynamoDBMapper = dynamoDBMapper;
  }

  public String saveAudit(ConnectionGroupAuditLog audit){
    dynamoDBMapper.save(audit);
    return audit.getGroupName();
  }

}
