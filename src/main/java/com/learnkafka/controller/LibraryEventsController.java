package com.learnkafka.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.learnkafka.domain.LibraryEvent;
import com.learnkafka.producer.LibraryEventProducer;

/**
 * this class responsible to controller all events for books. 
 * @author j.a.vasconcelos
 *
 */
@RestController
public class LibraryEventsController {
	
	@Autowired
	private LibraryEventProducer libraryEventProducer;

	/**
	 * Responsible to create a library event and invoke kafka producer.
	 * @param libraryEvent
	 * @return
	 * @throws JsonProcessingException 
	 */
	@PostMapping("/v1/libraryevent")
	public ResponseEntity<LibraryEvent> postLibraryEvent(@RequestBody LibraryEvent libraryEvent) throws JsonProcessingException{
		
		libraryEventProducer.sendLibraryEvent(libraryEvent);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(libraryEvent);
	}
}
