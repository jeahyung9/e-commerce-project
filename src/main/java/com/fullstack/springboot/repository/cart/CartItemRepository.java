package com.fullstack.springboot.repository.cart;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.fullstack.springboot.dto.cart.CartItemDTO;
import com.fullstack.springboot.entity.cart.CartItem;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

	@Query("select " +
	"new com.fullstack.springboot.dto.cart.CartItemDTO(ci.cino, od.odno, p.pno, s.sno, c.cno, c.member.mno, p.p_name, od.od_name, p.p_price, p.p_salePer, s.businessName, ci.c_cnt, p.p_stock) from " +
	"CartItem ci inner join Cart c on ci.cart = c " +
	"left join OptionDetail od on ci.optionDetail = od " +
	"left join Product p on od.product = p " +
	"left join Seller s on p.seller = s " +
	"where c.member.mno = :mno " +
	"order by pno, odno, ci desc")
	public List<CartItemDTO> getCartItems(@Param("mno") Long mno);
	
	@Query("select ci " +
			"from CartItem ci inner join Cart c on ci.cart = c " +
			"where c.member.mno = :mno and ci.optionDetail.odno = :odno")
	public CartItem getCi(@Param("mno") Long mno, @Param("odno") Long odno);
	
}
