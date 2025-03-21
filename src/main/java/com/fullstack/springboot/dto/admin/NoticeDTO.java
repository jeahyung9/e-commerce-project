package com.fullstack.springboot.dto.admin;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class NoticeDTO {
	
	private Long nno;
	
	private String title;
	
	private String content;
	
	private String imgPath;
	
	private boolean isPrivate;

	private LocalDateTime regDate;
	
	private Long adno; //Admin
}
