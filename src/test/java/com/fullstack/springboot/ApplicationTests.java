package com.fullstack.springboot;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.fullstack.springboot.dto.CartItemDTO;
import com.fullstack.springboot.dto.member.Membership;
import com.fullstack.springboot.entity.cart.Cart;
import com.fullstack.springboot.entity.cart.CartItem;
import com.fullstack.springboot.entity.member.Member;
import com.fullstack.springboot.entity.product.OptionDetail;
import com.fullstack.springboot.entity.product.Product;
import com.fullstack.springboot.entity.product.Seller;
import com.fullstack.springboot.repository.MemberRepository;
import com.fullstack.springboot.repository.SellerRespository;
import com.fullstack.springboot.util.RandomDateUtil;

import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;

@SpringBootTest
@Log4j2
class ApplicationTests {

	@Autowired
	private MemberRepository memberRepository;
	
	@Autowired
	private SellerRespository sellerRespository;
	
//	@Autowired
//	private ProductRepository productRepository;
//	
//	@Autowired
//	private OptionDetailRepository optionDetailRepository;
	
//	@Autowired
//	private CartRepository cartRepository;
//	
//	@Autowired
//	private CartItemRepository cartItemRepository;
//	
	//@Autowired
	//private CartService cartService;
	
	//@Test
//	void selCart() {
//		Optional<Cart> option = cartRepository.getCart(10L);
//		
//		if(option.isEmpty()) {
//			log.error("비었음");
//		}else {
//			log.error("카트");
//		}
//	}
	
	//@Test
	void insertMember() {
		
		Random random = new Random();
		
		LocalDate startDate = LocalDate.of(1950, Month.JANUARY, 1); //시작날
		LocalDate endDate = LocalDate.of(2014, Month.DECEMBER, 31); //종료날
		
		IntStream.rangeClosed(1, 100).forEach(i -> {
			
			LocalDate randomDate = RandomDateUtil.generateRandomDate(startDate, endDate); // 랜덤한 날짜 생성
			
			//비밀번호 암호화 아직임
			Member member = Member.builder()
					.m_id("user" + i )
					.m_pw("1111")
					.m_name("name" + i)
					.m_nickName("nickname" + i)
					.m_birth(randomDate)
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
		});
	}
	
	//@Test
//	void insertCart() {
//		Member member = Member.builder().mno(15L).build();
//		Cart cart = Cart.builder()
//				.member(member)
//				.build();
//		cartRepository.save(cart);
//	}
	
	//@Test
//	void insertCartItem() {
//		CartItemDTO cartItemDTO = CartItemDTO.builder()
//				.pno(101L)
//				.mno(99L)
//				.cno(7L)
//				.c_cnt(3)
//				.build();
//		
//		//List<CartItemDTO> cartItem = cartService.register(cartItemDTO);
//		
//		for(CartItemDTO dto : cartItem) {
//			log.error(dto);
//		}
//	}
	
	//@Test
	void insertSeller() {
		IntStream.rangeClosed(1, 30).forEach(i -> {
			Seller seller = Seller.builder()
					.s_id("seller" + i)
					.s_pw("2222")
					.businessName("company" + i)
					.businessNum("0000000000")
					.s_num(12345678)
					.s_email("seller" + i + "@sell.com")
					.s_addr("address" + i)
					.build();
			
			sellerRespository.save(seller);
		});
	}
	
	//@Test
	void insertProduct() {
		IntStream.rangeClosed(1, 100).forEach(i -> {
			Seller seller = Seller.builder().sno((long)((Math.random() * 30) + 1)).build();
//			Product product = Product.builder()
//					.p_name("상품" + i)
//					.p_content("이 상품은 상품" + i + "입니다")
//					.p_img("image.jpg")
//					.p_price(10000L)
//					.p_salePer(30)
//					.p_stock((int)((Math.random() * 30) + 1))
//					.p_salesVol((int)((Math.random() * 20) + 1))
//					.seller(seller)
//					.build();
//			
//			productRepository.save(product);
		});
	}
	
	//@Test
	//@Transactional
//	void selectCart() {
//		List<CartItemDTO> cartItemList = cartItemRepository.getCartItems(10L);
//		
//		for(CartItemDTO dto : cartItemList) {
//			log.info(dto);
//		}
//		
//	}
	
//	@Test
//	void insertOption() {
//		for(int i = 1; i <= 100; i++) {
//			int optionCnt = (int)((Math.random() * 4) + 1);
//			Long[] price = {1000L, 2000L, 3000L};
//			int totalStock = 0;
//			
//			Optional<Product> result = productRepository.getProductInfo((long)i);
//			Product product = result.orElseThrow();
//			Long pno = product.getPno();
//			if(optionCnt == 1) {
//				int stock = (int)((Math.random() * 20) + 1);
//				OptionDetail od = OptionDetail.builder()
//						.od_name(product.getP_name())
//						.od_stock(stock)
//						.od_price(price[(int)(Math.random() * 3)])
//						.product(product)
//						.build();
//				product.changeStock(stock);
//				optionDetailRepository.save(od);
//				productRepository.save(product);
//				continue;
//			}else {
//				for(int j = 1; j <= optionCnt; j++) {
//					int stock = (int)((Math.random() * 20) + 1);
//					OptionDetail od = OptionDetail.builder()
//							.od_name("상품" + i + " 옵션" + j)
//							.od_stock(stock)
//							.od_price(price[(int)(Math.random() * 3)])
//							.product(product)
//							.build();
//					optionDetailRepository.save(od);
//					totalStock += stock;
//				}
//			}
//			
//			product.changeStock(totalStock);
//			productRepository.save(product);
//		}
//	}

}
