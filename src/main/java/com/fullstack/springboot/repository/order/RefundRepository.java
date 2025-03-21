package com.fullstack.springboot.repository.order;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.fullstack.springboot.entity.order.Refund;

import java.util.List;
import java.util.Optional;

@Repository
public interface RefundRepository extends JpaRepository<Refund, Long> {
    
    /**
     * 결제번호로 환불 정보를 조회합니다.
     */
    @Query("SELECT r FROM Refund r WHERE r.payment.pmno = :pmno")
    Optional<Refund> findByPayment_Pmno(@Param("pmno") Long pmno);
    
    /**
     * 환불 상태별로 환불 내역을 조회합니다.
     */
    @Query("SELECT r FROM Refund r WHERE r.rf_status = :status")
    List<Refund> findByStatus(@Param("status") boolean status);
    
    
    
    
    
    
    /**
     * 회원의 모든 환불 내역을 조회합니다.
     */
    @Query("SELECT r FROM Refund r " +
           "LEFT JOIN FETCH r.payment p " +
           "LEFT JOIN FETCH p.order o " +
           "LEFT JOIN FETCH o.orderDetails od " +
           "WHERE o.member.mno = :mno " +
           "ORDER BY r.rf_requestDate DESC")
    List<Refund> findByMemberMno(@Param("mno") Long mno);
    
    /**
     * 회원의 특정 상태의 환불 내역을 조회합니다.
     */
    @Query("SELECT r FROM Refund r " +
           "LEFT JOIN FETCH r.payment p " +
           "LEFT JOIN FETCH p.order o " +
           "LEFT JOIN FETCH o.orderDetails od " +
           "WHERE o.member.mno = :mno " +
           "AND r.rf_status = :status " +
           "ORDER BY r.rf_requestDate DESC")
    List<Refund> findByMemberMnoAndStatus(
        @Param("mno") Long mno,
        @Param("status") boolean status
    );
    
}	