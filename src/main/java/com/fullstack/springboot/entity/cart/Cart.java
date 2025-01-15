package com.fullstack.springboot.entity.cart;

import com.fullstack.springboot.entity.member.Member;

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

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@Table(name = "cart")
public class Cart {
   
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long cno;
   
   @OneToOne(fetch = FetchType.LAZY)
   private Member member;
   
}