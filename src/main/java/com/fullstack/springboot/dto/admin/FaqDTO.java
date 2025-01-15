package com.fullstack.springboot.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FaqDTO {
	
	private Long fno;
	
	private String question;
	
	private String answer;
	
	private Long adno; //Admin
}
