package com.github.kmandalas.mongodb.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "com.github.kmandalas.mongodb.repository")
public class MongoConfig extends AbstractMongoClientConfiguration {

//    @Value("${mongo.db.name}")
//    private String dbName;
//
//    @Value("${mongo.db.host}")
//    private String dbHost;
//
//    @Value("${mongo.db.port}")
//    private String dbPort;

    @Bean
    MongoTransactionManager transactionManager(MongoDatabaseFactory dbFactory) {
        return new MongoTransactionManager(dbFactory);
    }

    @Override
    protected String getDatabaseName() {
        return "test";
    }

    @Override
    public MongoClient mongoClient() {
        String connectionStr = "mongodb://mongo1,mongo2,mongo3/test?replicaSet=rs0";
        final ConnectionString connectionString = new ConnectionString(connectionStr);
        final MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .build();
        return MongoClients.create(mongoClientSettings);
    }
}
