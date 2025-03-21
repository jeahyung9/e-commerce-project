package com.fullstack.springboot.service.order;

import com.fullstack.springboot.dto.order.OrderDTO;
import com.fullstack.springboot.dto.order.OrderStatus;
import java.util.List;

/**
 * 주문 관련 비즈니스 로직을 처리하는 서비스 인터페이스
 * 주문 생성, 조회, 취소 등의 기능을 정의합니다.
 */
public interface OrderService {
    
    /**
     * 새로운 주문을 생성합니다.
     * 
     * @param orderdto 주문 정보 (총금액, 배송지 등)
     * @return 생성된 주문 정보
     */
    OrderDTO createOrder(OrderDTO orderdto);
    
    /**
     * 주문 번호로 주문 정보를 조회합니다.
     * 
     * @param ono 주문번호
     * @return 주문 정보
     * @throws EntityNotFoundException 주문을 찾을 수 없는 경우
     */
    OrderDTO getOrder(Long ono);
    
    /**
     * 주문을 취소합니다.
     * 결제 전 상태의 주문만 취소 가능합니다.
     * 
     * @param ono 주문번호
     * @return 취소된 주문 정보
     * @throws EntityNotFoundException 주문을 찾을 수 없는 경우
     * @throws IllegalStateException 이미 결제가 완료된 주문인 경우
     */
    OrderDTO cancelOrder(Long ono);
    
    /**
     * 특정 회원의 모든 주문을 조회합니다.
     * React의 마이페이지에서 사용됩니다.
     * 
     * @param mno 회원번호
     * @return 주문 목록
     */
    List<OrderDTO> getUserOrders(Long mno);
    
    
    //모든 조회 찾기
    List<OrderDTO> getAllOrders();
    
    /**
     * 주문 상태별로 주문을 조회합니다.
     * 
     * @param status 주문 상태
     * @return 해당 상태의 주문 목록
     */
    List<OrderDTO> getOrdersByStatus(OrderStatus status);
    
    /**
     * 특정 회원의 주문을 상태별로 조회합니다.
     * 
     * @param mno 회원번호
     * @param status 주문 상태
     * @return 해당 회원의 해당 상태 주문 목록
     */
    List<OrderDTO> getUserOrdersByStatus(Long mno, OrderStatus status);
    
    
    
    
    
    
    
    
    
    
    
} 