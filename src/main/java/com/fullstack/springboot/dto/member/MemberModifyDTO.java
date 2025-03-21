package com.fullstack.springboot.dto.member;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class MemberModifyDTO {

	private Long mno;
	   private String m_id;
	   //@JsonProperty("pw")
	   private String m_pw;
	   private String m_name;
	   private String m_nickname;
	   private String m_phoNum;
	   @JsonProperty("email")
	   private String m_email;
	   private String def_addr;
	   
	   @JsonFormat(pattern = "yyyy-MM-dd")  // 날짜 형식 지정
	   private LocalDate birth;
	   
	   private boolean isMan;
	   private boolean ad_agree;
	   private boolean info_agree;
	   private boolean isBan;
	   private Long totalPay = 0L;
	   private Set<Membership> membership = new HashSet<>();
}