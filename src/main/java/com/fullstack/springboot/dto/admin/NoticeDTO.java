package com.fullstack.springboot.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NoticeDTO {
	
	private Long nno;
	
	private String title;
	
	private String content;
	
	private String imgPath;
	
	private boolean isPublic;
	
	private Long adno; //Admin
}
