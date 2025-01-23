package com.fullstack.springboot.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fullstack.springboot.entity.product.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

	
}
