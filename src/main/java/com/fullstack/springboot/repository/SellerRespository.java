package com.fullstack.springboot.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fullstack.springboot.entity.product.Seller;

public interface SellerRespository extends JpaRepository<Seller, Long> {

}
