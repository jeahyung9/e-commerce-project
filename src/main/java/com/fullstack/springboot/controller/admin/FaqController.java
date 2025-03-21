package com.fullstack.springboot.controller.admin;

import java.util.Map;

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
import com.fullstack.springboot.dto.admin.FaqDTO;
import com.fullstack.springboot.service.admin.FaqService;
import com.fullstack.springboot.util.ConvertBase64Util;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RestController
@RequestMapping("/api/faq")
@RequiredArgsConstructor
@Log4j2
public class FaqController {
	
	private final FaqService faqService;
	
	@GetMapping("/{encodedId}")
	public FaqDTO readFAQ(@PathVariable("encodedId") String encodedId) {
		
		 Long fno = ConvertBase64Util.decoding(encodedId);

		return faqService.readFAQ(fno);
	}
	
	@GetMapping("/list")
	public PageResponseDTO<FaqDTO> list(PageRequestDTO pageRequestDTO,
			@RequestParam(value = "sort", required = false, defaultValue = "fno") String sort,
			@RequestParam(value = "adno", required = false) Long adno,
			@RequestParam(value = "order", required = false) boolean order){
		log.error(pageRequestDTO);
		
		return faqService.faqList(pageRequestDTO, sort, adno, order);
	}
	
	@PostMapping("/")
	public Map<String, Long> register(@RequestBody FaqDTO dto){
		log.error("FaqDTO : " + dto);
		
		Long fno = faqService.registerFAQ(dto);
		
		return Map.of("FNO", fno);
	}
	
	@PutMapping("/{encodedId}")
	public Map<String, String> modifyFAQ(
			@PathVariable("encodedId") String encodedId,
			@RequestBody FaqDTO faqDTO){
		
		Long fno = ConvertBase64Util.decoding(encodedId);
		
		faqDTO.setFno(fno);
		
		log.error("FAQ : " + faqDTO);
		
		faqService.modifyFAQ(faqDTO);
		
		return Map.of("RESULT","SUCCESS");
	}
	
	@DeleteMapping("/{encodedId}")
	public Map<String, String> deleteFAQ(@PathVariable("encodedId") String encodedId){
		
		Long fno = ConvertBase64Util.decoding(encodedId);
		
		log.error("Delete : " + fno);
		
		faqService.deleteFAQ(fno);
		
		return Map.of("RESULT","SUCCESS"); 
	}
}
