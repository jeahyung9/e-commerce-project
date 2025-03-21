package com.fullstack.springboot.dto.admin;

import java.util.HashSet;
import java.util.Set;

import com.fullstack.springboot.entity.admin.Admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminDTO {

	private Long adno;
	
	private String adminEmail;
	
	private String adminId;
	
	private String adminPw;
	
	private String name;
	
	private Set<SuperAuth> superAuth = new HashSet<>();
	
	public AdminDTO(Admin admin) {
		this.adno = admin.getAdno();
		this.adminEmail = admin.getAdminEmail();
		this.adminId = admin.getAdminId();
		this.adminPw = admin.getAdminPw();
		this.name = admin.getName();
		this.superAuth = admin.getSuperAuth();
	}
}
