package com.fullstack.springboot.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fullstack.springboot.entity.member.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {

}
