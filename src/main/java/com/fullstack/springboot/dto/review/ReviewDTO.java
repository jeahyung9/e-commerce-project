package com.fullstack.springboot.dto.review;

import com.fullstack.springboot.dto.CartDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDTO {
	
	private Long rno;
	private String title;
	private String content;
	private int rate;
	private Long reviewLike;
	private Long odno;
	private String od_name;
	private Long pno;
	private Long sno;
	private String businessName;
	private Long mno;
	private String m_name;

}
