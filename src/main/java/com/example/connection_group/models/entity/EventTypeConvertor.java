package com.example.connection_group.models.entity;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;

public class EventTypeConvertor implements DynamoDBTypeConverter<String, EventType> {
  @Override
  public String convert(EventType object) {
    return object.name();
  }

  @Override
  public EventType unconvert(String object) {
    return EventType.valueOf(object);
  }
}