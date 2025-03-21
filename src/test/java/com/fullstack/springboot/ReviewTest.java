package com.fullstack.springboot;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.fullstack.springboot.dto.review.ReviewDTO;
import com.fullstack.springboot.entity.member.Member;
import com.fullstack.springboot.entity.product.OptionDetail;
import com.fullstack.springboot.entity.review.ProductReview;
import com.fullstack.springboot.repository.product.OptionDetailRepository;
import com.fullstack.springboot.repository.review.ReviewRepository;
import com.fullstack.springboot.service.review.ReviewService;

import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;

@SpringBootTest
@Log4j2
public class ReviewTest {
	
	@Autowired
	private ReviewRepository reviewRepository;
	@Autowired
	private ReviewService reviewService;
	@Autowired
	private OptionDetailRepository optionDetailRepository;
	
	//service test========================================================
	//@Test
	void getOne() {
		ReviewDTO dto = reviewService.getReviewOne(1L);
		System.out.println(dto);
	}
	
	//@Test
	void insertOne() {
		String str = "이거 \n 정말 \n 쩔어요. \n\n\n\n\n\n\n\n 이걸 믿어?";
		Long orno = null;
		ReviewDTO dto = ReviewDTO.builder()
				.title("오")
				.content(str)
				.rate((int)(Math.random() * 5) + 1)
				.reviewLike((long)(Math.random() * 100))
				.mno((long)(Math.random() * 100) + 1)
				.odno(3086L)
				.build();
		reviewService.register(orno, dto);
	}
	
