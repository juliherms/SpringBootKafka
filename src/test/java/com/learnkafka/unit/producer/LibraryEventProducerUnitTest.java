package com.learnkafka.unit.producer;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.when;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.util.concurrent.SettableListenableFuture;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.learnkafka.domain.Book;
import com.learnkafka.domain.LibraryEvent;
import com.learnkafka.producer.LibraryEventProducer;

/**
 * Class responsible to unit test kafka consumer.
 * 
 * @author j.a.vasconcelos
 *
 */
@ExtendWith(MockitoExtension.class)
public class LibraryEventProducerUnitTest {

	@Mock
	private KafkaTemplate<Integer, String> kafkaTemplate;

	@Spy
	ObjectMapper objectMapper = new ObjectMapper();

	@InjectMocks
	private LibraryEventProducer eventProducer;

	@SuppressWarnings("rawtypes")
	@Test
	void sendLibraryEventWithoutDefaultTopicFailure() throws JsonProcessingException {

		// given
		Book book = Book.builder().id(123).author("Dilip").name("Kafka using Spring Boot").build();
		LibraryEvent libraryEvent = LibraryEvent.builder().id(null).book(book).build();

		SettableListenableFuture future = new SettableListenableFuture();

		future.setException(new RuntimeException("Exception Calling Kafka"));
		when(kafkaTemplate.send(isA(ProducerRecord.class))).thenReturn(future);

		// when
		assertThrows(Exception.class, () -> eventProducer.sendLibraryEventWithoutDefaultTopic(libraryEvent).get());
		// then
	}
}
