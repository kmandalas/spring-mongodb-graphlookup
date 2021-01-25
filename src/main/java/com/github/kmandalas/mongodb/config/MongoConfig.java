package com.github.kmandalas.mongodb.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoTransactionManager;

@Configuration
public class MongoConfig {

	@Bean
	MongoTransactionManager transactionManager(MongoDatabaseFactory dbFactory) {

		return new MongoTransactionManager(dbFactory);
	}

}
