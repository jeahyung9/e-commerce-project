package com.fullstack.springboot.controller.qa;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fullstack.springboot.dto.PageRequestDTO;
import com.fullstack.springboot.dto.PageResponseDTO;
import com.fullstack.springboot.dto.qa.ProQAReplyDTO;
import com.fullstack.springboot.dto.qa.ProductQADTO;
import com.fullstack.springboot.service.qa.ProductQAReplyService;
import com.fullstack.springboot.service.qa.ProductQAService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/qa")
@Log4j2
public class ProductQAController {

	private final ProductQAService productQAService;
	
	private final ProductQAReplyService productQAReplyService;
	
	@GetMapping("/list/{pno}")
	public PageResponseDTO<ProductQADTO> getQAList(@PathVariable("pno") Long pno, PageRequestDTO pageRequestDTO) {
		return productQAService.getQAList(pno, pageRequestDTO);
	}
	
	@GetMapping("/list/my/{mno}")
	public List<ProductQADTO> getQAListBymno(@PathVariable("mno") Long mno) {
		return productQAService.getQAListBymno(mno);
	}
	
	@PostMapping("/add")
	public void addQA(@RequestBody ProductQADTO productQADTO) {
		productQAService.register(productQADTO);
	}
	
	@PostMapping("/add/reply")
	public void addQAReply(@RequestBody ProQAReplyDTO proQAReplyDTO) {
		productQAReplyService.register(proQAReplyDTO);
	}
	
	@PutMapping("/change")
	public void changeQA(@RequestBody ProductQADTO productQADTO) {
		productQAService.changeQA(productQADTO);
	}
	
	@PutMapping("/change/reply")
	public void changeQA(@RequestBody ProQAReplyDTO proQAReplyDTO) {
		productQAReplyService.register(proQAReplyDTO);
	}
	
	@DeleteMapping("/delete/{qno}")
	public void deleteQA(@PathVariable("qno") Long qno) {
		productQAService.deleteQA(qno);
	}
	
	@DeleteMapping("/delete/reply/{qrno}")
	public void deleteQAReply(@PathVariable("qrno") Long qrno) {
		productQAReplyService.deleteQAReply(qrno);
	}
	
}