	@Test
	void insert() {
		String[] reviewTitles = {
				"제품 사용 후 만족도 최고입니다!",
			    "정말 괜찮은 품질이에요. 강력 추천!",
			    "가격 대비 훌륭한 성능을 보여줘요.",
			    "배송도 빠르고 제품 상태도 완벽!",
			    "디자인이 마음에 들어요. 세련되었어요.",
			    "기대 이상으로 성능이 좋아서 만족합니다.",
			    "조금 비쌌지만 만족도가 높네요.",
			    "처음 사용해보는데 너무 좋아요.",
			    "가성비 최고! 또 구매할 의향 있어요.",
			    "사용법도 간단하고 직관적이에요.",
			    "오래 쓸 수 있을 것 같아요.",
			    "친구에게 추천하고 싶어요.",
			    "품질이 정말 좋아요. 다시 살 거예요.",
			    "이 가격에 이런 품질은 정말 대단해요.",
			    "배송도 빠르고 포장도 꼼꼼하게 왔어요.",
			    "기대 이상으로 사용감이 좋네요.",
			    "아이와 함께 사용 중인데 만족해요.",
			    "처음엔 조금 고민했지만 만족스러워요.",
			    "이 제품 덕분에 일상이 편해졌어요.",
			    "고급스러운 느낌이에요.",
			    "정말 편리하고 유용한 제품이에요.",
			    "사용하기 간편하고 효과도 좋아요.",
			    "디자인도 세련되고 성능도 좋네요.",
			    "가격대비 성능이 우수해요.",
			    "이 제품은 절대 후회하지 않을 거예요.",
			    "사용 후 편리함에 놀랐어요.",
			    "조용하고 성능도 뛰어나요.",
			    "재구매 의사 100%입니다.",
			    "품질이 좋아서 오래 사용할 것 같아요.",
			    "기대 이상으로 만족스럽네요.",
			    "다양한 기능이 마음에 들어요.",
			    "디자인이 심플하고 깔끔해요.",
			    "편리하게 사용할 수 있어요.",
			    "성능이 정말 뛰어나요.",
			    "품질에 비해 가격이 괜찮아요.",
			    "정말 잘 만든 제품 같아요.",
			    "효율적인 성능에 감동했어요.",
			    "디자인도 예쁘고 사용하기 좋아요.",
			    "이 제품 덕분에 생활이 편해졌어요.",
			    "간편하고 실용적인 제품이에요.",
			    "배송도 빨라서 기분 좋았어요.",
			    "재구매하고 싶은 제품이에요.",
			    "가격이 저렴해서 괜찮은 선택 같아요.",
			    "깔끔한 디자인과 고급스러움이 느껴져요.",
			    "효율적인 사용감에 만족해요.",
			    "편리하게 사용할 수 있어서 좋아요.",
			    "상상 이상으로 괜찮았어요.",
			    "기능이 많은 제품이라 유용해요.",
			    "설치가 간편하고 직관적이에요.",
			    "디자인도 예쁘고 성능도 좋아요."
	        };
		
		String[] reviewContents = {
				"제품의 품질이 기대 이상으로 좋았어요. 매우 만족합니다!",
			    "이 제품 정말 좋네요. 가격 대비 훌륭한 성능이에요. 가성비 최고입니다.",
			    "디자인이 깔끔하고 세련돼서 마음에 들어요. 방 분위기도 확 바뀌었어요.",
			    "배송이 정말 빠르며, 제품도 훌륭해요. 추천합니다.",
			    "가격이 비쌌지만 그만큼의 가치가 있는 것 같아요. 성능도 뛰어나고 내구성도 좋습니다.",
			    "처음 사용해봤는데 정말 만족스럽습니다. 사용법도 직관적이고, 성능도 기대 이상이에요.",
			    "이 제품은 가성비가 뛰어나요. 성능도 우수하고 가격도 적당해서 추천하고 싶어요.",
			    "제품이 깔끔하게 포장되어 왔고, 사용하기 너무 편리합니다. 특히 디자인이 예뻐서 더욱 마음에 들어요.",
			    "정말 괜찮은 품질이에요. 성능도 좋고 가격 대비 최고의 선택 같아요.",
			    "디자인이 세련되어서 집안 분위기에 잘 어울려요. 성능도 좋고 사용하기 편해요.",
			    "처음엔 조금 망설였지만, 생각보다 만족스러워요. 사용이 정말 간편하고 효과도 좋아요.",
			    "가격 대비 뛰어난 성능에 정말 놀랐어요. 앞으로도 자주 사용할 예정입니다.",
			    "이 제품을 사용하면서 느낀 점은 정말 만족스럽고 유용하다는 거예요. 모든 기능이 직관적이고 효율적입니다.",
			    "제품 상태가 정말 좋고, 사용하기도 쉽고 직관적이어서 매우 만족스러웠어요.",
			    "배송이 빠르고 포장도 안전하게 되어 왔어요. 제품도 깔끔하고 성능도 뛰어나서 만족스럽습니다.",
			    "이 제품 덕분에 일상이 훨씬 편리해졌어요. 사용이 간편하고 성능도 정말 뛰어나요.",
			    "조용하고 성능도 뛰어나요. 집에서 사용할 때 너무 편리해요.",
			    "디자인이 심플하고 깔끔해요. 기능도 우수하고, 사용하기도 정말 편리해요.",
			    "성능이 뛰어나고 가격도 적당해요. 가성비가 정말 좋은 제품입니다.",
			    "기대 이상으로 만족스럽네요. 기능도 다양하고, 성능도 정말 좋아요. 추천합니다!",
			    "가격에 비해 성능이 정말 좋네요. 다른 제품과 비교할 수 없을 만큼 효율적이에요.",
			    "디자인이 예쁘고 성능도 좋습니다. 주방에서 사용할 때 정말 유용해요.",
			    "제품이 매우 만족스러워요. 사용법도 간단하고 성능도 훌륭합니다.",
			    "디자인과 기능성 모두 만족해요. 가격 대비 성능이 정말 좋아요.",
			    "기능이 다양하고 성능도 좋아요. 사용하기 너무 편리해요.",
			    "간편하게 사용할 수 있어 너무 좋습니다. 디자인도 예쁘고 성능도 뛰어나요.",
			    "배송이 빨라서 기분 좋았어요. 제품도 정말 만족스럽고 기대 이상이었어요.",
			    "이 가격에 이만큼의 품질을 제공해준다면 정말 대박이에요. 추천할 만한 제품입니다.",
			    "제품이 깔끔하게 포장되어 왔고, 사용하기 정말 편리했어요. 가격 대비 만족도가 아주 높습니다.",
			    "정말 실용적이고 효율적인 제품이에요. 많은 기능이 있어도 직관적이라 사용하기 편합니다.",
			    "가격이 저렴하지만 성능은 뛰어나요. 너무 만족스럽습니다.",
			    "상상 이상으로 편리하고 사용하기 간편해요. 제품에 대한 만족도가 높아요.",
			    "디자인도 세련되고, 성능도 정말 뛰어나요. 다시 구매할 의향 있습니다.",
			    "제품 덕분에 일상이 훨씬 편해졌어요. 정말 좋은 선택이었어요.",
			    "가격 대비 성능이 훌륭합니다. 고급스럽고 세련된 디자인이 마음에 들어요.",
			    "이 가격에 이런 성능이라니 정말 놀랐어요. 사용하기 간편하고 만족스러워요.",
			    "이 제품 정말 추천해요. 사용해보니 성능이 뛰어나고 편리합니다.",
			    "디자인과 성능 모두 만족스러워요. 사용하기 간편하고 실용적이에요.",
			    "기능이 많아서 유용하게 사용할 수 있어요. 가격도 적당하고 만족스러워요.",
			    "간단한 설치와 직관적인 사용법이 마음에 들어요. 성능도 훌륭합니다.",
			    "정말 효율적인 제품이에요. 사용하기 편리하고 성능도 뛰어나서 만족하고 있습니다.",
			    "재구매할 의사가 있어요. 성능, 디자인 모두 훌륭해요.",
			    "디자인이 예쁘고 성능도 훌륭해서 매우 만족합니다. 강력 추천해요.",
			    "이 제품 사용 후 생활이 더 편리해졌어요. 정말 좋은 선택이었어요.",
			    "가격 대비 이 정도 성능이라면 충분히 만족합니다. 가성비가 좋아요.",
			    "제품이 너무 좋아서 바로 두 번째 구매를 했어요. 정말 만족합니다.",
			    "기능이 많고 성능이 뛰어나요. 제품 사용 후 매우 만족스러웠습니다.",
			    "설치가 간편하고 사용도 쉬워서 매우 만족스러워요. 가격 대비 최고의 선택 같아요.",
			    "성능도 우수하고 디자인도 예뻐서 사용하기 너무 편리해요. 꼭 추천합니다.",
			    "배송도 빠르고, 제품 상태도 매우 좋았어요. 마음에 듭니다."
			    };
		
		System.out.println(reviewTitles.length + " : " + reviewContents.length);
		
		for(int i = 0; i < 10000; i++) {
			int index = (int)(Math.random() * 50);
			Long odno = (long)(Math.random() * 1085) + 2000;
			Long orno = 1L;
			
//			Optional<OptionDetail> od =  optionDetailRepository.findById(odno);
//			
//			if(od.isPresent()) {
//				System.out.println("이거 안돼? : " + odno);
//				continue;
//			}
			
//			ReviewDTO dto = ReviewDTO.builder()
//					.title(reviewTitles[index])
//					.content(reviewContents[index])
//					.rate((int)(Math.random() * 5) + 1)
//					.reviewLike((long)(Math.random() * 100))
//					.mno((long)(Math.random() * 100) + 1)
//					.odno(odno)
//					.build();
//			
			//Member member = Member.builder().mno(dto.getMno()).build();
			
			//OptionDetail optionDetail = OptionDetail.builder().odno(dto.getOdno()).build();
			
			ProductReview review = ProductReview.builder()
					.title(reviewTitles[index])
					.content(reviewContents[index])
					.rate((int)(Math.random() * 5) + 1)
					.reviewLike((long)(Math.random() * 100))
					.member(Member.builder().mno((long)(Math.random() * 100) + 1).build())
					.optionDetail(OptionDetail.builder().odno(odno).build())
					.build();
			
			ProductReview rev = reviewRepository.save(review);
			
			//reviewService.register(orno, dto);
		}
	}
	
