package com.fullstack.springboot.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.fullstack.springboot.dto.member.MemberDTO;
import com.fullstack.springboot.entity.member.Member;

import jakarta.transaction.Transactional;

public interface MemberRepository extends JpaRepository<Member, Long> {
	
	@Query("select m from Member m where m.m_id = :id")
	Member getMemberInfo(@Param("id") String id);
	
	@EntityGraph(attributePaths = "membership")
	@Transactional
	@Query("SELECT m FROM Member m WHERE m.m_email = :m_email")
	Member findByM_email(@Param("m_email") String m_email);
	
	
	
    
	//boolean existsByMId(String m_id);
	
	//MemberDTO findByMEmailAndPassword(String m_email, String password);
}
