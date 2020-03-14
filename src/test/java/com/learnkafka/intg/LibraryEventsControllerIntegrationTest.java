package com.learnkafka.intg;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

import com.learnkafka.domain.Book;
import com.learnkafka.domain.LibraryEvent;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LibraryEventsControllerIntegrationTest {

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	void postLibraryEvent() {
		
		//given
		
		Book book = Book.builder()
				.id(123)
				.author("Dilip")
				.name("Kafka using Spring Boot")
				.build();
		
		LibraryEvent libraryEvent = LibraryEvent.builder()
				.id(null)
				.book(book)
				.build();
		
		HttpHeaders headers = new HttpHeaders();
		headers.set("content-type",MediaType.APPLICATION_JSON.toString());
		HttpEntity<LibraryEvent> request = new HttpEntity<>(libraryEvent,headers);
		
		//when
		ResponseEntity<LibraryEvent> responseEntity = restTemplate.exchange("/v1/libraryevent",HttpMethod.POST,request,LibraryEvent.class);
		
		System.out.print(responseEntity.getStatusCode());
		//then
		assertEquals(HttpStatus.CREATED,responseEntity.getStatusCode());
	}

}
