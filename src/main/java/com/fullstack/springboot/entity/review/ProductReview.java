package com.fullstack.springboot.entity.review;

import org.hibernate.annotations.ColumnDefault;

import com.fullstack.springboot.entity.BaseEntity;
import com.fullstack.springboot.entity.member.Member;
import com.fullstack.springboot.entity.product.OptionDetail;
import com.fullstack.springboot.entity.product.Product;

import jakarta.persistence.Column;
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
@Builder
@Table(name="product_review")
public class ProductReview extends BaseEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long rno;
	
	private String title;
	
	private String content;
	
	private int rate; // 별점 (구매자가 다는 리뷰)
	
	@Column(nullable = false)
	@Builder.Default
	private Long reviewLike = 0L; // 다른 멤버가 눌러주는 추천
	
	@ManyToOne(fetch = FetchType.LAZY)
	private Member member;
	
	@ManyToOne(fetch = FetchType.LAZY)
	private OptionDetail optionDetail;
}
