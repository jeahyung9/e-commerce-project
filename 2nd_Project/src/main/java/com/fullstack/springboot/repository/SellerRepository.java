package com.fullstack.springboot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.fullstack.springboot.entity.product.Seller;

public interface SellerRepository extends JpaRepository<Seller, Long> {
	
	@Query("select s from Seller s where s.sno = :sno")
	Seller getSellerInfo(@Param("sno") Long sno);
}
