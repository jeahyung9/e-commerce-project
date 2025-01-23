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
	
	//테스트 용 코드
	@Query("select " +
			"new com.fullstack.springboot.dto.product.OptionDetailDTO(od.odno, od.od_name, od.od_stock, od.od_price, p.pno, p.p_name, p.p_price, p.p_salePer, p.p_stock, p.delFlag, s.sno, s.businessName) " + 
			"from OptionDetail od " + 
			"left join Product p on od.product = p " +
			"left join Seller s on p.seller = s " +
			"where od.product.pno = :pno")
	public List<OptionDetailDTO> getProductWithOption(@Param("pno") Long pno);
	
	@Query("select od from OptionDetail od where od.product.pno = :pno")
	Optional<OptionDetail> getOptDetailWithOptAndPro(@Param("pno") Long pno);
	
	@Query("select od from OptionDetail od where od.product.pno = :pno")
	List<OptionDetail> getProductByPno(@Param("pno") Long pno); //옵션 조회
	
	@Query("select od from OptionDetail od where od.product.pno = :pno and od.od_stock > 0")
	List<OptionDetail> getProductStockByPno(@Param("pno") Long pno); // 재고 0이상

	@Query("update OptionDetail od set od.od_stock = od.od_stock -1 where od.odno = :odno and od.od_stock > 0")
	int decreaseOptionStock(@Param("odno") Long odno); // 재고 감소
	
}
