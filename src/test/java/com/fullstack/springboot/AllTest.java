package com.fullstack.springboot;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.fullstack.springboot.dto.admin.SuperAuth;
import com.fullstack.springboot.dto.member.Membership;
import com.fullstack.springboot.entity.admin.Admin;
import com.fullstack.springboot.entity.admin.Faq;
import com.fullstack.springboot.entity.admin.Notice;
import com.fullstack.springboot.entity.member.Member;
import com.fullstack.springboot.entity.order.OrderDetail;
import com.fullstack.springboot.entity.product.Category;
import com.fullstack.springboot.entity.product.CategoryProductBumper;
import com.fullstack.springboot.entity.product.OptionDetail;
import com.fullstack.springboot.entity.product.Product;
import com.fullstack.springboot.entity.seller.Seller;
import com.fullstack.springboot.repository.admin.AdminRepository;
import com.fullstack.springboot.repository.admin.FaqRepository;
import com.fullstack.springboot.repository.admin.NoticeRepository;
import com.fullstack.springboot.repository.member.MemberRepository;
import com.fullstack.springboot.repository.order.OrderDetailRepository;
import com.fullstack.springboot.repository.product.CategoryProductRepository;
import com.fullstack.springboot.repository.product.CategoryRepository;
import com.fullstack.springboot.repository.product.OptionDetailRepository;
import com.fullstack.springboot.repository.product.ProductRepository;
import com.fullstack.springboot.repository.seller.SellerRepository;
import com.fullstack.springboot.util.RandomDateUtil;

import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;

@SpringBootTest
@Log4j2
public class AllTest {
	
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
	private FaqRepository faqRepository;
	
	@Autowired
	private OptionDetailRepository optionDetailRepository; 
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private OrderDetailRepository orderDetailRepository;
	
