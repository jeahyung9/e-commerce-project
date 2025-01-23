package com.fullstack.springboot.controller.review;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fullstack.springboot.dto.PageRequestDTO;
import com.fullstack.springboot.dto.PageResponseDTO;
import com.fullstack.springboot.dto.review.ReviewDTO;
import com.fullstack.springboot.service.review.ReviewService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/review")
@Log4j2
public class ReviewController {
	
	private final ReviewService reviewService;

	@GetMapping("/list/{pno}")
	public PageResponseDTO<ReviewDTO> getReview(@PathVariable("pno") Long pno, PageRequestDTO pageRequestDTO) {
		return reviewService.getReview(pno, pageRequestDTO);
	}
	
	@GetMapping("/list/rate/{pno}")
    public PageResponseDTO<ReviewDTO> getReviewRate(@PathVariable("pno") Long pno, @RequestParam("rate") int rate, PageRequestDTO pageRequestDTO) {
		return reviewService.getReviewRate(pno, rate, pageRequestDTO);
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
