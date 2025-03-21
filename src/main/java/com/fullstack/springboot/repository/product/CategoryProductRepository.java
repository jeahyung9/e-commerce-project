package com.fullstack.springboot.repository.product;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.fullstack.springboot.entity.product.CategoryProductBumper;
import com.fullstack.springboot.entity.product.Product;

public interface CategoryProductRepository extends JpaRepository<CategoryProductBumper, Long> {
	
	@Query("Select p from Product p left join CategoryProductBumper cpb on p.pno = cpb.product.pno where cpb.category.ctno = :ctno and p.delFlag = false")
	List<Product> getProductInfoWithCate(@Param("ctno") Long ctno); //ctno 기준으로 상품 뽑아오기
	
	@Query("Select p from Product p left join CategoryProductBumper cpb on p.pno = cpb.product.pno where cpb.category.ctno = :ctno and p.delFlag = false")
	Page<Product> getProductInfoWithCate(@Param("ctno") Long ctno, Pageable pageable); //ctno 기준으로 상품 뽑아오기
	
	@Query("Select p from Product p left join CategoryProductBumper cpb on p.pno = cpb.product.pno "
			+ "where cpb.category.ctno = :ctno and p.delFlag = false and p.p_name like %:name%")
	Page<Product> getProductInfoWithCate(@Param("ctno") Long ctno, @Param("name") String name, Pageable pageable); //ctno, 검색어 기준으로 뽑아오기
	
	@Query("Select cpb from CategoryProductBumper cpb "
			+ "left join Product p on cpb.product.pno = p.pno "
			+ "left join Category c on cpb.category.ctno = c.ctno")
	List<CategoryProductBumper> getProductListWithCategory();
	
	@Query("SELECT p FROM Product p Left JOIN CategoryProductBumper cp on p.pno = cp.product.pno " +
            "WHERE cp.category.superCate.superCate.ctno = :ctno and p.delFlag = false")
    Page<Product> getProductsInDepth1Category(@Param("ctno") Long ctno, Pageable pageable);
	
	@Query("SELECT p FROM Product p Left JOIN CategoryProductBumper cp on p.pno = cp.product.pno " +
            "WHERE cp.category.superCate.superCate.ctno = :ctno and p.delFlag = false and p.p_name like %:name%")
    Page<Product> getProductsInDepth1Category(@Param("ctno") Long ctno, @Param("name") String name, Pageable pageable);

    @Query("SELECT p FROM Product p Left JOIN CategoryProductBumper cp on p.pno = cp.product.pno " +
    		"WHERE cp.category.superCate.ctno = :ctno and p.delFlag = false")
    Page<Product> getProductsInDepth2Category(@Param("ctno") Long ctno, Pageable pageable);
    
    @Query("SELECT p FROM Product p Left JOIN CategoryProductBumper cp on p.pno = cp.product.pno " +
    		"WHERE cp.category.superCate.ctno = :ctno and p.delFlag = false and p.p_name like %:name%")
    Page<Product> getProductsInDepth2Category(@Param("ctno") Long ctno, @Param("name") String name, Pageable pageable);
    
    @Query("Select cpb from CategoryProductBumper cpb left join Product p on cpb.product.pno = p.pno where cpb.product.pno = :pno and p.delFlag = false")
    List<CategoryProductBumper> getCpbListWithPno(@Param("pno") Long pno);
	
	/* 관리자 및 판매자 용 페이징 */
    
    @Query("Select p from Product p left join CategoryProductBumper cpb on p.pno = cpb.product.pno where cpb.category.ctno = :ctno")
	Page<Product> superProductInfoWithCate(@Param("ctno") Long ctno, Pageable pageable);
    
    @Query("Select p from Product p left join CategoryProductBumper cpb on p.pno = cpb.product.pno "
			+ "where cpb.category.ctno = :ctno and p.p_name like %:name%")
	Page<Product> superProductInfoWithCate(@Param("ctno") Long ctno, @Param("name") String name, Pageable pageable);
    
    @Query("SELECT p FROM Product p Left JOIN p.categoryProductBumpers cp on p.pno = cp.product.pno " +
    		"WHERE cp.category.superCate.ctno = :ctno")
    Page<Product> superProductsInDepth2Category(@Param("ctno") Long ctno, Pageable pageable);
    
    @Query("SELECT p FROM Product p Left JOIN p.categoryProductBumpers cp on p.pno = cp.product.pno " +
    		"WHERE cp.category.superCate.ctno = :ctno and p.p_name like %:name%")
    Page<Product> superProductsInDepth2Category(@Param("ctno") Long ctno, @Param("name") String name, Pageable pageable);
    
    @Query("SELECT p FROM Product p Left JOIN p.categoryProductBumpers cp on p.pno = cp.product.pno " +
            "WHERE cp.category.superCate.superCate.ctno = :ctno")
    Page<Product> superProductsInDepth1Category(@Param("ctno") Long ctno, Pageable pageable);
	
	@Query("SELECT p FROM Product p Left JOIN p.categoryProductBumpers cp on p.pno = cp.product.pno " +
            "WHERE cp.category.superCate.superCate.ctno = :ctno and p.p_name like %:name%")
    Page<Product> superProductsInDepth1Category(@Param("ctno") Long ctno, @Param("name") String name, Pageable pageable);
}