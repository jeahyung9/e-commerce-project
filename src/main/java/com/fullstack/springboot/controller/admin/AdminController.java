package com.fullstack.springboot.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fullstack.springboot.service.AdminService;

@RestController
@RequestMapping("/admin")
public class AdminController {
	
	@Autowired
	private AdminService adminService;
	
	@PutMapping("/ban/{mno}")
	public String banUser(@PathVariable Long mno) {
		adminService.banUser(mno);
		return "밴 처리 완료";
	}
	
	@PutMapping("/unban/{mno}")
	public String unbanUser(@PathVariable Long mno) {
		adminService.unBanUser(mno);
		return "밴 해제 완료";
	}
}
