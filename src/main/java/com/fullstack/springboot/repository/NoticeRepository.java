package com.fullstack.springboot.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.fullstack.springboot.entity.admin.Notice;

public interface NoticeRepository extends JpaRepository<Notice, Long> {
	
	@Query("select n from Notice n left join n.admin ad where ad.adno = :adno")
	Object[] getNoticeListByAdmin(@Param("adno") Long adno);
	
}

