package com.fullstack.springboot.service.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.fullstack.springboot.dto.review.ReviewDTO;

public interface ReviewService {
	
	public Page<ReviewDTO> getReview(Long pno, Pageable pageable);
	
	public Page<ReviewDTO> getReviewRate(int rate, Pageable pageable);
	
	public ReviewDTO getReviewOne(Long rno);
	
	public void register(ReviewDTO reviewDTO);
	
	public void changeReview(ReviewDTO reviewDTO);
	
	public void deleteReview(Long rno);

}
