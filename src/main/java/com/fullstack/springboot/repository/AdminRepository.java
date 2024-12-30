package com.fullstack.springboot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.fullstack.springboot.entity.admin.Admin;

public interface AdminRepository extends JpaRepository<Admin, Long> {
	
	@Query("Select a from Admin a where adminId = :adminId")
	Admin getAdminById(@Param("adminId") String adminId);
}
