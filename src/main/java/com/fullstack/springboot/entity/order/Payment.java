package com.fullstack.springboot.entity.order;

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
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name="payment")
public class Payment {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pmno;
    
    private String pm_method; // 결제수단
    
    private Long amount; // 결제 금액 추가
    
    private String imp_uid; // 아임포트 결제번호 추가
    
    private String merchant_uid; // 상점 거래번호 추가
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ono") // 외래 키 매핑
    private Order order;
    
}