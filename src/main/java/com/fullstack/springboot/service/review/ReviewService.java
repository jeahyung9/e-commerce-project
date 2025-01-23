package com.fullstack.springboot.service.review;

import com.fullstack.springboot.dto.PageRequestDTO;
import com.fullstack.springboot.dto.PageResponseDTO;
import com.fullstack.springboot.dto.review.ReviewDTO;

public interface ReviewService {
	
	public PageResponseDTO<ReviewDTO> getReview(Long pno, PageRequestDTO pageRequestDTO);
	
	public PageResponseDTO<ReviewDTO> getReviewRate(Long pno, int rate, PageRequestDTO pageRequestDTO);
	
	public ReviewDTO getReviewOne(Long rno);
	
	public void register(ReviewDTO reviewDTO);
	
	public void changeReview(ReviewDTO reviewDTO);
	
	public void deleteReview(Long rno);

}
