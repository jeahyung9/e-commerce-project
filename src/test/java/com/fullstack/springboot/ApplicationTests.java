package com.fullstack.springboot;

import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.fullstack.springboot.dto.member.Membership;
import com.fullstack.springboot.entity.cart.Cart;
import com.fullstack.springboot.entity.cart.CartItem;
import com.fullstack.springboot.entity.member.Member;
import com.fullstack.springboot.repository.MemberRepository;

@SpringBootTest
class ApplicationTests {

	@Autowired
	private MemberRepository memberRepository;
	
	@Test
	void insertMember() {
		IntStream.rangeClosed(1, 100).forEach(i -> {
			Member member = Member.builder()
					.m_id("hogeng" + i + "@abc.com")
					.m_pw("1111")
					.m_name("hogeng" + i)
					.m_nickName("호갱" + i)
					.build();
			
			member.addMembershipSet(Membership.USER);
			
			memberRepository.save(member);
		});
	}
	
	//@Test
	void insertMember2() {
		Member member = Member.builder()
				.m_id("jj@abc.com")
				.m_pw("1111")
				.m_name("jj")
				.m_nickName("제이제이")
				.build();
		
		member.addMembershipSet(Membership.BLACK);
		
		memberRepository.save(member);
	}
	
	//@Test
	void insertCart() {
		CartItem cartItem = CartItem.builder()
				.c_cnt(2)
				
	}

}
