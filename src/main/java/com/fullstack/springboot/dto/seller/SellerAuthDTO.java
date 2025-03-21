package com.fullstack.springboot.dto.seller;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import com.fullstack.springboot.entity.seller.Seller;

import lombok.Data;

@Data
public class SellerAuthDTO extends User {
	
	private String sId;
	
	private String sPw;
	
	private Set<String> roleSet;
	
	public SellerAuthDTO(Seller sellerEn) {
		super(sellerEn.getS_id(), sellerEn.getS_pw(),
				sellerEn.getSuperAuth().stream()
				.map((obj) -> new SimpleGrantedAuthority("ROLE-" + obj))
				.collect(Collectors.toSet()));
		this.sId = sellerEn.getS_pw();
		this.sPw = sellerEn.getS_pw();
		this.roleSet = (sellerEn.getSuperAuth() != null) ?
                sellerEn.getSuperAuth().stream()
                        .map(Enum::name)
                        .collect(Collectors.toSet()) :
                new HashSet<>();
	}
	
	public SellerAuthDTO(String sId, String sPw, Set<String> roleSet) {
		super(sId, sPw,
				roleSet.stream()
				.map((obj) -> new SimpleGrantedAuthority("ROLE-" + obj))
				.collect(Collectors.toSet()));
		this.sId = sId;
		this.sPw = sPw;
		this.roleSet = roleSet;
	}
	
	public Map<String, Object> getClaims(){
		Map<String, Object> claims = new HashMap<String ,Object>();
		claims.put("sId", sId);
		claims.put("sPw", sPw);
		claims.put("roleSet", roleSet);
		
		return claims;
	}
}


