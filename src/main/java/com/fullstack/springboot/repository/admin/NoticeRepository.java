package com.fullstack.springboot.repository.admin;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.fullstack.springboot.entity.admin.Notice;

public interface NoticeRepository extends JpaRepository<Notice, Long> {
	
	@Query("select n from Notice n left join n.admin ad where ad.adno = :adno")
	Object[] getNoticeListByAdmin(@Param("adno") Long adno);
	
	@Query("select n from Notice n left join n.admin ad where ad.adno = :adno and n.isPrivate = false")
	Page<Notice> getNoticeListByAdmin(@Param("adno") Long adno, Pageable pageable);
	
	@Query("select n from Notice n where n.isPrivate = false")
	Page<Notice> findIsPublic(Pageable pageable);
	
	@Modifying
	@Query("update Notice n set n.isPrivate = :isPrivate where n.nno = :nno")
	void updateToPrivate(@Param("nno") Long pno, @Param("isPrivate") boolean isPrivate);
}

