package com.fullstack.springboot.service.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.fullstack.springboot.dto.review.ReviewDTO;
import com.fullstack.springboot.entity.member.Member;
import com.fullstack.springboot.entity.product.OptionDetail;
import com.fullstack.springboot.entity.review.ProductReview;
import com.fullstack.springboot.repository.product.ReviewRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
	
	private final ReviewRepository reviewRepository;
	
	@Override
	public Page<ReviewDTO> getReview(Long pno, Pageable pageable) {
		return reviewRepository.getReview(pno, pageable);
	}
	
	@Override
	public Page<ReviewDTO> getReviewRate(int rate, Pageable pageable) {
		return reviewRepository.getReviewRate(rate, pageable);
	}
	
	@Override
	public ReviewDTO getReviewOne(Long rno) {
		return reviewRepository.getReviewOne(rno);
	}
	
	@Override
	public void register(ReviewDTO reviewDTO) {
		Member member = Member.builder().mno(reviewDTO.getMno()).build();
		
		OptionDetail optionDetail = OptionDetail.builder().odno(reviewDTO.getOdno()).build();
		
		ProductReview review = ProductReview.builder()
				.title(reviewDTO.getTitle())
				.content(reviewDTO.getContent())
				.rate(reviewDTO.getRate())
				.member(member)
				.optionDetail(optionDetail)
				.build();
		
		reviewRepository.save(review);
	}
	
	@Override
	public void changeReview(ReviewDTO reviewDTO) {
		Member member = Member.builder().mno(reviewDTO.getMno()).build();
		
		OptionDetail optionDetail = OptionDetail.builder().odno(reviewDTO.getOdno()).build();
		
		ProductReview review = ProductReview.builder()
				.rno(reviewDTO.getRno())
				.title(reviewDTO.getTitle())
				.content(reviewDTO.getContent())
				.rate(reviewDTO.getRate())
				.reviewLike(reviewDTO.getReviewLike())
				.member(member)
				.optionDetail(optionDetail)
				.build();
		
		reviewRepository.save(review);
	}
	
	@Override
	public void deleteReview(Long rno) {
		reviewRepository.deleteById(rno);
	}
	

}
