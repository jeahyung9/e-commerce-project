package com.fullstack.springboot.entity.order;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Entity
@Getter
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
	@Column(name = "rf_requsetDate", updatable = false)
	private LocalDateTime rf_requsetDate; // 환불 요청 
	
	@CreatedDate
	@Column(name = "rf_completeDate", updatable = false)
	private LocalDateTime rf_completeDate; // 환불 끝
	
	@OneToOne(fetch = FetchType.LAZY)
	private Payment payment;
}
