package com.fullstack.springboot.repository;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.fullstack.springboot.entity.product.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
	@EntityGraph(attributePaths = {"superCate","cateList"})
	@Query("select c from Category c where c.ctno = :ctno")
	Category getCateWithCateList(@Param("ctno") Long ctno);
	
	@Query("select c from Category c where c.cateDepth = :depth")
	List<Category> getCateWithDepth(@Param("depth") int depth);
	   
	@Query("select c.ctno from Category c where c.cateDepth = :depth")
	List<Long> getCtnoWithDepth(@Param("depth") int depth);
}
