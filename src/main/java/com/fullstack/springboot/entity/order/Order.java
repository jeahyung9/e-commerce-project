package com.fullstack.springboot.entity.order;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;

import com.fullstack.springboot.dto.order.OrderStatus;
import com.fullstack.springboot.entity.member.Member;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString(exclude = "orderDetails")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "pro_order")
public class Order {
    // 주문 entity, orderDetail 에서 개별 상품의 정보를 받아 한번에 저장하고 결제 여부를 판단하게 될 것, Member 과 Join

    // 멤버
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mno")
    private Member member;  // Member 엔티티와의 관계 추가

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ono;

    @Column(nullable = false)
    @Builder.Default
    private Long o_totalPrice = 0L; // not null

    private String o_address; // not null

    private String o_reciver; // not null

    private String o_reciverPhoNum; // not null

    @Enumerated(EnumType.STRING) // Enum을 문자열로 저장
    @Column(length = 18)
    private OrderStatus status;

    @CreatedDate
    @Column(name = "orderDate", updatable = false)
    private LocalDateTime orderDate; // 주문 날짜 저장

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderDetail> orderDetails = new ArrayList<>();

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Payment payment;

    public void increaseTotalPrice(int price) {
        this.o_totalPrice += price;
    }
}