	@Test
	void insertMember() {
		
		Random random = new Random();
		
		LocalDate startDate = LocalDate.of(1950, Month.JANUARY, 1); //시작날
		LocalDate endDate = LocalDate.of(2014, Month.DECEMBER, 31); //종료날

		for(int i = 1; i <= 100; i++) {
			LocalDate randomDate = RandomDateUtil.generateRandomDate(startDate, endDate); // 랜덤한 날짜 생성
			String encodedPassword = passwordEncoder.encode("1111");
			Member member = Member.builder()
					.m_pw(encodedPassword)
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
	
	@Test
	void insertSeller() {
		String encodedPassword = passwordEncoder.encode("1111");
		for(int i = 1; i <= 5; i++) {
			Seller seller = Seller.builder()
					.s_id("seller" + i)
					.s_pw(encodedPassword)
					.businessName("픽앤딜")
					.businessNum("0000"+i)
					.s_num("032-" + i + i + i + "-" + i + i + i + i)
					.s_email("seller" + i + "@aaa.com")
					.s_addr("판매자 주소" + i)
					.build();
			
			sellerRepository.save(seller);
		}
	}
	
	@Test 
	@Transactional
	void addSellerAuth() {
		for(int i = 1; i <= 5; i++) {
			Seller seller = sellerRepository.findById((long)i).get();
			seller.addSuperAuthSet(SuperAuth.SELLER);
			sellerRepository.save(seller);
		}
	}
	
//	@Test
	void insertProduct() { // 이미지 없음
		Random random = new Random();
		
		for(int i = 1; i <= 100; i++) {
			int randomSale = random.nextInt(15); // 0~14, 최대 70% 할인
			
			Seller sellerNo = Seller.builder().sno((long)random.nextInt(1, 6)).build();
			
			OptionDetail optionDetail = OptionDetail.builder().odno((long)random.nextInt(1, 100)).build();
			
			Product product = Product.builder()
					.p_name("상품" + i)
					.p_content("상품 설명" + i)
					.p_price((long)(i * 100))
					.p_salePer(randomSale * 5)
					.p_stock(random.nextInt(50, 100)) // 50 ~ 200개의 랜덤한 재고수
					.p_salesVol(random.nextInt(10, 50))
					.seller(sellerNo)
					.delFlag(false)
					.build();
			
			productRepository.save(product);
		}
	}
	
//	@Test
	void insertOptionDetail() {
		
		Random random = new Random();
		
		for(int i = 1; i <=100; i++) {
			Product product = Product.builder().pno((long)i).build();
			
			for(int j = 1; j < 4; j++) {
				
				int randomPrice = random.nextInt(10);
				
				OptionDetail optionDetail = OptionDetail.builder()
						.od_name("상품 " + i + "옵션 " + j)
						.od_stock(random.nextInt(50))
						.od_price((long)(randomPrice * 100))
						.od_salesVol(random.nextInt(10, 50))
						.product(product)
						.build();
				
				optionDetailRepository.save(optionDetail);
			}
		}
	}
	
//	@Test
	void insertAdmin() {
		
		 String encodedPassword = passwordEncoder.encode("1111");
		
		for(int i = 4; i <= 4; i++) {
			Admin admin = Admin.builder()
					.adminId("admin" + i)
					.adminEmail("admin" + i + "@aaa.com")
					.adminPw(encodedPassword)
					.name("ad_name" + i)
					.build();
			
			adminRepository.save(admin);
		}
	}
	
//	@Test 
//	@Transactional
	void addAdminAuth() {
		for(int i = 1; i <= 3; i++) {
			Admin admin = adminRepository.findById((long)i).get();
			admin.addSuperAuthSet(SuperAuth.ADMIN);
			adminRepository.save(admin);
		}
	}
	
//	@Test
	void insertNotice() {
		Random random = new Random();
		for(int i = 1; i <= 100; i++) {
			Admin admin = Admin.builder().adno((long)random.nextInt(1, 4)).build();
			
			Notice notice = Notice.builder()
					.title("공지사항" + i)
					.content("공지 내용" + i)
					.admin(admin)
					.build();
			
			noticeRepository.save(notice);
		}
	}
	
//	@Test
	void insertCategory() {
		
		//상위 5개 중간 5개 각 중간별로 4개씩 100개
		
		for(int i = 1; i<=5; i++) {
			Category superCate = Category.builder()
					.cateName("superCate" + i)
					.cateDepth(1)
					.build();
			categoryRepository.save(superCate);
			for(int j = 1; j<=5; j++) {
				Category middleCate = Category.builder()
						.cateName("middleCate" + j)
						.superCate(superCate)
						.cateDepth(2)
						.build();
				categoryRepository.save(middleCate);
				for(int k = 1; k<=4; k++) {
					Category lowCate = Category.builder()
							.cateName("lowCate" + k)
							.superCate(middleCate)
							.cateDepth(3)
							.build();
					categoryRepository.save(lowCate);
				}
			}
		}
	}

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
    
//    @Test
    void insertFAQ() {
    	
    	Random random = new Random();
    	
    	for(int i = 1; i <= 200; i++) {
    		Admin admin = Admin.builder().adno((long)random.nextInt(1, 4)).build();
    		
    		Faq faq = Faq.builder()
    				.question("자주 묻는 질문 " + i)
    				.answer("답변 " + i).
    				admin(admin)
    				.build();
    		
    		faqRepository.save(faq);
    	}
    }
	

	
//	@Test
//	@Transactional
	void searchProduct() {
		Long no = 3L;
		
		Optional<Product> productInfo = productRepository.findById(no);
		
		log.error(productInfo);
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
		Long ctno = 2L;
	     
	    Category category = categoryRepository.getCateWithCateList(ctno);
	     
	     
	    log.error(category);
	     
	    category.getCateList().forEach(cate -> {
	          log.error(cate);  // 각 하위 카테고리를 출력
	     });
	}
	
//	@Test
//	@Transactional
	void loadCateList() {
		List<Category> categories = categoryRepository.getAllCateList();
		
		categories.forEach(category->{
			log.warn(category);
			category.getCateList().forEach(cate->{
				log.error(cate);
			});
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
	void updateProductStockAndSalesVol() {		
		
		for(int i = 1; i <= 100; i++) {
			productRepository.updateProductStockByOptionDetail((long)i);
			productRepository.updateProductSalesVolByOptionDetail((long)i);
		}
		
		List<Product> proList = productRepository.findAll();
		
		proList.forEach(pro -> log.error("재고 : " + pro.getP_stock() + "판매량 : " + pro.getP_salesVol()));
	}
	
	
	

//	@Test
//	@Transactional
	void selectNotice() {
		
		log.error("start");
		
		List<Notice> list = noticeRepository.findAll();
		list.forEach(l -> log.error(l));
	}
	
//	@Test
//	@Transactional
	void getOptionDetail() {
		log.error("start");
		
		Long pno = 99L;
		
		List<OptionDetail> list = optionDetailRepository.getOptByPno(pno);
		
		list.forEach(opt->log.error("옵션 상세 : " + opt));
	}
	
//	@Test
//	@Transactional
	void testLowerCate() {
		
		Pageable pageable = 
	            PageRequest.of(
	                  0, 
	                  10,
	                  Sort.by("pno").descending());
		
		Page<Product> page = categoryProductRepository.getProductsInDepth1Category(1L, pageable);
		
		System.out.println(page);
	}
	
	
//	@Test
//	@Transactional
	void getodOne() {
		OrderDetail od = orderDetailRepository.getOrderDetailOne(1L);
		System.out.println(od);
	}

}
