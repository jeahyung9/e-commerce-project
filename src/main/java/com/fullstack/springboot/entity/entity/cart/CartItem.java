package com.fullstack.springboot.entity.cart;

import org.hibernate.annotations.ColumnDefault;

import com.fullstack.springboot.entity.product.OptionDetail;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name="cart_item")
public class CartItem {

	   @Id
	   @GeneratedValue(strategy = GenerationType.IDENTITY)
	   private Long cino;
	   
	   @ColumnDefault("'1'")
	   private int c_cnt;
	   
	   @ManyToOne(fetch = FetchType.LAZY)
	   private Cart cart;

	   @ManyToOne(fetch = FetchType.LAZY)
	   private OptionDetail optionDetail;
	   
	   public void changeCnt(int cnt) {
		   this.c_cnt = cnt;
	   }
	   
	}