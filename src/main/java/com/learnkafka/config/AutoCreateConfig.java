package com.learnkafka.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.config.TopicBuilder;

/**
 * This class responsible to create a topics from my application
 * this configuration is not recommended in production enviroment
 * @author j.a.vasconcelos
 *
 */
@Profile("local")
@Configuration
public class AutoCreateConfig {

	/**
	 * this method responsible to create topic libray-events
	 * @return
	 */
	@Bean
	public NewTopic libraryEvents() {
		return TopicBuilder.name("library-events")
		//.partitions(3)
		//.replicas(3)
		.build();
	}
}
