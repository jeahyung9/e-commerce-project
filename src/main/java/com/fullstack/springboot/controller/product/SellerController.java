package com.fullstack.springboot.controller.product;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fullstack.springboot.dto.seller.SellerDTO;
import com.fullstack.springboot.service.admin.AdminService;
import com.fullstack.springboot.service.seller.SellerService;
import com.fullstack.springboot.util.ConvertBase64Util;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RestController
@RequestMapping("/api/seller")
@RequiredArgsConstructor
@Log4j2
public class SellerController { 
	// 필요 기능 : 재고 스톡 증감, 상품 삭제 및 복구 처리, 상품 등록 및 수정(상품 컨트롤러에 존재), 내 매출 체크, 리뷰 관리, 주문 관리, 판매 분석
	
	private final AdminService adminService;
	
	private final SellerService sellerService;
	
	@PutMapping("/delStatus/{encodedId}")
	public String ChangeProStatus(@PathVariable String encodedId, @RequestParam boolean status) {
		
		Long pno = ConvertBase64Util.decoding(encodedId);
		
		adminService.productStatus(pno, status);
		
		if(status) {
			return "복구 처리 완료";
		}else {
			return "삭제 처리 완료";
		}
	}
	
	@PostMapping("/login")
	public ResponseEntity<SellerDTO> loginSeller(@RequestBody SellerDTO sellerDTO){
		log.error("sellerController" + sellerDTO);
		try {
			SellerDTO authenticatedSeller = sellerService.loginSeller(sellerDTO.getSId(), sellerDTO.getSPw());
			return ResponseEntity.ok(authenticatedSeller);
		}catch (RuntimeException e) {
			return ResponseEntity.status(401).body(null);
		}
	}
	
	@PostMapping("/join")
	public ResponseEntity<SellerDTO> joinSeller(@RequestBody SellerDTO sellerDTO){
		boolean isDuplicate = sellerService.checkDuplicateSellerId(sellerDTO.getSId());
		if (isDuplicate) {
			// ID가 중복될 경우 400 상태 코드와 메시지 반환
			return ResponseEntity.status(400).body(null);
		}
		
		SellerDTO createSeller = sellerService.createSeller(sellerDTO);
		return ResponseEntity.ok(createSeller);
	}
	
	@GetMapping("/check-id/{m_id}") // 중복 아이디 체크
    public ResponseEntity<Boolean> checkDuplicateId(@PathVariable String sId) {
		boolean isDuplicate = sellerService.checkDuplicateSellerId(sId);
        return ResponseEntity.ok(isDuplicate); // 중복 여부 반환
    }
	
//	@GetMapping("/salse/{enocodedId}")
//	public Long totalSales(@PathVariable String encodedId) {
//		
//		return totalSalse;
//	}
}
