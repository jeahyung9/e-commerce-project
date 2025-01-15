package com.fullstack.springboot.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.fullstack.springboot.entity.product.CategoryProductBumper;
import com.fullstack.springboot.entity.product.Product;

public interface CategoryProductRepository extends JpaRepository<CategoryProductBumper, Long> {
	
	@Query("Select p from Product p left join CategoryProductBumper cpb on p.pno = cpb.product.pno where p.pno = :pno")
	Product getProductInfoWithCate(@Param("pno") Long pno); //pno 기준으로 카테고리 뽑아오기
	
	@Query("Select cpb from CategoryProductBumper cpb "
			+ "left join Product p on cpb.product.pno = p.pno "
			+ "left join Category c on cpb.category.ctno = c.ctno")
	List<CategoryProductBumper> getProductListWithCategory();
	
}

//select * from category_product_bumper cpb 
//left join Product p on cpb.product_pno = p.pno
//left join category c on cpb.cate_ctno = c.ctno;