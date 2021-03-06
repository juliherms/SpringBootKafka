package com.learnkafka.domain;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

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
	
	@NotNull
	private Integer id;
	@NotBlank
	private String name;
	@NotBlank
	private String author;
}
