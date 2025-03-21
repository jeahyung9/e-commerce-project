package com.fullstack.springboot.service.seller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.fullstack.springboot.dto.product.CategoryProductBumperDTO;
import com.fullstack.springboot.dto.product.OptionDetailDTO;
import com.fullstack.springboot.dto.product.ProductDTO;
import com.fullstack.springboot.dto.product.ProductImageDTO;
import com.fullstack.springboot.dto.seller.SellerDTO;
import com.fullstack.springboot.entity.product.CategoryProductBumper;
import com.fullstack.springboot.entity.product.OptionDetail;
import com.fullstack.springboot.entity.product.Product;
import com.fullstack.springboot.entity.product.ProductImage;
import com.fullstack.springboot.entity.seller.Seller;
import com.fullstack.springboot.repository.product.CategoryProductRepository;
import com.fullstack.springboot.repository.product.CategoryRepository;
import com.fullstack.springboot.repository.product.ProductRepository;
import com.fullstack.springboot.repository.seller.SellerRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@Transactional
@Log4j2
@RequiredArgsConstructor
public class SellerSeviceImpl implements SellerService {
	
	private final SellerRepository sellerRepository;
	
	private final ProductRepository productRepository;
	
	private final CategoryRepository categoryRepository;
	
	private final CategoryProductRepository categoryProductRepository;
	
	private final PasswordEncoder passwordEncoder;

	@Override
	public SellerDTO createSeller(SellerDTO sellerDTO) {
		
		String sId = sellerDTO.getSId();
		
		if(checkDuplicateSellerId(sId)) {
			throw new RuntimeException("이미 존재하는 판매자 ID");
		}
		
		if(sellerRepository.findBySellerWithSId(sId).getS_email() != null) {
			throw new RuntimeException("이미 존재하는 판매자 ID");
		}
		
		String encodedSellerPassword = passwordEncoder.encode(sellerDTO.getSPw());
		
		List<ProductDTO> proDTOList = sellerDTO.getProductList();
		
		List<Product> proList = new ArrayList<>(); 
		
		for(ProductDTO proDTO : proDTOList) {
			
			List<OptionDetailDTO> odDTOList = proDTO.getOptionDetail();
			
			List<OptionDetail> odList = new ArrayList<>();
			
			for(OptionDetailDTO odDTO : odDTOList) {
				
				Product product = Product.builder().pno(odDTO.getPno()).build();
				
				OptionDetail detail = OptionDetail.builder()
						.odno(odDTO.getOdno())
						.od_name(odDTO.getOd_name())
						.od_price(odDTO.getOd_price())
						.od_stock(odDTO.getOd_stock())
						.od_salesVol(odDTO.getOd_salesVol())
						.product(product)
						.build();
				
				odList.add(detail);
			}
			
			List<ProductImageDTO> imgDTOList = proDTO.getProductImage();
			
			List<ProductImage> imgList = new ArrayList<>();
			
			for(ProductImageDTO imgDTO : imgDTOList) {
				Product product = Product.builder().pno(imgDTO.getPno()).build();
				
				ProductImage proImg = ProductImage.builder()
						.pino(imgDTO.getPino())
						.pi_name(imgDTO.getPi_name())
						.pi_path(imgDTO.getPi_path())
						.pi_ord(imgDTO.getPi_ord())
						.product(product)
						.build();
				
				imgList.add(proImg);
			}
			
			List<CategoryProductBumperDTO> cpbDTOList = categoryProductRepository.getCpbListWithPno(proDTO.getPno()).stream()
					.map(cpb -> {
						CategoryProductBumperDTO cpbDTO = CategoryProductBumperDTO.builder()
								.cpno(cpb.getCpno())
								.ctno(cpb.getCategory().getCtno())
								.pno(proDTO.getPno())
								.build();
						return cpbDTO;
					}) // 카테고리 추가
					.collect(Collectors.toList());
			
			List<CategoryProductBumper> cpbList = new ArrayList<>();
			
			for(CategoryProductBumperDTO cpbDTO : cpbDTOList) {
				CategoryProductBumper cpb = CategoryProductBumper.builder()
						.cpno(cpbDTO.getCpno())
						.category(categoryRepository.findById(cpbDTO.getCtno()).get())
						.product(productRepository.findById(cpbDTO.getPno()).get())
						.build();
				cpbList.add(cpb);
			}
			
			
			Product p = Product.builder()
					.pno(proDTO.getPno())
					.p_name(proDTO.getP_name())
					.p_content(proDTO.getP_content())
					.p_price(proDTO.getP_price())
					.p_salePer(proDTO.getP_salePer())
					.p_stock(proDTO.getP_stock())
					.p_salesVol(proDTO.getP_salesVol())
					.optionDetails(odList)
					.productImage(imgList)
					.categoryProductBumpers(cpbList)
					.delFlag(proDTO.isDelFlag())
					.build();
			
			proList.add(p);
		}
		
		Seller seller = Seller.builder()
				.s_id(sId)
				.s_pw(encodedSellerPassword)
				.businessName(sellerDTO.getBusinessName())
				.businessNum(sellerDTO.getBusinessNum())
				.s_num(sellerDTO.getSNum())
				.s_email(sellerDTO.getSEmail())
				.s_addr(sellerDTO.getSAddr())
				.totalSales(sellerDTO.getTotalSales())
				.productList(proList)
				.build();
		
		sellerRepository.save(seller);
		
		return sellerDTO;
	}

	@Override
	public boolean checkDuplicateSellerId(String sId) {
		return sellerRepository.findBySellerWithSId(sId) != null;
	}

	@Override
	public SellerDTO loginSeller(String sId, String sPw) {
		Seller seller =  sellerRepository.findBySellerWithSId(sId);
		
		if(seller == null) {
			throw new RuntimeException("Seller Not found");
		}
		
		if(!passwordEncoder.matches(sPw, seller.getS_pw())) {
			throw new RuntimeException("Incorrect seller password");
		}
		
		return new SellerDTO(seller);
	}
	
}
