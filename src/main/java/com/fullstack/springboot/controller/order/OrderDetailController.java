package com.fullstack.springboot.controller.order;

import com.fullstack.springboot.dto.order.OrderDetailDTO;
import com.fullstack.springboot.service.order.OrderDetailService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/order-details")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class OrderDetailController {

    private final OrderDetailService orderDetailService;

    /**
     * 주문 상세 정보를 생성합니다.
     * React의 장바구니에서 주문 완료 시 호출됩니다.
     * 
     * @param orderDetailDTO 주문 상세 정보 (상품 정보, 수량 등)
     * @return 생성된 주문 상세 정보
     */
    
//    @PostMapping
//    public ResponseEntity<OrderDetailDTO> createOrderDetail(@RequestBody @Valid OrderDetailDTO orderDetailDTO) {
//        log.info("========== 주문 상세 생성 시작 ==========");
//        log.info("주문 상세 정보: {}", orderDetailDTO);
//        
//        try {
//            OrderDetailDTO createdDetail = orderDetailService.createOrderDetail(orderDetailDTO);
//            log.info("주문 상세 생성 완료 - 상세번호: {}", createdDetail.getOrno());
//            return ResponseEntity.ok(createdDetail);
//        } catch (Exception e) {
//            log.error("주문 상세 생성 실패: {}", e.getMessage());
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
//        }
//    }

    
    /**
     * 기본정보
     * 주문의 모든 상세 정보를 조회합니다.
     * React의 주문 상세 페이지에서 사용됩니다.
     * 
     * @param ono 주문 번호
     * @return 주문 상세 정보 목록
     */
    @GetMapping("/order/{ono}")
    public ResponseEntity<List<OrderDetailDTO>> getOrderDetails(@PathVariable("ono") Long ono) {
        log.info("========== 주문 상세 목록 조회 시작 ==========");
        log.info("주문번호: {}", ono);

        List<OrderDetailDTO> details = orderDetailService.getOrderDetails(ono);
        log.info("주문 상세 조회 완료 - 상세항목 수: {}", details.size());
        return ResponseEntity.ok(details);
    }

    /**
     * 리뷰 번호를 업데이트합니다.
     * React의 리뷰 작성 완료 시 호출됩니다.
     * 
     * @param orno 주문 상세 번호
     * @param rno 리뷰 번호
     * @return 업데이트된 주문 상세 정보
     */
    @PutMapping("/{orno}/review-status")
    public ResponseEntity<OrderDetailDTO> updateReviewStatus(
            @PathVariable("orno") Long orno,
            @RequestParam("rno") Long rno) {
        log.info("========== 리뷰 번호 업데이트 시작 ==========");
        log.info("주문 상세 번호: {}, 리뷰 번호: {}", orno, rno);

        OrderDetailDTO updatedDetail = orderDetailService.updateReviewStatus(orno, rno);
        log.info("리뷰 번호 업데이트 완료 - 상세번호: {}, 리뷰번호: {}", orno, rno);
        return ResponseEntity.ok(updatedDetail);
    }

    /**
     * 회원의 리뷰 작성 가능한 주문 상세 목록을 조회합니다.
     * React의 리뷰 작성 페이지에서 사용됩니다.
     * 
     * @param mno 회원 번호
     * @return 리뷰 작성 가능한 주문 상세 목록
     */
    @GetMapping("/reviewable/{mno}")
    public ResponseEntity<List<OrderDetailDTO>> getReviewableOrders(@PathVariable("mno") Long mno) {
        log.info("========== 리뷰 작성 가능 목록 조회 시작 ==========");
        log.info("회원번호: {}", mno);

        List<OrderDetailDTO> reviewableOrders = orderDetailService.getReviewableOrders(mno);
        log.info("리뷰 작성 가능 목록 조회 완료 - 항목 수: {}", reviewableOrders.size());
        return ResponseEntity.ok(reviewableOrders);
    }
}