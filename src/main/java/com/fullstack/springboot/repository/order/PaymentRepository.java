package com.fullstack.springboot.repository.order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fullstack.springboot.entity.order.Payment;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    
    /**
     * 주문의 결제 정보를 조회합니다.
     */
    //Optional<Payment> findByOrder_Ono(Long orderOno);
    Optional<Payment> findByOrder_Ono(Long ono);
  
    
    /**
     * 회원의 모든 결제 내역을 조회합니다.
     */
    List<Payment> findByOrder_Member_Mno(Long mno);
    
    /**
     * 결제번호로 결제 정보를 조회합니다.
     */
    Optional<Payment> findByPmno(Long pmno);
}