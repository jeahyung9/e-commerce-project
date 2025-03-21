package com.fullstack.springboot.controller.admin;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fullstack.springboot.dto.PageRequestDTO;
import com.fullstack.springboot.dto.PageResponseDTO;
import com.fullstack.springboot.dto.admin.AdminDTO;
import com.fullstack.springboot.dto.member.MemberDTO;
import com.fullstack.springboot.dto.product.ProductDTO;
import com.fullstack.springboot.service.admin.AdminService;
import com.fullstack.springboot.service.member.MemberService;
import com.fullstack.springboot.service.product.ProductService;
import com.fullstack.springboot.util.ConvertBase64Util;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@Log4j2
public class AdminController {
	
	private final AdminService adminService;
	
	private final MemberService memberService;
	
	private final ProductService productService;
	
	@PutMapping("/ban/{encodedId}")
	public ResponseEntity<String> banUser(@PathVariable("encodedId") String encodedId) {
		try {	
			Long mno = ConvertBase64Util.decoding(encodedId);
			adminService.banUser(mno);
			return ResponseEntity.ok("밴 완료");
		} catch (Exception e) {
			log.error("밴 에러");
			return ResponseEntity.badRequest().body("밴 에러");
		}
		
		
	}
	
	@PutMapping("/unban/{encodedId}")
	public ResponseEntity<String> unbanUser(@PathVariable("encodedId") String encodedId) {
		try{
			Long mno = ConvertBase64Util.decoding(encodedId);
			adminService.unBanUser(mno);
			return ResponseEntity.ok("밴 해제 완료");
		}catch (Exception e){
			log.error("밴 해제 에러");
			return ResponseEntity.badRequest().body("밴 해제 에러");
		}
	}
	
	@GetMapping("/{encodedId}")
	public MemberDTO getUserInfo(@PathVariable("encodedId") String encodedId) {
		
		Long mno = ConvertBase64Util.decoding(encodedId);
		
		return memberService.findByMno(mno);
	}

	@GetMapping("/id/{adminId}")
	public AdminDTO getMethodName(@PathVariable("adminId") String adminId) {
		
		return adminService.findAdminById(adminId);
	}
	
	
	@PutMapping("/delStatus/{encodedId}")
	public String ChangeProStatus(@PathVariable("encodedId") String encodedId, @RequestParam("status") Boolean status) {
		
		if(status == null) {
			throw new RuntimeException("status is null");
		}
		
		Long pno = ConvertBase64Util.decoding(encodedId);
		
		log.warn(status);
		log.error("adminController - ChangeProStatus check status : " + status);
		
		adminService.productStatus(pno, status);
		
		return status ? "복구 처리 완료" : "삭제 처리 완료";
	}
	
	@GetMapping("/productList")
	public PageResponseDTO<ProductDTO> productList(PageRequestDTO pageRequestDTO,
			@RequestParam(value = "ctno", required = false, defaultValue = "0") Long ctno,
			@RequestParam(value = "delFlag", required = false) Boolean delFlag,
			@RequestParam(value = "keyword", required = false) String keyword){
		
		log.error(delFlag);
		
		return productService.superProductList(pageRequestDTO, ctno, delFlag, keyword);
		
	}
}
