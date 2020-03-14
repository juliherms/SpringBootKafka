package com.learnkafka.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * this class responsible to domain a book
 * @author j.a.vasconcelos
 *
 */

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Book {
	
	private Integer id;
	private String name;
	private String author;
}
