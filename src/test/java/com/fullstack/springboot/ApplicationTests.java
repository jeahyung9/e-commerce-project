package com.fullstack.springboot;

import java.time.LocalDate;
import java.time.Month;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.fullstack.springboot.dto.member.MemberDTO;
import com.fullstack.springboot.dto.member.Membership;
import com.fullstack.springboot.entity.admin.Admin;

import com.fullstack.springboot.entity.admin.Notice;
import com.fullstack.springboot.entity.member.Member;
import com.fullstack.springboot.entity.product.Category;
import com.fullstack.springboot.entity.product.CategoryProductBumper;
import com.fullstack.springboot.entity.product.Product;
import com.fullstack.springboot.entity.product.Seller;
import com.fullstack.springboot.repository.AdminRepository;
import com.fullstack.springboot.repository.CategoryProductRepository;
import com.fullstack.springboot.repository.CategoryRepository;

import com.fullstack.springboot.repository.MemberRepository;
import com.fullstack.springboot.repository.NoticeRepository;
import com.fullstack.springboot.repository.ProductRepository;
import com.fullstack.springboot.repository.SellerRepository;
import com.fullstack.springboot.service.MemberService;
import com.fullstack.springboot.util.RandomDateUtil;

import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;

@SpringBootTest
@Log4j2
class ApplicationTests {
	
	@Autowired
	private MemberRepository memberRepository;
	
	@Autowired
	private SellerRepository sellerRepository;
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private AdminRepository adminRepository;
	
	@Autowired
	private NoticeRepository noticeRepository;
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	@Autowired
	private CategoryProductRepository categoryProductRepository; 
	
	@Autowired
	private MemberService memberService;

	
	
	

