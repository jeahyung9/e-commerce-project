package com.fullstack.springboot.repository.order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.fullstack.springboot.entity.order.OrderDetail;

import java.util.List;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {

    // 주문 번호로 주문 상세 정보를 찾습니다.
    List<OrderDetail> findByOrderOno(Long ono);

    // 회원 번호로 주문 상세 정보를 찾습니다.
    List<OrderDetail> findByOrderMemberMno(Long mno);

   
    
     
     
     
     

      
      
	    @Query("SELECT DISTINCT od FROM OrderDetail od " +
	 	       "JOIN FETCH od.order o " +
	 	       "LEFT JOIN FETCH od.optionDetail opt " +
	 	       "LEFT JOIN FETCH opt.product p " +
	 	       "LEFT JOIN FETCH p.productImage pi " +
	 	       "WHERE o.ono = :ono")
	 	List<OrderDetail> findOrderDetailsWithAllInfo(@Param("ono") Long ono);


       @Query("SELECT DISTINCT od FROM OrderDetail od " +
              "JOIN FETCH od.order o " +
              "JOIN FETCH od.optionDetail opt " +
              "JOIN FETCH opt.product p " +
              "LEFT JOIN FETCH p.productImage " +
              "WHERE o.member.mno = :mno " +
              "AND od.rno IS NULL " +
              "AND o.status = 'ORDER_COMPLETE'")
       List<OrderDetail> findReviewableByMno(@Param("mno") Long mno);
    
    // 김재형 추가 코드
    @Query("select od from OrderDetail od where od.orno = :orno")
    OrderDetail getOrderDetailOne(@Param("orno") Long orno);
    
}