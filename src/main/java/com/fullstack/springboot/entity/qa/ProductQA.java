package com.fullstack.springboot.entity.qa;



import com.fullstack.springboot.entity.BaseEntity;
import com.fullstack.springboot.entity.member.Member;
import com.fullstack.springboot.entity.product.Product;

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
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Table(name="product_QA")
public class ProductQA extends BaseEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long qno;
	
	private String title;
	
	private String content;

	private boolean replyCheck;

	private boolean secret;
	
	@ManyToOne(fetch = FetchType.LAZY)
	private Member member;
	
	@ManyToOne(fetch = FetchType.LAZY)
	private Product product;
}
