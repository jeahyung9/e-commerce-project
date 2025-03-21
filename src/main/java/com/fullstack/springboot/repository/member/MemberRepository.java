package com.fullstack.springboot.repository.member;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.fullstack.springboot.entity.member.Member;

import jakarta.transaction.Transactional;

public interface MemberRepository extends JpaRepository<Member, Long> {
	
	@Query("select m from Member m where m.m_email = :m_email")
	Member getMemberInfo(@Param("m_email") String m_email);
	
	@EntityGraph(attributePaths = "membership")
	@Transactional
	@Query("SELECT m FROM Member m WHERE m.m_email = :m_email")
	Member findByM_email(@Param("m_email") String m_email);
	
	@Query("select m from Member m where isBan = :check")
	Page<Member> getBanCheckList(@Param("check") boolean check, Pageable pageable);
	
	@Query("select m from Member m where isBan = :check and m.m_email like %:id%") // id 검색 (id를 없애서 email로 비교)
	Page<Member> getBanCheckList(@Param("check") boolean check, @Param("id") String id, Pageable pageable);

	@Query("select m from Member m")
	Page<Member> getBanNonCheckList(Pageable pageable);
	
	@Query("select m from Member m where m.m_email like %:id%") // id 검색 (id를 없애서 email로 비교)
	Page<Member> getBanNonCheckList(@Param("id") String id, Pageable pageable);
	
	@Query("SELECT m FROM Member m WHERE m.m_email = :m_email")
	Optional<Member> findBykakao(@Param("m_email") String m_email);
	
	@Query("SELECT m FROM Member m WHERE m.m_email = :m_email")
	Optional<Member> findByNaver(@Param("m_email") String m_email);

	@Query("SELECT m FROM Member m WHERE m.m_email = :m_email")
	Optional<Member> findByGoogle(@Param("m_email") String m_email);

}
