package com.fullstack.springboot.repository.product;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.fullstack.springboot.dto.product.ProductDTO;
import com.fullstack.springboot.entity.product.Product;

import jakarta.transaction.Transactional;

public interface ProductRepository extends JpaRepository<Product, Long> {
	
	@Query("select p from Product p where p.pno = :pno")
	Optional<Product> getProductInfo(@Param("pno") Long pno);
	
	@Modifying
	@Query("update Product p set p.delFlag = :flag where p.pno = :pno")
	void updateToDelete(@Param("pno") Long pno, @Param("flag") boolean flag);
	
	@Query("select p from Product p where p.delFlag = false")
	Page<Object[]> selectList(Pageable pageable);
	
	@Modifying
	@Transactional
	@Query("update Product p set p.p_stock = (select sum(od.od_stock) from OptionDetail od where od.product.pno = p.pno) where p.pno = :pno")
	int updateProductStockByOptionDetail(@Param("pno") Long pno);

	// 특정 상품의 이름으로 상품을 조회
	@Query("select p from Product p where p.p_name = :name")
	List<Product> findByName(@Param("name") String name);
	    
	
}
