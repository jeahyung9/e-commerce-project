package com.fullstack.springboot.repository.qa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.fullstack.springboot.dto.qa.ProQAReplyDTO;
import com.fullstack.springboot.entity.qa.ProQAReply;

public interface ProductQAReplyRepository extends JpaRepository<ProQAReply, Long> {
	
	@Query("select " +
			"new com.fullstack.springboot.dto.qa.ProQAReplyDTO(qr.qrno, q.qno, s.sno, qr.qr_content, qr.regDate) from " +
			"ProQAReply qr left join ProductQA q on qr.productQA = q " +
			"left join Seller s on qr.seller = s " +
			"where qr.productQA.qno = :qno")
	ProQAReplyDTO getQAReply(@Param("qno") Long qno);
	
}
