package com.fullstack.springboot.dto.member;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import com.fullstack.springboot.entity.member.Member;

import lombok.Data;
import lombok.extern.log4j.Log4j2;

@Data
@Log4j2
public class MemberAuthDTO extends User{
	private String email;
	private String password;
//	private String name;
//	private String id;
//	private String nickname;
//	private String def_addr;
//	private LocalDate birth;
//	private String m_phoNum;
//	private boolean isMan;
	private Set<String> roleSet;
	
	public MemberAuthDTO(Member memberEn) {
		super(memberEn.getM_email(),memberEn.getM_pw(),memberEn.getMembership().stream().map((obj)->new SimpleGrantedAuthority("ROLE-"+obj)).collect(Collectors.toSet()));
		this.email = memberEn.getM_email();
		this.password = memberEn.getM_pw();
//		this.name = memberEn.getM_name();
//		this.id = memberEn.getM_id();
//		this.nickname = memberEn.getM_nickName();
//		this.def_addr = memberEn.getDef_addr();
//		this.birth = memberEn.getBirth();
//		this.m_phoNum = memberEn.getM_phoNum();
//		this.isMan = memberEn.isMan();
		this.roleSet = memberEn.getMembership().stream().map((obj)-> new String(obj.name())).collect(Collectors.toSet());
		
		log.error("birth+++++++++++++++++++++++++++" + memberEn.getBirth());
	}
	public MemberAuthDTO(String email, String password, Set<String> roleSet) {
		super(email,password,roleSet.stream().map((obj)->new SimpleGrantedAuthority("ROLE-"+obj)).collect(Collectors.toSet()));
		this.email = email;
		this.password = password;
//		this.name = name;
//		this.id = id;
//		this.nickname = nickname;
//		this.def_addr = def_addr;
//		this.birth = birth;
//		this.m_phoNum = m_phoNum;
//		this.isMan = isMan;
		this.roleSet = roleSet;
	}
	public Map<String,Object> getClaims(){
		Map<String,Object> claims = new HashMap<String ,Object>();
		claims.put("email", email);
		claims.put("password", password);
//		claims.put("name", name);
//		claims.put("id", id);
//		claims.put("nickname", nickname);
//		claims.put("def_addr", def_addr);
		claims.put("roleSet", roleSet);
//		claims.put("birth", birth);
//		
//		log.error("birth" + birth);
		return claims;
	}
}