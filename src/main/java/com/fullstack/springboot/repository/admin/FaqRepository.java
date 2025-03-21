package com.fullstack.springboot.repository.admin;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.fullstack.springboot.entity.admin.Faq;

public interface FaqRepository extends JpaRepository<Faq, Long>{
	
	@Query("select f from Faq f left join f.admin ad where ad.adno = :adno")
	Object[] getFaqListByAdmin(@Param("adno") Long adno);
	
	@Query("select f from Faq f left join f.admin ad where ad.adno = :adno and f.isPrivate = false")
	Page<Faq> getFaqListByAdmin(@Param("adno") Long adno, Pageable pageable);
	
	@Query("select f from Faq f where f.isPrivate = false")
	Page<Faq> findIsPublic(Pageable pageable);
	
	@Modifying
	@Query("update Faq f set f.isPrivate = :isPrivate where f.fno = :fno")
	void updateToPrivate(@Param("fno") Long fno, @Param("isPrivate") boolean isPrivate);
}
