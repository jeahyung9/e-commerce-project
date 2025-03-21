package com.fullstack.springboot.entity.qa;

import com.fullstack.springboot.entity.BaseEntity;
import com.fullstack.springboot.entity.seller.Seller;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
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
@Builder
@Table(name="product_QAReply")
public class ProQAReply extends BaseEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long qrno;
	
	private String qr_content;
	
	@OneToOne(fetch = FetchType.LAZY)
	private ProductQA productQA;
	
	@ManyToOne(fetch = FetchType.LAZY)
	private Seller seller;
}
