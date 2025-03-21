package com.fullstack.springboot.repository.qa;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.fullstack.springboot.entity.qa.ProductQA;

public interface ProductQARepository extends JpaRepository<ProductQA, Long> {
    
      @Query("select qa from ProductQA qa " +
     		 "left join Member m on qa.member = m " + 
     		 "left join Product p on qa.product = p " +
     		 "where qa.product.pno = :pno")
      Page<ProductQA> getQAList(@Param("pno") Long pno, Pageable pageable);
      
      @Query("select qa from ProductQA qa " +
      		 "left join Member m on qa.member = m " + 
      		 "left join Product p on qa.product = p " +
      		 "where qa.member.mno = :mno")
       List<ProductQA> getQAListBymno(@Param("mno") Long mno);
      
      @Query("select qa from ProductQA qa " +
       		 "left join Member m on qa.member = m " + 
       		 "left join Product p on qa.product = p " +
       		 "where qno = :qno")
      ProductQA getQAOne(@Param("qno") Long qno);

}
