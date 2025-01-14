package com.fullstack.springboot.entity.product;

import com.fullstack.springboot.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@Table(name = "seller")
public class Seller extends BaseEntity{

	//댓글번호, 댓글내용, 작성자를 필드로 추가
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long sno;
	
	@Column(nullable = false)
	private String s_id;
	
	@Column(nullable = false)
	private String s_pw;
	
	@Column(nullable = false)
	private String businessName;
	
	@Column(nullable = false)
	private String businessNum;
	
	private int s_num;
	
	private String s_email;
	
	private String s_addr;
	
	@Builder.Default
	private Long totalSales = 0L;
	
}
