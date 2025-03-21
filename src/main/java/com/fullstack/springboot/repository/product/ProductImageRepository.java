package com.fullstack.springboot.repository.product;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.fullstack.springboot.entity.product.ProductImage;

import jakarta.transaction.Transactional;

public interface ProductImageRepository extends JpaRepository<ProductImage, Long>{
	
	@Query("select pi from ProductImage pi where pi.product.pno = :pno")
	List<ProductImage> getProductImgByPno(@Param("pno") Long pno); //상품 정보로 리스트 뽑기
	
	@Query("select count(pi) from ProductImage pi where pi.product.pno = :pno")
	int getProductImgCountByPno(@Param("pno") Long pno);
	
	@Query("select pi from ProductImage pi where pi.product.pno = :pno and pi.pi_ord = :ord")
	ProductImage getProductImgByPnoWithOrd(@Param("pno") Long pno, @Param("ord") int ord); //상품의 해당 이미지 중 순서에 해당하는 이미지 
	
	@Modifying
	@Transactional // 이미지간 순서 변경
	@Query("update ProductImage pi set pi.pi_ord = :newOrd where pi.product.pno = :pno and pi.pi_ord = :oldOrd")
	void updateProImgOrd(@Param("pno") Long pno, @Param("oldOrd") int oldOrd, @Param("newOrd") int newOrd); // old : 이미지 현 순서, new : 바뀐 후의 이미지 순서
	
	@Modifying
	@Transactional  // 이미지 중간 순서에 추가시 나머지 이미지 순서 + 1
	@Query("update ProductImage pi set pi.pi_ord = pi.pi_ord + 1 where pi.product.pno = :pno and pi.pi_ord >= :insertOrd")
	void increaseOrdWithNewOrd(@Param("pno") Long pno, @Param("insertOrd") int insertOrd); //추가된 순서
	
	@Modifying
	@Transactional  // 이미지 중간 순서에 삭제시 나머지 이미지 순서 - 1
	@Query("update ProductImage pi set pi.pi_ord = pi.pi_ord -1 where pi.product.pno = :pno and pi.pi_ord >= :deleteOrd")
	void decreaseOrdWithDelOrd(@Param("pno") Long pno, @Param("deleteOrd") int deleteOrd); //삭제된 순서
	
	@Modifying
	@Transactional  // 이미지간 순서 변경시, 나머지 이미지 순서 + 1
	@Query("update ProductImage pi set pi.pi_ord = pi.pi_ord + 1 where pi.product.pno = :pno and pi.pi_ord between :oldOrd and :newOrd - 1")
	void increaseProImgOrd(@Param("pno") Long pno, @Param("oldOrd") int oldOrd, @Param("newOrd") int newOrd); // old : 바뀌기 전, new : 바뀐 후
	
	@Modifying
	@Transactional  // 이미지간 순서 변경시, 나머지 이미지 순서 - 1
	@Query("update ProductImage pi set pi.pi_ord = pi.pi_ord -1 where pi.product.pno = :pno and pi.pi_ord between :newOrd + 1 and :oldOrd")
	void decreaseProImgOrd(@Param("pno") Long pno, @Param("oldOrd") int oldOrd, @Param("newOrd") int newOrd); // old : 바뀌기 전, new : 바뀐 후
	
	@Modifying
	@Transactional
	@Query("delete from ProductImage pi where pi.product.pno = :pno and pi.pi_ord =:ord")
	void delProImg(@Param("pno") Long pno, @Param("ord") int ord);
	
	@Modifying
	@Transactional
	@Query("delete from ProductImage pi where pi.product.pno = :pno")
	void delAllProImg(@Param("pno") Long pno);
	
	
}
