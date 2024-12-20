package com.fullstack.springboot.entity.member;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name="member_address")
public class MemberAddress{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long adno;
	
	private String addrName;
	
	private int postNum;
	
	private String addr;
	
	@ManyToOne(fetch = FetchType.LAZY)
	private Member member;
	
}