package com.fullstack.springboot.dto.member;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import com.fullstack.springboot.entity.member.Member;

import lombok.Data;

@Data
public class MemberAuthDTO extends User{
	private String email;
	private String password;
	private Set<String> roleSet;
	
	public MemberAuthDTO(Member memberEn) {
		super(memberEn.getM_email(),memberEn.getM_pw(),memberEn.getMembership().stream().map((obj)->new SimpleGrantedAuthority("ROLE-"+obj)).collect(Collectors.toSet()));
		this.email = memberEn.getM_email();
		this.password = memberEn.getM_pw();
		this.roleSet = memberEn.getMembership().stream().map((obj)-> new String(obj.name())).collect(Collectors.toSet());
	}
	public MemberAuthDTO(String email, String password, Set<String> roleSet) {
		super(email,password,roleSet.stream().map((obj)->new SimpleGrantedAuthority("ROLE-"+obj)).collect(Collectors.toSet()));
		this.email = email;
		this.password = password;
		this.roleSet = roleSet;
	}
	public Map<String,Object> getClaims(){
		Map<String,Object> claims = new HashMap<String ,Object>();
		claims.put("email", email);
		claims.put("password", password);
		claims.put("roleSet", roleSet);
		
		return claims;
	}
}