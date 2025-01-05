package com.example.connection_group.models.entity;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;
import org.joda.time.DateTime;

public class DateTimeConverter implements DynamoDBTypeConverter<String, DateTime> {

  @Override
  public String convert(DateTime object) {
    // Converts DateTime to a String in ISO 8601 format
    return object.toString();
  }

  @Override
  public DateTime unconvert(String object) {
    // Converts a String in ISO 8601 format back to DateTime
    return DateTime.parse(object);
  }
}

