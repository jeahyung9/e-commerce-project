package com.fullstack.springboot.repository.cart;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.fullstack.springboot.entity.cart.Cart;


public interface CartRepository extends JpaRepository<Cart, Long> {

	@Query("select c from Cart c where c.member.mno = :mno")
	public Optional<Cart> getCart(@Param("mno") Long mno);
	
	
}
