package com.fullstack.springboot.repository.product;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.fullstack.springboot.dto.product.OptionDetailDTO;
import com.fullstack.springboot.dto.product.ProductDTO;
import com.fullstack.springboot.entity.product.OptionDetail;

public interface OptionDetailRepository extends JpaRepository<OptionDetail, Long> {
	
	@Query("select od from OptionDetail od left join Product p on od.product.pno = p.pno where od.product.pno = :pno and p.delFlag = false")
	List<OptionDetail> getOptByPno(@Param("pno") Long pno);
	
	@Query("select od from OptionDetail od where od.product.pno = :pno")
	Optional<OptionDetail> getOptDetailWithOptAndPro(@Param("pno") Long pno);
	
	@Query("select od from OptionDetail od where od.product.pno = :pno")
	List<OptionDetail> getProductByPno(@Param("pno") Long pno); //옵션 조회
	
	@Query("select od from OptionDetail od where od.product.pno = :pno and od.od_stock > 0")
	List<OptionDetail> getProductStockByPno(@Param("pno") Long pno); // 재고 0이상

	@Query("update OptionDetail od set od.od_stock = od.od_stock -:cnt where od.odno = :odno and od.od_stock > 0")
	int decreaseOptionStock(@Param("odno") Long odno, @Param("cnt") int cnt); // 재고 감소

	@Query("update OptionDetail od set od.od_stock = od.od_stock +:cnt where od.odno = :odno and od.od_stock > 0")
	int increaseOptionStock(@Param("odno") Long odno, @Param("cnt") int cnt); // 재고 증가
	
}
