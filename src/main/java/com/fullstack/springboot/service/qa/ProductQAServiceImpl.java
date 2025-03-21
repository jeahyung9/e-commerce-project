package com.fullstack.springboot.service.qa;

import java.util.ArrayList;
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
import com.fullstack.springboot.dto.qa.ProQAReplyDTO;
import com.fullstack.springboot.dto.qa.ProductQADTO;
import com.fullstack.springboot.entity.member.Member;
import com.fullstack.springboot.entity.product.Product;
import com.fullstack.springboot.entity.qa.ProductQA;
import com.fullstack.springboot.repository.qa.ProductQAReplyRepository;
import com.fullstack.springboot.repository.qa.ProductQARepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductQAServiceImpl implements ProductQAService {
	
	private final ModelMapper modelMapper;

	private final ProductQARepository productQARepository;
	
	private final ProductQAReplyRepository productQAReplyRepository;
	
	@Override
	public PageResponseDTO<ProductQADTO> getQAList(Long pno, PageRequestDTO pageRequestDTO) {
		Pageable pageable =
				PageRequest.of(
						pageRequestDTO.getPage() - 1,
						pageRequestDTO.getSize(),
						Sort.by(pageRequestDTO.getSortBy()).descending());
		Page<ProductQA> page = productQARepository.getQAList(pno, pageable);

		long totalCount = page.getTotalElements();
		
		List<ProductQADTO> dtoList = page.getContent().stream()
				.map(productQA -> modelMapper.map(productQA, ProductQADTO.class)
						.toBuilder()
						.mno(productQA.getMember().getMno())
						.m_nickName(productQA.getMember().getM_nickName())
						.pno(productQA.getProduct().getPno())
						.build())
				.collect(Collectors.toList());
		
		List<ProductQADTO> res = new ArrayList<ProductQADTO>();
		
		for(ProductQADTO dto : dtoList) {
			ProQAReplyDTO replyDTO = productQAReplyRepository.getQAReply(dto.getQno());
			if(replyDTO != null) {
				dto = dto.toBuilder().proQAReply(replyDTO).build();
			}
			res.add(dto);
		}
		
		PageResponseDTO<ProductQADTO> responseDTO = PageResponseDTO.<ProductQADTO>withAll()
				.dtoList(res)
				.pageRequestDTO(pageRequestDTO)
				.totalCount(totalCount)
				.build();
		
		return responseDTO;
	}
	
	@Override
	public List<ProductQADTO> getQAListBymno(Long mno) {
		List<ProductQA> list = productQARepository.getQAListBymno(mno);
		System.out.println(list);
		List<ProductQADTO> dtoList = list.stream()
				.map(productQA -> modelMapper.map(productQA, ProductQADTO.class)
						.toBuilder()
						.mno(productQA.getMember().getMno())
						.pno(productQA.getProduct().getPno())
						.build())
				.collect(Collectors.toList());
		
		List<ProductQADTO> res = new ArrayList<ProductQADTO>();
		
		for(ProductQADTO dto : dtoList) {
			ProQAReplyDTO replyDTO = productQAReplyRepository.getQAReply(dto.getQno());
			if(replyDTO != null) {
				dto = dto.toBuilder().proQAReply(replyDTO).build();
			}
			res.add(dto);
		}
		return res;
	}
	
	@Override
	public void register(ProductQADTO productQADTO) {
		ProductQA productQA = ProductQA.builder()
				.title(productQADTO.getTitle())
				.content(productQADTO.getContent())
				.replyCheck(productQADTO.isReplyCheck())
				.secret(productQADTO.isSecret())
				.member(Member.builder().mno(productQADTO.getMno()).build())
				.product(Product.builder().pno(productQADTO.getPno()).build())
				.build();
		
		productQARepository.save(productQA);
	}
	
	@Override
	public void changeQA(ProductQADTO productQADTO) {
		ProductQA productQA = ProductQA.builder()
				.qno(productQADTO.getQno())
				.title(productQADTO.getTitle())
				.content(productQADTO.getContent())
				.replyCheck(productQADTO.isReplyCheck())
				.secret(productQADTO.isSecret())
				.member(Member.builder().mno(productQADTO.getMno()).build())
				.product(Product.builder().pno(productQADTO.getPno()).build())
				.build();
		
		productQARepository.save(productQA);
	}
	
	@Override
	public void deleteQA(Long qno) {
		productQARepository.deleteById(qno);
	}

}
