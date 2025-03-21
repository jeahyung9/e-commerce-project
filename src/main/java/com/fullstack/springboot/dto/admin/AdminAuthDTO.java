package com.fullstack.springboot.dto.admin;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import com.fullstack.springboot.entity.admin.Admin;

import lombok.Data;

@Data
public class AdminAuthDTO extends User {

	private Long adno;
	
	private String adminId;
	
	private String adminPw;
	
	private Set<String> roleSet;
	
	public AdminAuthDTO(Admin adminEn) {
		super(adminEn.getAdminId(), adminEn.getAdminPw(),
				adminEn.getSuperAuth().stream()
				.map((obj) -> new SimpleGrantedAuthority("ROLE-" + obj))
				.collect(Collectors.toSet()));
		this.adno = adminEn.getAdno();
		this.adminId = adminEn.getAdminId();
		this.adminPw = adminEn.getAdminPw();
		this.roleSet = adminEn.getSuperAuth().stream()
				.map((obj) -> new String(obj.name())).collect(Collectors.toSet());
	}
	
	public AdminAuthDTO(Long adno, String adminId, String adminPw, Set<String> roleSet) {
		super(adminId, adminPw,
				roleSet.stream()
				.map((obj) -> new SimpleGrantedAuthority("ROLE-" + obj))
				.collect(Collectors.toSet()));
		this.adno = adno;
		this.adminId = adminId;
		this.adminPw = adminPw;
		this.roleSet = roleSet;
	}
	
	public Map<String, Object> getClaims(){
		Map<String, Object> claims = new HashMap<String, Object>();
		claims.put("adno", adno);
		claims.put("adminId", adminId);
		claims.put("adminPw", adminPw);
		claims.put("roleSet", roleSet);
		
		return claims;
	}
}
