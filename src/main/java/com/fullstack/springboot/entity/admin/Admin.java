package com.fullstack.springboot.entity.admin;

import java.util.HashSet;
import java.util.Set;

import com.fullstack.springboot.dto.admin.SuperAuth;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Builder
@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "admin_account")
public class Admin {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long adno;
	
	private String adminEmail;
	
	private String adminId;
	
	private String adminPw;
	
	private String name;
	
	@ElementCollection(fetch = FetchType.LAZY)
	@Builder.Default
	private Set<SuperAuth> superAuth = new HashSet<SuperAuth>(Set.of(SuperAuth.ADMIN));
	
	public void addSuperAuthSet(SuperAuth adminAuth) {
		superAuth.add(adminAuth);
	}
}
