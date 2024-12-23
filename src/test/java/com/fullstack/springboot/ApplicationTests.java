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
		
		Random random = new Random();
		
		LocalDate startDate = LocalDate.of(1950, Month.JANUARY, 1); //시작날
		LocalDate endDate = LocalDate.of(2014, Month.DECEMBER, 31); //종료날

		for(int i = 1; i <= 100; i++) {
			LocalDate randomDate = RandomDateUtil.generateRandomDate(startDate, endDate); // 랜덤한 날짜 생성
			
			//비밀번호 암호화 아직임
			Member member = Member.builder()
					.m_id("user" + i )
					.m_pw("1111")
					.m_name("name" + i)
					.m_nickName("nickname" + i)
					.birth(randomDate)
					.m_phoNum("010-0000-1111")
					.m_email("user" + i + "@aaa.com")
					.def_addr("주소" + i)
					.isMan(i % 2 == 0 ? false : true) // 성비를 50 : 50 으로
					.ad_agree(random.nextBoolean()) 
					.info_agree(random.nextBoolean()) // 약관 동의을 랜덤
					.build();
			
			if(i<=50) {
				member.addMembershipSet(Membership.USER); // 50%
			}
			else if(i>50 && i<=70) {
				member.addMembershipSet(Membership.SLIVER); // 20%
			}
			else if(i>70 && i<=85) {
				member.addMembershipSet(Membership.GOLD); // 15%
			}
			else if(i>85 && i<=95) {
				member.addMembershipSet(Membership.PLATINUM); // 10%
			}
			else if(i>95 && i<=100) {
				member.addMembershipSet(Membership.BLACK); // 5%
			}
			
			memberRepository.save(member);
		}
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
