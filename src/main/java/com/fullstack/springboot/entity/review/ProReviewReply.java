package com.fullstack.springboot.entity.review;

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
@Table(name="product_reviewReply")
public class ProReviewReply extends BaseEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long rrno;
	
	private String content;
	
	@OneToOne(fetch = FetchType.LAZY)
	private ProductReview productReview;
	
	@ManyToOne(fetch = FetchType.LAZY)
	private Seller seller;
}
