package com.fullstack.springboot.service.product;

import java.util.List;
import java.util.Optional;

import com.fullstack.springboot.dto.PageRequestDTO;
import com.fullstack.springboot.dto.PageResponseDTO;
import com.fullstack.springboot.dto.product.OptionDetailDTO;
import com.fullstack.springboot.dto.product.ProductDTO;
import com.fullstack.springboot.entity.product.OptionDetail;
import com.fullstack.springboot.entity.product.Product;
import com.fullstack.springboot.entity.product.Seller;

public interface ProductService {
Long registerProduct(ProductDTO productDTO);
	
	ProductDTO readProduct(Long pno);
	
	void deleteProduct(Long pno);
	
	void modifyProduct(ProductDTO productDTO);
	
	PageResponseDTO<ProductDTO> productList(PageRequestDTO pageRequestDTO); //페이징

    OptionDetailDTO getOptionDetail(Long opno, Long pno);
    
    void updateProductStock(Long pno);
    
    List<OptionDetailDTO> getStockOptionDetails(Long pno);
    
    void decreaseOptionStock(Long odno);
    
    List<OptionDetailDTO> getOptionsByProductId(Long pno);
	
	default Product dtoToEntity(ProductDTO dto) {
		Seller seller = Seller.builder().sno(dto.getSno()).build();
	
		Product product = Product.builder()
				.pno(dto.getPno())
				.p_name(dto.getP_name())
				.p_content(dto.getP_content())
				.p_price(dto.getP_price())
				.p_salePer(dto.getP_salePer())
				.p_stock(dto.getP_stock())
				.p_salesVol(dto.getP_salesVol())
				.seller(seller)
				.delFlag(dto.isDelFlag())
				.build();
		
		return product;
	}
	
	default ProductDTO entityToDTO(Product product) {
		Seller seller = Seller.builder().sno(product.getSeller().getSno()).build();
		
		ProductDTO productDTO = ProductDTO.builder()
				.pno(product.getPno())
				.p_name(product.getP_name())
				.p_content(product.getP_content())
				.p_price(product.getP_price())
				.p_salePer(product.getP_salePer())
				.p_stock(product.getP_stock())
				.p_salesVol(product.getP_salesVol())
				.sno(seller.getSno())
				.delFlag(product.isDelFlag())
				.build();
		
		return productDTO;
	}
	
	default OptionDetail convertDetailDTOToEntity(OptionDetailDTO dto) {// optionDetail 의 dtoToEntity
		Product product = Product.builder().pno(dto.getPno()).build();
		
		OptionDetail optionDetail = OptionDetail.builder()
				.odno(dto.getOdno())
				.od_name(dto.getOd_name())
				.od_price(dto.getOd_price())
				.od_stock(dto.getOd_stock())
				.product(product)
				.build();
		
		return optionDetail;
	}
	
	default OptionDetailDTO convertDetailToDTO(OptionDetail optDetail) {// optionDetail 의 entityToDTO
		Product product = Product.builder().pno(optDetail.getProduct().getPno()).build();
		
		OptionDetailDTO optDetailDTO = OptionDetailDTO.builder()
				.odno(optDetail.getOdno())
				.od_name(optDetail.getOd_name())
				.od_price(optDetail.getOd_price())
				.od_stock(optDetail.getOd_stock())
				.pno(product.getPno())
				.build();
		
		return optDetailDTO;
	}
}

