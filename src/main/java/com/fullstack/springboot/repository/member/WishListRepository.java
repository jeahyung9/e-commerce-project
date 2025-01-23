package com.fullstack.springboot.repository.member;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.fullstack.springboot.dto.member.WishListDTO;
import com.fullstack.springboot.entity.product.WishList;

public interface WishListRepository extends JpaRepository<WishList, Long> {

	@Query("select " +
			"new com.fullstack.springboot.dto.member.WishListDTO(w.wno, p.pno, s.sno, w.member.mno, p.p_name, p.p_price, p.p_salePer, s.businessName, p.p_stock) from " +
			"WishList w left join Product p on w.product = p " +
			"left join Seller s on p.seller = s " +
			"where w.member.mno = :mno " +
			"order by wno desc")
	public List<WishListDTO> getWishList(@Param("mno") Long mno);
	
	@Query("select w " +
			"from WishList w left join Product p on w.product = p " +
			"where w.member.mno = :mno and p.pno = :pno")
	public WishList getWishListOne(@Param("mno") Long mno, @Param("pno") Long pno);
	
}
