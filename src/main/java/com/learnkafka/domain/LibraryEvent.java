package com.learnkafka.domain;

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
	private Book book;

}
