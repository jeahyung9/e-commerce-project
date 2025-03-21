package com.fullstack.springboot.repository.order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.fullstack.springboot.entity.order.Order;
import com.fullstack.springboot.dto.order.OrderStatus;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    
    /**
     * 회원의 모든 주문을 조회합니다.
     */
    List<Order> findByMember_Mno(Long mno);
    
    /**
     * 회원의 특정 상태의 주문을 조회합니다.
     */
    List<Order> findByMember_MnoAndStatus(Long mno, OrderStatus status);

    /**
     * 주문과 결제를 통해 환불 정보를 조회합니다.
     */
    @Query("SELECT o FROM Order o JOIN o.payment p JOIN Refund r ON p.pmno = r.payment.pmno WHERE o.member.mno = :mno")
    List<Order> findOrdersWithRefundByMemberMno(@Param("mno") Long mno);
    
    // 모든 주문 조회 (결제, 환불 정보 포함)
    @Query("SELECT DISTINCT o FROM Order o " +
           "LEFT JOIN FETCH o.payment p " +
           "LEFT JOIN FETCH o.member " +
           "LEFT JOIN FETCH o.orderDetails")
    List<Order> findAllWithPaymentInfo(); 
    
    /**
     * 특정 상태의 모든 주문을 조회합니다.
     */
    List<Order> findByStatus(OrderStatus status);
    
    
    
    
    
    
    
    
    
    
} 