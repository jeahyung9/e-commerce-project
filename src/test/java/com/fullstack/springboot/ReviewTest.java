package com.fullstack.springboot;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;

import com.fullstack.springboot.dto.PageRequestDTO;
import com.fullstack.springboot.dto.PageResponseDTO;
import com.fullstack.springboot.dto.review.ReviewDTO;
import com.fullstack.springboot.entity.member.Member;
import com.fullstack.springboot.entity.product.OptionDetail;
import com.fullstack.springboot.entity.review.ProductReview;
import com.fullstack.springboot.repository.product.ReviewRepository;
import com.fullstack.springboot.service.product.ReviewService;

import lombok.extern.log4j.Log4j2;

@SpringBootTest
@Log4j2
public class ReviewTest {
	
	@Autowired
	private ReviewRepository reviewRepository;
	@Autowired
	private ReviewService reviewService;
	
	//service test========================================================
	//@Test
	void getOne() {
		ReviewDTO dto = reviewService.getReviewOne(1L);
		System.out.println(dto);
	}
	
	
	//@Test
	void insert() {
		for(int i = 0; i < 200; i++) {
			String str = "내용 " + i + "   ";
			String dummy = "";
			int random = (int)(Math.random() * 100);
			for(int j = 0; j < random; j++) {
				dummy += str;
			}
			
			ReviewDTO dto = ReviewDTO.builder()
					.title("타이틀 " + (i + 1))
					.content(dummy)
					.rate((int)(Math.random() * 6))
					.mno((long)(Math.random() * 90) + 1)
					.odno((long)(Math.random() * 4) + 1)
					.build();
			reviewService.register(dto);
		}
	}
	
	//@Test
	void change() {
		ReviewDTO dto2 = reviewService.getReviewOne(1L);
		
		ReviewDTO dto = ReviewDTO.builder()
				.rno(dto2.getRno())
				.title(dto2.getTitle())
				.content(dto2.getContent())
				.rate(dto2.getRate())
				.reviewLike(dto2.getReviewLike() + 1)
				.mno(dto2.getMno())
				.odno(dto2.getOdno())
				.build();
		
		reviewService.changeReview(dto);
	}
	
	//@Test
	void delete() {
		reviewService.deleteReview(7L);
	}
	
	
	//repository test----------------------------------------------------------
	
	//@Test
	void getReview() {
		//PageRequestDTO pageable = PageRequest.of(0, 100, Sort.by(Order.asc("od.od_name")));
		//PageResponseDTO<ReviewDTO> list = reviewRepository.getReview(1L, pageable);
		//for(ReviewDTO dto : list.getDtoList()) {
			//System.out.println(dto);
		//}
	}
	
	//@Test
	void insertReview() {
		ProductReview review = ProductReview.builder()
				.title("찬우는 물었다")
				.content("왈왈왈왈 개개개")
				.rate(2)
				.reviewLike(2L)
				.member(Member.builder().mno(10L).build())
				.optionDetail(OptionDetail.builder().odno(1L).build())
				.build();
		reviewRepository.save(review);
	}

}
