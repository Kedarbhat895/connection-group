package com.example.connection_group.models.entity;


import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverted;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConvertedJson;
import java.util.Set;
import org.joda.time.DateTime;

@DynamoDBTable(tableName = "connection-group-audit-logs")
public class ConnectionGroupAuditLog {

  public ConnectionGroupAuditLog(String groupName, EventType eventType,
      DateTime timeStamp, GroupDetails details, String user) {
    this.groupName = groupName;
    this.sortKey = eventType.name() + "#" + timeStamp;
    this.eventType = eventType;
    this.timeStamp = timeStamp;
    this.details = details;
    this.user = user;
  }

  @DynamoDBHashKey(attributeName = "GroupName")
  private String groupName;

  @DynamoDBRangeKey
  private String sortKey;

  @DynamoDBTypeConverted(converter = EventTypeConvertor.class)
  private EventType eventType;


  @DynamoDBTypeConverted(converter = DateTimeConverter.class)
  private DateTime timeStamp;

  @DynamoDBTypeConvertedJson
  private GroupDetails details;
  private String user;

  public static class GroupDetails {

    public GroupDetails(String groupName, Set<String> nodes) {
      this.groupName = groupName;
      this.nodes = nodes;
    }

    public String getGroupName() {
      return groupName;
    }

    public void setGroupName(String groupName) {
      this.groupName = groupName;
    }

    public Set<String> getNodes() {
      return nodes;
    }

    public void setNodes(Set<String> nodes) {
      this.nodes = nodes;
    }

    private String groupName;
    private Set<String> nodes;
  }

  public String getGroupName() {
    return groupName;
  }

  public String getSortKey() {
    return sortKey;
  }

  public EventType getEventType() {
    return eventType;
  }

  public GroupDetails getDetails() {
    return details;
  }

  public DateTime getTimeStamp() {
    return timeStamp;
  }

  public String getUser() {
    return user;
  }
}