	//@Test
	void change() {
		ReviewDTO dto2 = reviewService.getReviewOne(1L);
		
		ReviewDTO dto = ReviewDTO.builder()
				.rno(dto2.getRno())
				.title(dto2.getTitle())
				.content(dto2.getContent())
				.rate(dto2.getRate())
				.reviewLike(dto2.getReviewLike() + 1)
				.mno(dto2.getMno())
				.odno(dto2.getOdno())
				.build();
		
		reviewService.changeReview(dto);
	}
	
	//@Test
	void delete() {
		reviewService.deleteReview(7L);
	}
	
	
	//repository test----------------------------------------------------------
	
	//@Test
	void getCnt() {
		System.out.println(reviewRepository.getReviewCnt(19L));
	}
	
	//@Test
	void getReview() {
		//PageRequestDTO pageable = PageRequest.of(0, 100, Sort.by(Order.asc("od.od_name")));
		//PageResponseDTO<ReviewDTO> list = reviewRepository.getReview(1L, pageable);
		//for(ReviewDTO dto : list.getDtoList()) {
			//System.out.println(dto);
		//}
	}
	
	//@Test
	void insertReview() {
		ProductReview review = ProductReview.builder()
				.title("타이틀")
				.content("내용")
				.rate(2)
				.reviewLike(2L)
				.member(Member.builder().mno(10L).build())
				.optionDetail(OptionDetail.builder().odno(1L).build())
				.build();
		reviewRepository.save(review);
	}

}
