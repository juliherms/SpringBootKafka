package com.learnkafka.unit;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

	/**
	 * Responsible to unit test method post
	 * @throws Exception
	 */
	@Test
	void postLibraryEvent() throws Exception {

		// given
		Book book = Book.builder().id(123).author("Dilip").name("Kafka using Spring Boot").build();
		LibraryEvent libraryEvent = LibraryEvent.builder().id(null).book(book).build();
		
		String json = objectMapper.writeValueAsString(libraryEvent);
		
		//When
		mockMvc.perform(post("/v1/libraryevent")
				.content(json)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated());
		
	}

}
