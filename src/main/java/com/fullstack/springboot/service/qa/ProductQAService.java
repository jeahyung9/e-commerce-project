package com.fullstack.springboot.service.qa;

import java.util.List;

import com.fullstack.springboot.dto.PageRequestDTO;
import com.fullstack.springboot.dto.PageResponseDTO;
import com.fullstack.springboot.dto.qa.ProductQADTO;

public interface ProductQAService {
    
	public PageResponseDTO<ProductQADTO> getQAList(Long pno, PageRequestDTO pageRequestDTO);
	
	public List<ProductQADTO> getQAListBymno(Long mno);
	
	public void register(ProductQADTO productQADTO);
	
	public void changeQA(ProductQADTO productQADTO);
	
	public void deleteQA(Long qno);
}
