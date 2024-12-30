package com.fullstack.springboot.service;

public interface AdminService {
	
	void banUser(Long mno);
	
	void unBanUser(Long mno);
}
