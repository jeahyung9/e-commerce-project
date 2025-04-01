package com.fullstack.springboot;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;

import com.fullstack.springboot.dto.qa.ProQAReplyDTO;
import com.fullstack.springboot.dto.qa.ProductQADTO;
import com.fullstack.springboot.entity.member.Member;
import com.fullstack.springboot.entity.product.Product;
import com.fullstack.springboot.entity.qa.ProQAReply;
import com.fullstack.springboot.entity.qa.ProductQA;
import com.fullstack.springboot.entity.seller.Seller;
import com.fullstack.springboot.repository.qa.ProductQAReplyRepository;
import com.fullstack.springboot.repository.qa.ProductQARepository;

import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;

@SpringBootTest
@Log4j2
public class QATest {
	
	@Autowired
	private ProductQARepository productQARepository;
	
	@Autowired
	private ProductQAReplyRepository productQAReplyRepository;
	
	//@Test
	void insertQA() {
		String[] title = {
	            "상품에 대한 자세한 설명 부탁드립니다.",
	            "이 상품은 재입고 되나요?",
	            "배송 기간은 얼마나 걸리나요?",
	            "상품 사이즈는 어떻게 되나요?",
	            "제품 색상 선택이 가능한가요?",
	            "이 제품의 가격은 변동이 있나요?",
	            "상품의 사용법에 대해 궁금합니다.",
	            "제품에 대한 보증 기간은 얼마나 되나요?",
	            "상품이 유통기한이 있나요?",
	            "해당 상품은 국제 배송이 가능한가요?",
	            "상품의 재고 상황을 확인하고 싶습니다.",
	            "이 상품은 어떤 재질로 만들어졌나요?",
	            "제품의 교환 및 반품 정책을 알고 싶어요.",
	            "이 상품은 세일 중인가요?",
	            "제품에 포함된 액세서리가 무엇인지 궁금합니다.",
	            "상품 구매 시 포인트 적립이 가능한가요?",
	            "이 제품은 무엇으로 사용하나요?",
	            "상품에 대한 리뷰는 어떻게 확인하나요?",
	            "해당 제품의 제조사는 어디인가요?",
	            "이 상품의 원산지는 어디인가요?",
	            "상품을 다른 방법으로 결제할 수 있나요?",
	            "이 상품은 어떻게 사용하나요?",
	            "제품에 대한 상세 스펙을 알고 싶습니다.",
	            "상품에 대한 추가 옵션이 있나요?",
	            "이 상품을 선물용으로 포장할 수 있나요?",
	            "상품 구매 시 배송비는 얼마인가요?",
	            "이 상품의 고객 후기 중 가장 인상 깊은 것은 무엇인가요?",
	            "상품의 원산지와 제조일자는 어떻게 되나요?",
	            "이 상품은 어디에서 생산되었나요?",
	            "상품을 사용할 때 주의사항이 있나요?",
	            "상품에 포함된 구성품은 무엇인가요?",
	            "이 상품은 한정판인가요?",
	            "해당 상품은 다른 색상이나 디자인이 있나요?",
	            "제품의 사용 기간이나 유효기간은 어떻게 되나요?",
	            "이 상품의 배송 추적 번호는 어떻게 확인하나요?",
	            "상품의 상세 사진을 추가로 요청할 수 있나요?",
	            "이 상품은 새 제품인가요, 중고 제품인가요?",
	            "상품의 품질 보증은 어떻게 이루어지나요?",
	            "이 상품의 특징은 무엇인가요?",
	            "제품을 한 번에 많이 구매하면 할인이 되나요?",
	            "상품의 크기와 무게는 어떻게 되나요?",
	            "이 제품은 다른 국가로 배송이 가능한가요?",
	            "상품에 포함된 설명서가 있나요?",
	            "이 제품을 여러 개 구매할 때 추가 할인 혜택이 있나요?",
	            "상품에 대한 전문가의 리뷰나 평가는 어떤가요?",
	            "해당 상품을 다른 색상으로 교환할 수 있나요?",
	            "이 상품은 기프트 카드로 구매할 수 있나요?",
	            "상품을 선물로 보내고 싶습니다. 가능한가요?",
	            "이 제품은 어린이에게 안전한가요?",
	            "상품에 대해 궁금한 점이 있습니다."
	        };
		String[] content = {
			    "상품에 대해 더 자세히 알고 싶습니다. 예를 들어, 기능, 크기, 사용법 등 자세한 설명을 부탁드립니다.",
			    "이 상품은 재입고 예정이 있나요? 재입고 일정이나 알림 기능이 있다면 알려주세요.",
			    "상품을 주문하고 싶은데, 배송에 걸리는 기간은 얼마나 되나요? 지역에 따라 다를 수 있나요?",
			    "이 상품은 사이즈가 어떻게 되나요? 예시로, 옷, 신발, 가방 등 다양한 카테고리에 대해 궁금합니다.",
			    "이 상품에는 여러 색상 옵션이 있나요? 내가 원하는 색을 선택할 수 있나요?",
			    "현재 가격은 고정 가격인가요? 할인이나 프로모션이 적용될 예정이 있나요?",
			    "이 상품은 사용 방법에 대해 상세히 알고 싶습니다. 시작 방법이나 주의사항 등을 알려주세요.",
			    "이 제품에 대한 보증 기간은 얼마나 되나요? 보증 범위나 조건도 알고 싶습니다.",
			    "상품에 유통기한이나 만료일이 있나요? 해당 정보를 제공해 주세요.",
			    "해외로 이 상품을 배송받을 수 있나요? 국제 배송이 가능하다면, 배송 비용과 기간은 어떻게 되나요?",
			    "현재 이 상품의 재고는 충분한가요? 빠른 시일 내에 구매할 수 있을지 궁금합니다.",
			    "이 상품은 어떤 재질로 만들어졌나요? 예를 들어, 의류라면 소재, 가방이라면 외부 재질을 알고 싶습니다.",
			    "상품의 교환 및 반품 정책에 대해 알고 싶습니다. 교환이나 환불이 가능하다면, 어떻게 진행되나요?",
			    "이 상품은 세일 중인가요? 세일이 끝나면 가격은 어떻게 되나요?",
			    "이 제품에는 어떤 액세서리가 포함되어 있나요? 혹시 별도로 구매할 수 있는 액세서리가 있나요?",
			    "상품을 구매하면 포인트 적립이 가능한가요? 포인트 사용 방법도 함께 알고 싶습니다.",
			    "이 제품은 어떤 용도로 사용되는 건가요? 해당 제품의 활용 사례를 알려주세요.",
			    "상품에 대한 사용자 리뷰를 어떻게 볼 수 있나요? 다른 사람들의 피드백을 보고 싶습니다.",
			    "이 제품의 제조사는 어디인가요? 어떤 브랜드인지 궁금합니다.",
			    "이 상품의 원산지는 어디인가요? 제조지와 관련된 정보를 알려주세요.",
			    "이 상품은 다른 방법으로 결제할 수 있나요? 예를 들어, 카드, 간편 결제 등 다양한 결제 옵션이 있을지 궁금합니다.",
			    "이 상품은 사용하기 어려운 점은 없나요? 어떤 방식으로 사용해야 할지 궁금합니다.",
			    "이 제품의 상세 스펙을 알 수 있을까요? 제품의 기능, 크기, 성능 등 자세히 알고 싶습니다.",
			    "이 상품은 추가적인 옵션이나 패키지가 있나요? 선택할 수 있는 다른 버전이 있으면 알려주세요.",
			    "이 상품을 선물용으로 포장할 수 있나요? 포장 옵션이나 선물용으로 제공되는 서비스가 있는지 궁금합니다.",
			    "상품을 구매하면 배송비는 얼마인가요? 무료 배송 서비스가 제공되는지 알고 싶습니다.",
			    "이 상품의 고객 후기 중 가장 인상 깊은 후기는 어떤 것이었나요? 다른 고객들의 경험을 알고 싶습니다.",
			    "이 상품의 원산지와 제조일자에 대해 정확히 알 수 있을까요? 구매 시 참고할 수 있으면 좋겠습니다.",
			    "이 상품은 어디에서 생산되었나요? 생산 국가나 생산 공장을 알고 싶습니다.",
			    "이 상품을 사용할 때 주의해야 할 점이 있나요? 안전하게 사용하려면 무엇을 조심해야 할지 궁금합니다.",
			    "상품에 포함된 구성품은 무엇인가요? 예를 들어, 번들로 제공되는 다른 제품이나 액세서리가 있는지 알고 싶습니다.",
			    "이 상품은 한정판으로 판매되고 있나요? 수량이 한정된 제품이라면 언제 품절될지 궁금합니다.",
			    "이 상품은 다른 색상이나 디자인 옵션이 있나요? 선택할 수 있는 다양한 옵션이 있다면 알려주세요.",
			    "제품의 사용 기간이나 유효 기간은 어떻게 되나요? 예를 들어, 배터리 수명, 사용 가능 기간 등 궁금합니다.",
			    "이 상품의 배송 추적 번호를 어떻게 확인할 수 있나요? 배송 상태를 실시간으로 추적하고 싶습니다.",
			    "상품의 상세 사진을 추가로 요청할 수 있을까요? 제품의 디테일을 더 잘 보고 싶습니다.",
			    "이 상품은 새 제품인가요, 아니면 중고 제품인가요? 제품 상태를 정확히 알고 싶습니다.",
			    "이 상품의 품질 보증은 어떻게 이루어지나요? 보증 기간과 보증 범위에 대해 알고 싶습니다.",
			    "이 상품의 특징은 무엇인가요? 다른 유사 제품들과 비교했을 때 이 제품의 장점은 무엇인지 궁금합니다.",
			    "이 제품을 한 번에 많이 구매하면 할인이 되나요? 대량 구매 시 할인 혜택이 있는지 알고 싶습니다.",
			    "상품의 크기와 무게는 어떻게 되나요? 배송을 고려할 때 정확한 크기와 무게가 궁금합니다.",
			    "이 제품은 다른 국가로 배송이 가능한가요? 해외 배송이 가능한지 확인하고 싶습니다.",
			    "상품에 포함된 설명서가 있나요? 제품 사용에 필요한 설명서나 가이드가 있는지 궁금합니다.",
			    "이 제품을 여러 개 구매할 때 추가 할인 혜택이 있나요? 예를 들어, 2개 이상 구매 시 할인 등이 있는지 알고 싶습니다.",
			    "상품에 대한 전문가의 리뷰나 평가는 어떤가요? 전문가들이 이 제품을 어떻게 평가하는지 궁금합니다.",
			    "해당 상품을 다른 색상으로 교환할 수 있나요? 색상 변경이 가능하다면 절차나 조건을 알고 싶습니다.",
			    "이 상품은 기프트 카드로 구매할 수 있나요? 기프트 카드 사용이 가능한지 궁금합니다.",
			    "상품을 선물로 보내고 싶습니다. 가능한가요? 선물 포장이나 직접 배송이 가능한지 알고 싶습니다.",
			    "이 제품은 어린이에게 안전한가요? 어린이가 사용할 수 있는 제품인지 안전성에 대해 궁금합니다.",
			    "상품에 대해 궁금한 점이 있습니다. 해당 제품에 대한 추가적인 정보가 필요합니다."
			};
		
		for(int i = 1; i <= 1; i++) {
			//Member member = Member.builder().mno((long)((Math.random() * 99) + 1)).build();
			Member member = Member.builder().mno(101L).build();
			//Product product = Product.builder().pno((long)((Math.random() * 2700) + 101)).build();
			Product product = Product.builder().pno(2800L).build();
			ProductQA qa = ProductQA.builder()
					.title(title[(int)(Math.random() * title.length)])
					.content(content[(int)(Math.random() * title.length)])
					.replyCheck(false)
					.secret(false)
					.member(member)
					.product(product)
					.build();
			try {
				productQARepository.save(qa);				
			} catch (Exception e) {
				System.out.println(e);
			}
		}
	}
	
