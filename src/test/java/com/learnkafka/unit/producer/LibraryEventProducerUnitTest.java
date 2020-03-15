package com.learnkafka.unit.producer;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.when;

import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.TopicPartition;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;
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

	/**
	 * Test case sent consumer shows exception
	 * 
	 * @throws JsonProcessingException
	 */
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

	/**
	 * Teste case send consumer shows sucess.
	 * 
	 * @throws JsonProcessingException
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	@SuppressWarnings("rawtypes")
	@Test
	void sendLibraryEventWithoutDefaultTopicSucess()
			throws JsonProcessingException, InterruptedException, ExecutionException {

		// given
		Book book = Book.builder().id(123).author("Dilip").name("Kafka using Spring Boot").build();

		LibraryEvent libraryEvent = LibraryEvent.builder().id(null).book(book).build();
		String record = objectMapper.writeValueAsString(libraryEvent);
		SettableListenableFuture future = new SettableListenableFuture();

		ProducerRecord<Integer, String> producerRecord = new ProducerRecord("library-events", libraryEvent.getId(),
				record);
		RecordMetadata recordMetadata = new RecordMetadata(new TopicPartition("library-events", 1), 1, 1, 342,
				System.currentTimeMillis(), 1, 2);
		SendResult<Integer, String> sendResult = new SendResult<Integer, String>(producerRecord, recordMetadata);

		future.set(sendResult);
		when(kafkaTemplate.send(isA(ProducerRecord.class))).thenReturn(future);
		// when

		ListenableFuture<SendResult<Integer, String>> listenableFuture = eventProducer
				.sendLibraryEventWithoutDefaultTopic(libraryEvent);

		// then
		SendResult<Integer, String> sendResult1 = listenableFuture.get();
		assert sendResult1.getRecordMetadata().partition() == 1;
	}
}
