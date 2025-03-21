package com.fullstack.springboot.service.qa;

import org.springframework.stereotype.Service;

import com.fullstack.springboot.dto.qa.ProQAReplyDTO;
import com.fullstack.springboot.entity.qa.ProQAReply;
import com.fullstack.springboot.entity.qa.ProductQA;
import com.fullstack.springboot.entity.seller.Seller;
import com.fullstack.springboot.repository.qa.ProductQAReplyRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductQAReplyServiceImpl implements ProductQAReplyService {
	
	private final ProductQAReplyRepository productQAReplyRepository;

	@Override
	public void register(ProQAReplyDTO proQAReplyDTO) {
		try {
			ProQAReply proQAReply = 
					ProQAReply.builder()
					.qr_content(proQAReplyDTO.getQr_content())
					.productQA(ProductQA.builder().qno(proQAReplyDTO.getQno()).build())
					.seller(Seller.builder().sno(proQAReplyDTO.getSno()).build())
					.build();
			productQAReplyRepository.save(proQAReply);
		}catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void changeQAReply(ProQAReplyDTO proQAReplyDTO) {
		try {
			ProQAReply proQAReply = 
					ProQAReply.builder()
					.qrno(proQAReplyDTO.getQrno())
					.qr_content(proQAReplyDTO.getQr_content())
					.productQA(ProductQA.builder().qno(proQAReplyDTO.getQno()).build())
					.seller(Seller.builder().sno(proQAReplyDTO.getSno()).build())
					.build();
			productQAReplyRepository.save(proQAReply);
		}catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void deleteQAReply(Long qrno) {
		try {
			productQAReplyRepository.deleteById(qrno);			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	
}