	//@Test
	//@Transactional
	void getList() {
		Pageable pageable = PageRequest.of(0, 100, Sort.by(Order.asc("qa.qno")));
		Page<ProductQA> list = productQARepository.getQAList(602L, pageable);
		
		for(ProductQA dto : list.getContent()) {
			System.out.println(dto);
		}
	}
	
	//@Test
	void insetQAReply() {
		for(int i = 1; i <= 1; i++) {
			ProductQA qa = productQARepository.getQAOne((long)i);
			if(qa == null) {
				continue;
			}
			qa = qa.toBuilder().replyCheck(true).build();
			ProQAReply qr = ProQAReply.builder()
					.qr_content("안녕하세요. 고객님 \n 항상 픽앤딜을 믿고 이용해주시는 고객님께 감사 인사드립니다."
							+ " \n\n 문의 답변 드립니다."
							+ " \n\n 고객님, 이루고자 하시는 모든 일 건승하실수있으면 하시고, "
							+ "항상 건강과 행운이 저한테 가득하길 빌겠습니다."
							+ " \n\n 감사합니다.")
					.productQA(qa)
					.seller(Seller.builder().sno(1L).build())
					.build();
			productQAReplyRepository.save(qr);
			productQARepository.save(qa);
		}
	}
	
	//@Test
	void getQAReply() {
		ProQAReplyDTO dto = productQAReplyRepository.getQAReply(29L);
		
		System.out.println(dto);
	}
	
	//@Test
	//@Transactional
	void get() {
		List<ProductQA> list = productQARepository.getQAListBymno(1L);
		for(ProductQA qa : list) {
			System.out.println(qa);
		}
	}

}