	//@Test
//	void insertMember() {
//		
//		Random random = new Random();
//		
//		LocalDate startDate = LocalDate.of(1950, Month.JANUARY, 1); //시작날
//		LocalDate endDate = LocalDate.of(2014, Month.DECEMBER, 31); //종료날
//
//		for(int i = 1; i <= 100; i++) {
//			LocalDate randomDate = RandomDateUtil.generateRandomDate(startDate, endDate); // 랜덤한 날짜 생성
//			
//			//비밀번호 암호화, 멤버쉽 랭크를 아직 안줌
//			Member member = Member.builder()
//					.m_id("user" + i )
//					.m_pw("1111")
//					.m_name("name" + i)
//					.m_nickName("nickname" + i)
//					.birth(randomDate)
//					.m_phoNum("010-0000-1111")
//					.m_email("user" + i + "@aaa.com")
//					.def_addr("주소" + i)
//					.isMan(i % 2 == 0 ? false : true) // 성비를 50 : 50 으로
//					.ad_agree(random.nextBoolean()) 
//					.info_agree(random.nextBoolean()) // 약관 동의을 랜덤
//					.build();
//			
//			if(i<=50) {
//				member.addMembershipSet(Membership.USER); // 50%
//			}
//			else if(i>50 && i<=70) {
//				member.addMembershipSet(Membership.SLIVER); // 20%
//			}
//			else if(i>70 && i<=85) {
//				member.addMembershipSet(Membership.GOLD); // 15%
//			}
//			else if(i>85 && i<=95) {
//				member.addMembershipSet(Membership.PLATINUM); // 10%
//			}
//			else if(i>95 && i<=100) {
//				member.addMembershipSet(Membership.BLACK); // 5%
//			}
//			
//			memberRepository.save(member);
//		}
//		
//	}
	
////	@Test
//	void insertSeller() {
//		
//		for(int i = 1; i <= 5; i++) {
//			Seller seller = Seller.builder()
//					.s_id("seller" + i)
//					.s_pw("1111")
//					.businessName("store" + i)
//					.businessNum("0000"+i)
//					.s_num("032-" + i + i + i + "-" + i + i + i + i)
//					.s_email("seller" + i + "@aaa.com")
//					.s_addr("판매자 주소" + i)
//					.build();
//			
//			sellerRepository.save(seller);
//		}
//	}
//	
////	@Test
//	void insertProduct() { // 이미지 없음
//		Random random = new Random();
//		
//		for(int i = 1; i <= 100; i++) {
//			int randomSale = random.nextInt(15); // 0~14, 최대 70% 할인
//			
//			Seller sellerNo = Seller.builder().sno((long)random.nextInt(1, 6)).build();
//			
//			
//			
//			Product product = Product.builder()
//					.p_name("상품" + i)
//					.p_content("상품 설명" + i)
//					.p_price((long)(i * 100))
//					.p_salePer(randomSale * 5)
//					.p_stock(random.nextInt(50, 100)) // 50 ~ 200개의 랜덤한 재고수
//					.p_salesVol(random.nextInt(10, 50))
//					.seller(sellerNo)
//					.build();
//			
//			productRepository.save(product);
//		}
//	}
//	
////	@Test
//	void insertAdmin() {
//		
//		for(int i = 1; i <= 3; i++) {
//			Admin admin = Admin.builder()
//					.adminId("admin" + i)
//					.adminEmail("admin" + i + "@aaa.com")
//					.adminPw("1111")
//					.name("ad_name" + i)
//					.build();
//			
//			adminRepository.save(admin);
//		}
//	}
////	@Test
//	void insertNotice() {
//		Random random = new Random();
//		for(int i = 1; i <= 10; i++) {
//			Admin admin = Admin.builder().adno((long)random.nextInt(1, 4)).build();
//			
//			Notice notice = Notice.builder()
//					.title("공지사항" + i)
//					.content("공지 내용" + i)
//					.admin(admin)
//					.build();
//			
//			noticeRepository.save(notice);
//		}
//	}
//	
////	@Test
//	void insertCategory() {
//		
//		//상위 5개 중간 5개 각 중간별로 4개씩 100개
//		
//		for(int i = 1; i<=5; i++) {
//			Category superCate = Category.builder()
//					.cateName("superCate" + i)
//					.cateDepth(1)
//					.build();
//			categoryRepository.save(superCate);
//			for(int j = 1; j<=5; j++) {
//				Category middleCate = Category.builder()
//						.cateName("middleCate" + j)
//						.superCate(superCate)
//						.cateDepth(2)
//						.build();
//				categoryRepository.save(middleCate);
//				for(int k = 1; k<=4; k++) {
//					Category lowCate = Category.builder()
//							.cateName("lowCate" + k)
//							.superCate(middleCate)
//							.cateDepth(3)
//							.build();
//					categoryRepository.save(lowCate);
//				}
//			}
//		}
//	}

//	@Test
    void insertSetCateForProduct() {
       
       Random random = new Random();
       
       for(int i = 1; i <= 100; i++) {
          
          List<Long> ctnoList = categoryRepository.getCtnoWithDepth(3);
          int randomIndexNum = random.nextInt(ctnoList.size()); 
          Long randomCtno = ctnoList.get(randomIndexNum);
          
          Category category = Category.builder().ctno(randomCtno).build();
          Product product = Product.builder().pno((long)i).build();
          
          CategoryProductBumper cpBumper = CategoryProductBumper.builder()
                            .category(category)
                            .product(product)
                            .build();
          
          categoryProductRepository.save(cpBumper);
       }
    }
    
	
//	@Test
//	@Transactional
	void searchMember() {
		String id = "user65";
		
		Member memberInfo = memberRepository.getMemberInfo(id);
		
		log.error(memberInfo);
	}
	
//	@Test
//	@Transactional
	void searchProduct() {
		Long no = 3L;
		
		Product productInfo = productRepository.getProductInfo(no);
		
		log.error(productInfo);
	}
	
//	@Test
//	@Transactional
	void searchSeller() {
		Long no = 3L;
		
		Seller sellerInfo = sellerRepository.getSellerInfo(no);
		
		log.error(sellerInfo);
	}
	
//	@Test
//	@Transactional
	void getNoticeList() {
		
		Long adno = 3L;
		
		Object[] noticeList = noticeRepository.getNoticeListByAdmin(adno);
		
		log.error(noticeList);
	}
	
//	@Test 
//	@Transactional
	void searchCategory() {
		Long ctno = 1L;
	     
	    Category category = categoryRepository.getCateWithCateList(ctno);
	     
	     
	    log.error(category);
	     
	    category.getCateList().forEach(cate -> {
	          log.error(cate);  // 각 하위 카테고리를 출력
	     });
	}
	
//	@Test
//	@Transactional
	void searchCategoryByDepth() {
		
		List<Category> lowerCateList = categoryRepository.getCateWithDepth(3);
	      
	    lowerCateList.forEach(cate ->{
	       log.error(cate);
	    });
	}
	
//	@Test
//	@Transactional
	void searchCtnoByDepth() {
		List<Long> ctno = categoryRepository.getCtnoWithDepth(3);
	      
	    log.error(ctno);

	}
	
//	@Test
//	@Transactional
	void getProductListWithCate() {
		List<CategoryProductBumper> proListWithCate = categoryProductRepository.getProductListWithCategory();
		
	    proListWithCate.forEach(proInfo ->{
	       log.error(proInfo);
	    });
	}
//	@Test
//	@Transactional
	void findAdminByAdno() {
		Admin admin = adminRepository.getAdminById("admin1");
		
		log.error("어드민 : " + admin);
	}
	
	@Test
	void createMember() {
		Set<Membership> membership = new HashSet<Membership>();
		
		LocalDate startDate = LocalDate.of(1950, Month.JANUARY, 1); //시작날
		LocalDate endDate = LocalDate.of(2014, Month.DECEMBER, 31); //종료날
		LocalDate randomDate = RandomDateUtil.generateRandomDate(startDate, endDate);
		
		membership.add(Membership.USER);
		MemberDTO memberdto = MemberDTO.builder()
				.m_id("good4")
				.m_pw("1111")
				.m_name("jason")
				.m_nickname("jason1")
				.m_phoNum("000-0000-0000")
				.m_email("good4@a.com")
				.birth(randomDate)
				.def_addr("dddddddd")
				.isMan(false)
				.ad_agree(false)
				.info_agree(false)
				.isBan(false)
				.totalPay(0L)
				.membership(membership)
				.build();
		
		
		MemberDTO dto = memberService.createMember(memberdto);
	}
	@Autowired
	PasswordEncoder pwEncoder;
	//@Test
	void testing() {
		Member mem = memberRepository.findById(129L).get();
		String encoded = pwEncoder.encode("1234");
		System.out.println(encoded);
		mem.setPw(encoded);
		memberRepository.save(mem);
		
	}

}
