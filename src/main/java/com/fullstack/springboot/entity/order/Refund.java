package com.fullstack.springboot.entity.order;

import java.time.LocalDateTime;
import org.springframework.data.annotation.CreatedDate;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name="refund")
public class Refund {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rfno;
    
    private Long rf_payment;
    
    private String rf_reason;
    
    private boolean rf_status; // false 는 미완, true 는 완
    
    @CreatedDate
    @Column(name = "rf_requestDate", updatable = false) 
    private LocalDateTime rf_requestDate; // 환불 요청
    
    @CreatedDate
    @Column(name = "rf_completeDate", updatable = true)
    private LocalDateTime rf_completeDate; // 환불 끝
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pmno") // 외래 키 매핑
    private Payment payment;
    
    private String imp_uid;
    
    private String merchant_uid;
}