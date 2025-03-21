package com.fullstack.springboot.service.admin;

import com.fullstack.springboot.dto.PageRequestDTO;
import com.fullstack.springboot.dto.PageResponseDTO;
import com.fullstack.springboot.dto.admin.FaqDTO;
import com.fullstack.springboot.entity.admin.Admin;
import com.fullstack.springboot.entity.admin.Faq;

public interface FaqService {
	
	Long registerFAQ(FaqDTO faqDTO);
	
	FaqDTO readFAQ(Long adno);
	
	void deleteFAQ(Long fno);
	
	void modifyFAQ(FaqDTO faqDTO);
	
	PageResponseDTO<FaqDTO> faqList(PageRequestDTO pageRequestDTO, String sort, Long adno, boolean order);
	
	default Faq dtoToEntity(FaqDTO dto) {
		Admin admin = Admin.builder().adno(dto.getAdno()).build();
		
		Faq faq = Faq.builder()
				.fno(dto.getFno())
				.question(dto.getQuestion())
				.answer(dto.getAnswer())
				.admin(admin)
				.build();
		
		return faq;
	}
	
	default FaqDTO entityToDTO(Faq faq, Admin admin) {
		FaqDTO faqDTO = FaqDTO.builder()
				.fno(faq.getFno())
				.question(faq.getQuestion())
				.answer(faq.getAnswer())
				.adno(admin.getAdno())
				.build();
		
		return faqDTO;
	}
}
