package com.fullstack.springboot.repository.product;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.fullstack.springboot.entity.product.Product;

import jakarta.transaction.Transactional;

public interface ProductRepository extends JpaRepository<Product, Long> {
	
	@Modifying
	@Query("update Product p set p.delFlag = :flag where p.pno = :pno")
	void updateToDelFlag(@Param("pno") Long pno, @Param("flag") boolean flag);
	
	@Modifying
	@Transactional
	@Query("update Product p set p.p_stock = (select sum(od.od_stock) from OptionDetail od where od.product.pno = p.pno) where p.pno = :pno")
	int updateProductStockByOptionDetail(@Param("pno") Long pno);
	
	@Modifying
	@Transactional
	@Query("update Product p set p.p_salesVol = (select sum(od.od_salesVol) from OptionDetail od where od.product.pno = p.pno) where p.pno = :pno")
	int updateProductSalesVolByOptionDetail(@Param("pno") Long pno);

	// 특정 상품의 이름으로 상품을 조회
	@Query("select p from Product p where p.p_name like %:name% and p.delFlag = false")	
	Page<Product> findByName(@Param("name") String name, Pageable pageable);

	@Query("select p from Product p where p.p_name like %:name%")	
	Page<Product> superFindByName(@Param("name") String name, Pageable pageable);
	    
	@Query("select p.seller.sno from Product p")
	List<Long> findBySno();
		
	@Query("select p from Product p where p.delFlag = false")
	Page<Product> getDelFlagFalse(Pageable pageable); // 메인 페이지 용
	
	@Query("select p from Product p where p.delFlag = true")
	Page<Product> getDelFlagTrue(Pageable pageable); // 관리자용, 전체 볼때가 아닌 내려간 상품을 확인하기 위함
	
}
