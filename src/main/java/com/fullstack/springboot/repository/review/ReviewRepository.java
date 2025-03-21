package com.fullstack.springboot.repository.review;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.fullstack.springboot.dto.review.ReviewDTO;
import com.fullstack.springboot.entity.review.ProductReview;

public interface ReviewRepository extends JpaRepository<ProductReview, Long> {

	@Query("select " +
			"new com.fullstack.springboot.dto.review.ReviewDTO(r.rno, r.title, r.content, r.rate, r.reviewLike, r.regDate, od.odno, od.od_name, p.pno, s.sno, s.businessName, m.mno, m.m_name) from " +
			"ProductReview r inner join OptionDetail od on r.optionDetail = od " +
			"left join Product p on od.product = p " +
			"left join Seller s on p.seller = s " +
			"left join Member m on r.member = m " +
			"where p.pno = :pno")
	Page<ReviewDTO> getReview(@Param("pno") Long pno, Pageable pageable);
	
	@Query("select " +
			"new com.fullstack.springboot.dto.review.ReviewDTO(r.rno, r.title, r.content, r.rate, r.reviewLike, r.regDate, od.odno, od.od_name, p.pno, s.sno, s.businessName, m.mno, m.m_name) from " +
			"ProductReview r inner join OptionDetail od on r.optionDetail = od " +
			"left join Product p on od.product = p " +
			"left join Seller s on p.seller = s " +
			"left join Member m on r.member = m " +
			"where p.pno = :pno and r.rate = :rate")
	Page<ReviewDTO> getReviewRate(@Param("pno") Long pno, @Param("rate") int rate, Pageable pageable);
	
	@Query("select " +
			"new com.fullstack.springboot.dto.review.ReviewDTO(r.rno, r.title, r.content, r.rate, r.reviewLike, r.regDate, od.odno, od.od_name, p.pno, s.sno, s.businessName, m.mno, m.m_name) from " +
			"ProductReview r inner join OptionDetail od on r.optionDetail = od " +
			"left join Product p on od.product = p " +
			"left join Seller s on p.seller = s " +
			"left join Member m on r.member = m " +
			"where r.rno = :rno")
	ReviewDTO getReviewOne(@Param("rno") Long rno);
	
	@Query("select count(r) from " +
			"ProductReview r inner join OptionDetail od on r.optionDetail = od " +
			"left join Product p on od.product = p " +
			"where p.pno = :pno")
	int getReviewCnt(@Param("pno") Long pno);
	
}
