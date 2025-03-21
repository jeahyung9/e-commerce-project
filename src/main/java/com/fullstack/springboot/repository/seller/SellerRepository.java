package com.fullstack.springboot.repository.seller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.fullstack.springboot.entity.product.Product;
import com.fullstack.springboot.entity.seller.Seller;

import jakarta.transaction.Transactional;

public interface SellerRepository extends JpaRepository<Seller, Long> {
	
	@Query("select p from Product p where p.seller.sno = :sno")
	Page<Product> findProductWithSeller(@Param("sno") Long sno, Pageable pageable); // 판매자용, 자신이 판매중 및 판매 종료 상품 확인
	
	@Query("select p from Product p where p.seller.sno = :sno and p.delFlag = :status")
	Page<Product> findDelTrueForSeller(@Param("sno") Long sno, @Param("status") boolean status, Pageable pageable); // 판매자용, 자신이 판매한 상품중 내린것 또는 판매중 확인

	@EntityGraph(attributePaths = "superAuth")
    @Transactional
    @Query("select s from Seller s where s.s_id = :sId")
    Seller findBySellerWithSId(@Param("sId") String sId);
}
