package com.learnkafka.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.learnkafka.domain.LibraryEvent;
import com.learnkafka.domain.LibraryEventType;
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
	public ResponseEntity<LibraryEvent> postLibraryEvent(@RequestBody @Valid LibraryEvent libraryEvent) throws JsonProcessingException{
		
		libraryEvent.setEventType(LibraryEventType.NEW);
		//libraryEventProducer.sendLibraryEvent(libraryEvent);
		libraryEventProducer.sendLibraryEventWithoutDefaultTopic(libraryEvent);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(libraryEvent);
	}
	
	/**
	 * Responsible to update a library event and invoke kafka producer.
	 * @param libraryEvent
	 * @return
	 * @throws JsonProcessingException 
	 */
	@PutMapping("/v1/libraryevent")
	public ResponseEntity<?> putLibraryEvent(@RequestBody @Valid LibraryEvent libraryEvent) throws JsonProcessingException{
		
		if(libraryEvent.getId() == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please pass the Library Event Id");
		}
		
		libraryEvent.setEventType(LibraryEventType.UPDATE);
		libraryEventProducer.sendLibraryEventWithoutDefaultTopic(libraryEvent);
		
		return ResponseEntity.status(HttpStatus.OK).body(libraryEvent);
	}
	
	
}
