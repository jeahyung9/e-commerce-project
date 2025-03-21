package com.fullstack.springboot.service.review;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.fullstack.springboot.dto.PageRequestDTO;
import com.fullstack.springboot.dto.PageResponseDTO;
import com.fullstack.springboot.dto.review.ReviewDTO;
import com.fullstack.springboot.entity.member.Member;
import com.fullstack.springboot.entity.order.OrderDetail;
import com.fullstack.springboot.entity.product.OptionDetail;
import com.fullstack.springboot.entity.review.ProductReview;
import com.fullstack.springboot.repository.order.OrderDetailRepository;
import com.fullstack.springboot.repository.review.ReviewRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
	
	private final ModelMapper modelMapper;
	
	private final ReviewRepository reviewRepository;
	
	private final OrderDetailRepository orderDetailRepository;
	
	@Override
	public PageResponseDTO<ReviewDTO> getReview(Long pno, PageRequestDTO pageRequestDTO) {
		String sortBy = pageRequestDTO.getSortBy();
		
		if(sortBy == null) {
			sortBy = "reviewLike";
		}

		Pageable pageable =
				PageRequest.of(
						pageRequestDTO.getPage() - 1,
						pageRequestDTO.getSize(),
						Sort.by(sortBy).descending());
		Page<ReviewDTO> page = reviewRepository.getReview(pno, pageable);
		
		List<ReviewDTO> dtoList = page.getContent().stream()
			      .map(review -> modelMapper.map(review, ReviewDTO.class))
			      .collect(Collectors.toList());
		
		long totalCount = page.getTotalElements();
		
		PageResponseDTO<ReviewDTO> responseDTO = PageResponseDTO.<ReviewDTO>withAll()
			      .dtoList(dtoList)
			      .pageRequestDTO(pageRequestDTO)
			      .totalCount(totalCount)
			      .build();
		
		return responseDTO;
	}
	
	@Override
	public PageResponseDTO<ReviewDTO> getReviewRate(Long pno, int rate, PageRequestDTO pageRequestDTO) {
		Pageable pageable =
				PageRequest.of(
						pageRequestDTO.getPage() - 1,
						pageRequestDTO.getSize(),
						Sort.by(pageRequestDTO.getSortBy()).descending());
		Page<ReviewDTO> page = reviewRepository.getReviewRate(pno, rate, pageable);
		
		List<ReviewDTO> dtoList = page.getContent().stream()
			      .map(review -> modelMapper.map(review, ReviewDTO.class))
			      .collect(Collectors.toList());
		
		long totalCount = page.getTotalElements();
		
		PageResponseDTO<ReviewDTO> responseDTO = PageResponseDTO.<ReviewDTO>withAll()
			      .dtoList(dtoList)
			      .pageRequestDTO(pageRequestDTO)
			      .totalCount(totalCount)
			      .build();
		
		return responseDTO;
	}
	
	@Override
	public ReviewDTO getReviewOne(Long rno) {
		return reviewRepository.getReviewOne(rno);
	}
	
	@Override
	public int getReviewCnt(Long pno) {
		return reviewRepository.getReviewCnt(pno);
	}
	
	@Override
	public void register(Long orno, ReviewDTO reviewDTO) {
		OrderDetail orderDetail = orderDetailRepository.getOrderDetailOne(orno);
		
		if(orderDetail == null || orderDetail.getRno() != null) {
			return;
		}
		
		Member member = Member.builder().mno(reviewDTO.getMno()).build();
		
		OptionDetail optionDetail = OptionDetail.builder().odno(reviewDTO.getOdno()).build();
		
		ProductReview review = ProductReview.builder()
				.title(reviewDTO.getTitle())
				.content(reviewDTO.getContent())
				.rate(reviewDTO.getRate())
				.member(member)
				.optionDetail(optionDetail)
				.build();
		
		ProductReview rev = reviewRepository.save(review);
		
		orderDetail.setRno(rev.getRno());
		
		orderDetailRepository.save(orderDetail);
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
