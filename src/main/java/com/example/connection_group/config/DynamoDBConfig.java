package com.example.connection_group.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import org.socialsignin.spring.data.dynamodb.repository.config.EnableDynamoDBRepositories;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableDynamoDBRepositories
public class DynamoDBConfig {

  @Value("${AWS_DB_ENDPOINT}")
  private String amazonDynamoDBEndpoint;

  @Value("${AWS_ACCESS_KEY_ID}")
  private String amazonAWSAccessKey;

  @Value("${AWS_SECRET_ACCESS_KEY}")
  private String amazonAWSSecretKey;

  @Value("${AWS_REGION}")
  private String awsRegion;

  @Bean
  public DynamoDBMapper dynamoDBMapper() {
    return new DynamoDBMapper(buildAmazonDynamoDB());
  }

  @Bean
  public AmazonDynamoDB buildAmazonDynamoDB() {
    return AmazonDynamoDBClient.builder()
        .withEndpointConfiguration(
            new AwsClientBuilder.EndpointConfiguration(amazonDynamoDBEndpoint, awsRegion))
        .withCredentials(new AWSStaticCredentialsProvider(
            new BasicAWSCredentials(amazonAWSAccessKey, amazonAWSSecretKey)))
        .build();
  }
}

