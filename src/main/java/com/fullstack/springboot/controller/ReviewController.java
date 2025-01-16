package com.fullstack.springboot.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fullstack.springboot.dto.review.ReviewDTO;
import com.fullstack.springboot.service.product.ReviewService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RestController
@RequiredArgsConstructor
@RequestMapping("/review")
@Log4j2
public class ReviewController {
	
	private final ReviewService reviewService;

	@GetMapping("/list/{pno}/{sort}")
	public Page<ReviewDTO> getReview(@PathVariable("pno") Long pno, @PathVariable("sort") String sort) {
		Pageable pageable = PageRequest.of(0, 10, Sort.by(Order.desc(sort)));
		return reviewService.getReview(pno, pageable);
	}
	
	@GetMapping("/list/rate/{rate}/{sort}")
    public Page<ReviewDTO> getReviewRate(@PathVariable("rate") int rate, @PathVariable("sort") String sort) {
		Pageable pageable = PageRequest.of(0, 10, Sort.by(Order.desc(sort)));
		return reviewService.getReviewRate(rate, pageable);
	}
	
	@PostMapping("/add")
	public void addReview(@RequestBody ReviewDTO reviewDTO) {
		reviewService.register(reviewDTO);
	}
	
	@PutMapping("/change")
	public void changeReview(@RequestBody ReviewDTO reviewDTO) {
		reviewService.changeReview(reviewDTO);
	}
	
	@DeleteMapping("/delete/{rno}")
	public void deleteReview(@PathVariable("rno") Long rno) {
		reviewService.deleteReview(rno);
	}
	
}
