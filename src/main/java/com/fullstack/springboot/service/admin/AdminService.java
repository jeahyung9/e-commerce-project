package com.fullstack.springboot.service.admin;

import com.fullstack.springboot.dto.admin.AdminDTO;
import com.fullstack.springboot.entity.admin.Admin;

public interface AdminService { // 카테고리는 관리자만 수정 등록 삭제 가능
	
	void banUser(Long mno);
	
	void unBanUser(Long mno);
	
	void productStatus(Long pno, boolean status);

	AdminDTO findAdminById(String adminId);

	default AdminDTO entityToDTO(Admin admin) {
		AdminDTO adminDTO = AdminDTO.builder()
				.adno(admin.getAdno())
				.adminEmail(admin.getAdminEmail())
				.adminId(admin.getAdminId())
				.adminPw(admin.getAdminPw())
				.name(admin.getName())
				.build();
		return adminDTO;
	}
}
