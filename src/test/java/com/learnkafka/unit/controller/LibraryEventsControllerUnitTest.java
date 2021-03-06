package com.learnkafka.unit.controller;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.learnkafka.controller.LibraryEventsController;
import com.learnkafka.domain.Book;
import com.learnkafka.domain.LibraryEvent;
import com.learnkafka.producer.LibraryEventProducer;

/**
 * Class responsible to unit test LibraryEventsController
 * 
 * @author j.a.vasconcelos
 *
 */
@WebMvcTest(LibraryEventsController.class)
@AutoConfigureMockMvc
public class LibraryEventsControllerUnitTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private LibraryEventProducer libraryEventProducer;

	ObjectMapper objectMapper = new ObjectMapper();

	@Test
	void postLibraryEventError() throws Exception {

		// given
		Book book = Book.builder().id(null).author("Dilip").name(null).build();
		LibraryEvent libraryEvent = LibraryEvent.builder().id(null).book(book).build();

		String json = objectMapper.writeValueAsString(libraryEvent);

		// expected
		String message = "book.id - must not be null,book.name - must not be blank";

		// When
		mockMvc.perform(post("/v1/libraryevent").content(json).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is4xxClientError()).andExpect(content().string(message));

	}

	/**
	 * Responsible to unit test method post
	 * 
	 * @throws Exception
	 */
	@Test
	void postLibraryEvent() throws Exception {

		// given
		Book book = Book.builder().id(123).author("Dilip").name("Kafka using Spring Boot").build();
		LibraryEvent libraryEvent = LibraryEvent.builder().id(null).book(book).build();

		String json = objectMapper.writeValueAsString(libraryEvent);
		when(libraryEventProducer.sendLibraryEventWithoutDefaultTopic(isA(LibraryEvent.class))).thenReturn(null);

		// When
		mockMvc.perform(post("/v1/libraryevent").content(json).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated());

	}

}
