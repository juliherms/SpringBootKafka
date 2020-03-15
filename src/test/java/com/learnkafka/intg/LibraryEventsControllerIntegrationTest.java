package com.learnkafka.intg;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.IntegerDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.context.TestPropertySource;

import com.learnkafka.domain.Book;
import com.learnkafka.domain.LibraryEvent;

/**
 * this class responsible to test post message to kafka producer
 * using embedded kafka in this test
 * 
 * @author j.a.vasconcelos
 *
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@EmbeddedKafka(topics = { "library-events" }, partitions = 3)
@EmbeddedKafka(topics = { "library-events" })
@TestPropertySource(properties = { "spring.kafka.producer.bootstrap-servers=${spring.embedded.kafka.brokers}",
		"spring.kafka.admin.properties.bootstrap.servers=${spring.embedded.kafka.brokers}" })
public class LibraryEventsControllerIntegrationTest {

	@Autowired
	private TestRestTemplate restTemplate;
	
	@Autowired
	EmbeddedKafkaBroker embeddedKafkaBroker;
	
	private Consumer<Integer,String> consumer;
	
	@BeforeEach
	void setUp() {
		//create a config with group id and autocommited.
		Map<String,Object> configs = new HashMap<>(KafkaTestUtils.consumerProps("group1", "true", embeddedKafkaBroker));
		
		consumer = new DefaultKafkaConsumerFactory<>(configs, new IntegerDeserializer(), new StringDeserializer()).createConsumer();
		embeddedKafkaBroker.consumeFromAllEmbeddedTopics(consumer);
	}
	
	@AfterEach
	void tearDown() {
		consumer.close();
	}

	@Test
	void postLibraryEvent() {

		// given
		Book book = Book.builder().id(123).author("Dilip").name("Kafka using Spring Boot").build();
		LibraryEvent libraryEvent = LibraryEvent.builder().id(null).book(book).build();

		HttpHeaders headers = new HttpHeaders();
		headers.set("content-type", MediaType.APPLICATION_JSON.toString());
		HttpEntity<LibraryEvent> request = new HttpEntity<>(libraryEvent, headers);

		// when
		ResponseEntity<LibraryEvent> responseEntity = restTemplate.exchange("/v1/libraryevent", HttpMethod.POST,
				request, LibraryEvent.class);

		
		// then
		assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
		
		
		ConsumerRecord<Integer, String> consumerRecord =  KafkaTestUtils.getSingleRecord(consumer, "library-events");
		
		
		String expected = "{\"id\":null,\"eventType\":\"NEW\",\"book\":{\"id\":123,\"name\":\"Kafka using Spring Boot\",\"author\":\"Dilip\"}}";
		String value = consumerRecord.value();
		
		assertEquals(expected, value);
		
	}

}
