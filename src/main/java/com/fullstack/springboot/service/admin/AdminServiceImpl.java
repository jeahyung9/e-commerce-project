package com.fullstack.springboot.service.admin;

import org.springframework.stereotype.Service;

import com.fullstack.springboot.dto.admin.AdminDTO;
import com.fullstack.springboot.entity.admin.Admin;
import com.fullstack.springboot.entity.member.Member;
import com.fullstack.springboot.repository.admin.AdminRepository;
import com.fullstack.springboot.repository.member.MemberRepository;
import com.fullstack.springboot.repository.product.CategoryRepository;
import com.fullstack.springboot.repository.product.ProductRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@Transactional
@Log4j2
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

	private final AdminRepository adminRepository;
	
	private final MemberRepository memberRepository;
	
	private final ProductRepository productRepository;
	
	private final CategoryRepository categoryRepository; //관리자 카테고리 추가
	
	@Override
	public void banUser(Long mno) {
		Member member = memberRepository.findById(mno).orElseThrow(()-> new RuntimeException("계정을 찾을수 없음"));
		
		member.banUser(); //isBan 값 true
		memberRepository.save(member);
	}

	@Override
	public void unBanUser(Long mno) {
		Member member = memberRepository.findById(mno).orElseThrow(()-> new RuntimeException("계정을 찾을수 없음"));
		
		member.unbanUser(); // isBan 값 false
		memberRepository.save(member);
		
	}

	@Override
	public void productStatus(Long pno, boolean status) {
		productRepository.findById(pno).orElseThrow(()-> new RuntimeException("해당 상품을 찾을 수 없음"));
		
		status = !status;
		
		productRepository.updateToDelFlag(pno, status);
	}

	@Override
	public AdminDTO findAdminById(String adminId){
		Admin admin = adminRepository.getAdminWithId(adminId);
		
		AdminDTO dto = entityToDTO(admin);

		return dto;
		
	}
	
}
