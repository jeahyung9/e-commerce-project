package com.fullstack.springboot.service.order;

import com.fullstack.springboot.dto.order.OrderDetailDTO;
import java.util.List;

/**
 * 주문 상세 정보를 처리하는 서비스 인터페이스
 * 주문 상세 생성, 조회, 리뷰 상태 업데이트 등의 기능을 정의합니다.
 */
public interface OrderDetailService {
    OrderDetailDTO createOrderDetail(OrderDetailDTO orderDetailDTO);
    List<OrderDetailDTO> getOrderDetails(Long orderOno);
    OrderDetailDTO updateReviewStatus(Long orno, Long rno);
    List<OrderDetailDTO> getReviewableOrders(Long mno);
}