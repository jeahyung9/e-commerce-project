package com.fullstack.springboot.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class FaqDTO {
	
	private Long fno;
	
	private String question;
	
	private String answer;
	
	private boolean isPrivate; //공개 여부
	
	private Long adno; //Admin
}
