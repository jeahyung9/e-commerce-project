// RefundController.java
package com.fullstack.springboot.controller.order;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fullstack.springboot.dto.order.IamportApiResult;
import com.fullstack.springboot.dto.order.RefundDTO;
import com.fullstack.springboot.entity.order.Refund;
import com.fullstack.springboot.external.payment.IamportClient;
import com.fullstack.springboot.external.payment.exception.PaymentException;
import com.fullstack.springboot.repository.order.RefundRepository;
import com.fullstack.springboot.service.order.RefundService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class RefundController {

    private final RefundService refundService;
    private final IamportClient iamportClient;
    private final RefundRepository refundRepository;
    

    @PostMapping("/orders/{ono}/refund")
    public ResponseEntity<RefundDTO> createOrderRefund(@PathVariable("ono") Long ono, @Valid @RequestBody RefundDTO refundDTO) {
        log.info("========== 주문 환불 요청 시작 ==========");
        log.info("주문번호: {}, 환불 사유: {}", ono, refundDTO.getRf_reason());

        try {
            RefundDTO createdRefund = refundService.createRefundForOrder(ono, refundDTO.getRf_reason(), refundDTO.getImp_uid(), refundDTO.getMerchant_uid());
            log.info("주문 환불 요청 완료 - 환불번호: {}", createdRefund.getRfno());
            return ResponseEntity.status(HttpStatus.CREATED).body(createdRefund);
        } catch (Exception e) {
            log.error("주문 환불 요청 실패: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/refunds/{rfno}/approve")
    public ResponseEntity<String> approveRefundRequest(@PathVariable("rfno") Long rfno) {
        log.info("========== 환불 승인 요청 시작 - 환불번호: {} ==========", rfno);
        
        if (rfno == null) {
            log.error("유효하지 않은 환불 번호");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("유효하지 않은 환불 번호입니다");
        }

        try {
            // 환불 승인 처리
            IamportApiResult cancelResult = refundService.approveRefundRequest(rfno);
            log.info("환불 승인 처리 완료 - 환불번호: {}", rfno);
            
            if (cancelResult.getCode() == 0) {
                return ResponseEntity.ok("환불이 정상적으로 처리되었습니다");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("환불 처리 실패: " + cancelResult.getMessage());
            }

        } catch (PaymentException e) {
            log.error("결제 취소 실패: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("환불 처리 실패: " + e.getMessage());
        } catch (EntityNotFoundException e) {
            log.error("환불 정보 조회 실패: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            log.error("환불 처리 중 오류 발생: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("환불 처리 중 오류가 발생했습니다: " + e.getMessage());
        }
    }
    
    
    @GetMapping("/refunds/member/{mno}")
    public ResponseEntity<?> getMemberRefunds(
            @PathVariable Long mno,
            @RequestParam(required = false) Boolean status) {
        
        log.info("========== 회원 환불 내역 조회 요청 ==========");
        log.debug("회원번호: {}, 상태: {}", mno, status);

        try {
            List<RefundDTO> refunds;
            if (status != null) {
                refunds = refundService.getMemberRefundsByStatus(mno, status);
            } else {
                refunds = refundService.getMemberRefunds(mno);
            }
            
            return ResponseEntity.ok(refunds);
        } catch (Exception e) {
            log.error("환불 내역 조회 실패: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("환불 내역 조회 중 오류가 발생했습니다: " + e.getMessage());
        }
    }
    
    
    
    
    
    
    
    
    
    
    
}