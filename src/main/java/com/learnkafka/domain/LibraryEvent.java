package com.learnkafka.domain;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * this class responsible to domain a library event
 * 
 * @author j.a.vasconcelos
 *
 */

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class LibraryEvent {

	private Integer id;
	private LibraryEventType eventType;
	@Valid
	@NotNull
	private Book book;

}
