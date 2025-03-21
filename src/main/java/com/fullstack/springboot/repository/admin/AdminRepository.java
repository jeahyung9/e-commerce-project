package com.fullstack.springboot.repository.admin;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.fullstack.springboot.entity.admin.Admin;

public interface AdminRepository extends JpaRepository<Admin, Long> {
	
	@Query("select ad from Admin ad where ad.adno = :adno")
	Admin getAdminByAdno(@Param("adno") Long adno);
	
	@Query("select ad.adno from Admin ad")
	List<Long> getAdminNo();
	
	@EntityGraph(attributePaths = "superAuth")
	@Query("select ad from Admin ad where ad.adminId = :adminId")
	Admin getAdminWithId(@Param("adminId") String adminId);
}
