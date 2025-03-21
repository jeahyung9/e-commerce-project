package com.fullstack.springboot.controller.order;

import com.fullstack.springboot.dto.order.IamportResponseDTO;
import com.fullstack.springboot.dto.order.PaymentDTO;
import com.fullstack.springboot.entity.order.Payment;
import com.fullstack.springboot.external.payment.IamportClient;
import com.fullstack.springboot.service.order.PaymentService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class PaymentController {

	 private final PaymentService paymentService; // 하나의 PaymentService만 선언
	 private final IamportClient iamportClient;
	 
	 
    /**
     * 결제 정보를 생성합니다.
     * React에서 결제 요청 시 호출됩니다.
     */
	 @PostMapping
	 public ResponseEntity<Map<String, Object>> createPayment(@RequestBody PaymentDTO paymentDTO) {
	     Map<String, Object> response = new HashMap<>();
	     log.info("========== 결제 생성 시작 ==========");
	     log.info("결제 요청 정보: 주문번호(ono)={}, 결제금액={}, imp_uid={}, 결제방법={}", 
	             paymentDTO.getOno(), 
	             paymentDTO.getAmount(),
	             paymentDTO.getImp_uid(),
	             paymentDTO.getPm_method());

	     try {
	         if (paymentDTO.getPm_method() == null || paymentDTO.getPm_method().isEmpty()) {
	             throw new IllegalArgumentException("결제 방법(pm_method)이 누락되었습니다.");
	         }
	         PaymentDTO createdPaymentDTO = paymentService.createPayment(paymentDTO);
	         log.info("결제 생성 완료 - 결제번호: {}", createdPaymentDTO.getPmno());
	         response.put("success", true);
	         response.put("pmno", createdPaymentDTO.getPmno()); // pmno를 명시적으로 응답에 포함
	         response.put("payment", createdPaymentDTO);
	         return ResponseEntity.ok(response);
	     } catch (Exception e) {
	         log.error("결제 생성 실패: {}", e.getMessage());
	         response.put("success", false);
	         response.put("errorMessage", e.getMessage());
	         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	     }
	 }

	/**
     * 결제 검증을 수행합니다.
     * 아임포트 결제 완료 후 호출됩니다.
     */
	@PostMapping("/verify/{imp_uid}/{pmno}")
	public ResponseEntity<Boolean> verifyPayment(
	        @PathVariable("imp_uid") String imp_uid,
	        @PathVariable("pmno") Long pmno) {
	    log.info("========== 결제 검증 시작 ==========");
	    log.info("검증 정보: imp_uid={}, 결제번호={}", imp_uid, pmno);

	    try {
	        // 아임포트에서 결제 정보 조회
	        IamportResponseDTO responseDTO = iamportClient.paymentByImpUid(imp_uid);

	        // 결제 정보 검증 로직 추가
	        Boolean isValid = paymentService.verifyPayment(imp_uid, pmno);
	        log.info("결제 검증 완료 - 결과: {}", isValid);
	        return ResponseEntity.ok(isValid);
	    } catch (Exception e) {
	        log.error("결제 검증 실패: {}", e.getMessage());
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false);
	    }
	}
	
	
	

    /**
     * 결제 정보를 조회합니다.
     */
    @GetMapping("/order/{ono}")
    public ResponseEntity<PaymentDTO> getPaymentByOrderNumber(@PathVariable("ono") Long ono) {
        log.info("========== 주문 번호로 결제 정보 조회 시작 ==========");
        log.info("주문번호: {}", ono);

        try {
            Payment payment = paymentService.getPaymentByOrderNumber(ono);
            PaymentDTO paymentDTO = paymentService.convertToDTO(payment);
            log.info("결제 정보 조회 완료 - 주문번호: {}", ono);
            return ResponseEntity.ok(paymentDTO);
        } catch (EntityNotFoundException e) {
            log.error("결제 정보 조회 실패: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            log.error("결제 정보 조회 실패: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    
    
    
    
    
    
    

//    /**
//     * 회원의 모든 결제 내역을 조회합니다.
//     */
//    @GetMapping("/member/{mno}")
//    public ResponseEntity<List<PaymentDTO>> getMemberPayments(@PathVariable Long mno) {
//        log.info("========== 회원 결제 내역 조회 시작 ==========");
//        log.info("회원번호: {}", mno);
//
//        try {
//            List<PaymentDTO> paymentdtolist = paymentservice.getMemberPayments(mno);
//            log.info("회원 결제 내역 조회 완료 - 결제 수: {}", paymentdtolist.size());
//            return ResponseEntity.ok(paymentdtolist);
//        } catch (Exception e) {
//            log.error("회원 결제 내역 조회 실패: {}", e.getMessage());
//            throw e;
//        }
//    }
    
    
    
    
}