package com.fullstack.springboot.controller.order;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fullstack.springboot.dto.order.OrderDTO;
import com.fullstack.springboot.dto.order.OrderStatus;
import com.fullstack.springboot.service.order.OrderDetailService;
import com.fullstack.springboot.service.order.OrderService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")  // React 연동을 위한 CORS 설정
public class OrderController {

    private final OrderService orderService;
    private final OrderDetailService orderDetailService;
    
    /**
     * 새로운 주문과 주문 상세 정보를 생성합니다.
     * React에서 전달받은 주문 및 주문 상세 정보를 처리합니다.
     * 
     * @param orderDTO 주문 및 주문 상세 정보
     * @return 생성된 주문 정보와 HTTP 200 응답
     */
    @PostMapping
    public ResponseEntity<OrderDTO> createOrder(@RequestBody OrderDTO orderDTO) {
        log.info("========== 주문 및 주문상세 생성 시작 ==========");
        log.info("받은 주문 정보: {}", orderDTO);

        try {
            // 주문 생성
            OrderDTO createdOrder = orderService.createOrder(orderDTO);
            
            return ResponseEntity.ok(createdOrder);
        } catch (Exception e) {
            log.error("주문 및 주문상세 생성 실패: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    
    
    
    
    
    
    

    /**
     * 주문 정보를 조회합니다.
     * React에서 주문 상세 정보 표시에 사용됩니다.
     * 
     * @param ono 주문번호
     * @return 주문 정보와 HTTP 200 응답
     */
    @GetMapping("/{ono}")
    public ResponseEntity<OrderDTO> getOrder(@PathVariable("ono") Long ono) {
        log.info("========== 주문 조회 시작 ==========");
        log.info("조회 주문번호: {}", ono);
        
        try {
            OrderDTO order = orderService.getOrder(ono);
            //log.info("주문 조회 완료 - 상태: {}", order.isOrderState());
            log.info("주문 조회 완료 - 상태: {}", order.getStatus().getDescription());
            return ResponseEntity.ok(order);
            
        } catch (Exception e) {
            log.error("주문 조회 실패: {}", e.getMessage());
            throw e;
        }
    }
    
    @GetMapping("/member/{mno}")
    public ResponseEntity<List<OrderDTO>> getUserOrders(@PathVariable("mno") Long mno) {
        log.info("========== 회원 주문 목록 조회 시작 ==========");
        log.info("회원번호: {}", mno);

        try {
            List<OrderDTO> orders = orderService.getUserOrders(mno);
            log.info("회원 주문 조회 완료 - 주문 수: {}", orders.size());
            // 주문 상세 정보 로그 제거
            return ResponseEntity.ok(orders);

        } catch (Exception e) {
            log.error("회원 주문 목록 조회 실패: {}", e.getMessage());
            throw e;
        }
    }

    /**
     * 모든 주문 목록을 조회합니다.
     * 관리자 페이지에서 사용됩니다.
     * 
     * @return 모든 주문 목록과 HTTP 200 응답
     */
    
    // 모든 주문 가져오기 (판매자용)
//    @GetMapping
//    public ResponseEntity<List<OrderDTO>> getAllOrders() {
//        log.info("========== 모든 주문 목록 조회 시작 ==========");
//
//        try {
//            List<OrderDTO> orders = orderService.getAllOrders();
//            log.info("모든 주문 조회 완료 - 주문 수: {}", orders.size());
//            // 주문 상세 정보 로그 제거
//            return ResponseEntity.ok(orders);
//        } catch (Exception e) {
//            log.error("모든 주문 목록 조회 실패: {}", e.getMessage());
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
//        }
//    }
//    
    
    
    
    @GetMapping
    public ResponseEntity<List<OrderDTO>> getAllOrders() {
        log.info("========== 모든 주문 목록 조회 시작 ==========");

        try {
            List<OrderDTO> orders = orderService.getAllOrders();
            log.info("모든 주문 조회 완료 - 주문 수: {}", orders.size());
            // 주문 상세 정보 로그 제거
            return ResponseEntity.ok(orders);
        } catch (Exception e) {
            log.error("모든 주문 목록 조회 실패: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    
    
    /**
     * 주문 상태별로 주문 목록을 조회합니다.
     * 판매자 페이지에서 사용됩니다.
     * 
     * @param status 주문 상태 (ORDER_COMPLETE, REFUND_PENDING, REFUND_COMPLETE)
     * @param mno 회원 번호 (선택사항)
     * @return 상태별 주문 목록과 HTTP 200 응답
     */
    @GetMapping("/status")
    public ResponseEntity<List<OrderDTO>> getOrdersByStatus(
            @RequestParam(name = "status", required = false) OrderStatus status) {
        log.info("========== 판매자 주문 상태별 조회 시작 ==========");
        log.info("조회할 주문 상태: {}", status);

        try {
            List<OrderDTO> orders = orderService.getOrdersByStatus(status);
            log.info("주문 상태별 조회 완료 - 주문 수: {}", orders.size());
            return ResponseEntity.ok(orders);
        } catch (Exception e) {
            log.error("주문 상태별 조회 실패: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * 특정 회원의 주문 상태별 목록을 조회합니다.
     * 판매자 페이지에서 사용됩니다.
     * 
     * @param mno 회원 번호
     * @param status 주문 상태 (ORDER_COMPLETE, REFUND_PENDING, REFUND_COMPLETE)
     * @return 회원별 상태별 주문 목록과 HTTP 200 응답
     */
    @GetMapping("/user/{mno}/status")
    public ResponseEntity<List<OrderDTO>> getUserOrdersByStatus(
            @PathVariable(name = "mno") Long mno,
            @RequestParam(name = "status", required = false) OrderStatus status) {
        log.info("========== 판매자 회원별 주문 상태 조회 시작 ==========");
        log.info("조회할 회원번호: {}, 주문 상태: {}", mno, status);

        try {
            List<OrderDTO> orders = orderService.getUserOrdersByStatus(mno, status);
            log.info("회원별 주문 상태 조회 완료 - 회원번호: {}, 주문 수: {}", mno, orders.size());
            return ResponseEntity.ok(orders);
        } catch (Exception e) {
            log.error("회원별 주문 상태 조회 실패: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    /**
     * 주문을 취소합니다.
     * React에서 주문 취소 버튼 클릭 시 호출됩니다.
     * 
     * @param ono 주문번호
     * @return 취소된 주문 정보와 HTTP 200 응답
     */
//    @DeleteMapping("/{ono}")
//    public ResponseEntity<OrderDTO> cancelOrder(@PathVariable("ono") Long ono) {
//        log.info("========== 주문 취소 시작 ==========");
//        log.info("취소 주문번호: {}", ono);
//        
//        try {
//            OrderDTO canceledOrder = orderService.cancelOrder(ono);
//            log.info("주문 취소 완료 - 주문번호: {}", ono);
//            return ResponseEntity.ok(canceledOrder);
//            
//        } catch (Exception e) {
//            log.error("주문 취소 실패: {}", e.getMessage());
//            throw e;
//        }
//    }
}