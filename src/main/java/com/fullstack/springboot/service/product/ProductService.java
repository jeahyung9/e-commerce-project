package com.fullstack.springboot.service.product;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.web.multipart.MultipartFile;

import com.fullstack.springboot.dto.PageRequestDTO;
import com.fullstack.springboot.dto.PageResponseDTO;
import com.fullstack.springboot.dto.product.OptionDetailDTO;
import com.fullstack.springboot.dto.product.ProductDTO;
import com.fullstack.springboot.dto.product.ProductImageDTO;
import com.fullstack.springboot.entity.product.OptionDetail;
import com.fullstack.springboot.entity.product.Product;
import com.fullstack.springboot.entity.product.ProductImage;
import com.fullstack.springboot.entity.seller.Seller;


public interface ProductService {
	
	Long registerProduct(ProductDTO productDTO, List<Long> ctno, List<MultipartFile> images, List<OptionDetailDTO> opts) throws IOException;
	
	ProductDTO readProduct(Long pno);
	
	void updateToDelFlag(Long pno, boolean status);
	
	void modifyProduct(ProductDTO productDTO, List<MultipartFile> newImages, List<Integer> delImgOrders) throws IOException;
	
	PageResponseDTO<ProductDTO> productList(PageRequestDTO pageRequestDTO, String sort, Long ctno, boolean order, String keyword); //페이징

	PageResponseDTO<ProductDTO> superProductList(PageRequestDTO pageRequestDTO, Long ctno, Boolean delFlag, String keyword); //관리자, 판매자용 상품 페이징 

    OptionDetailDTO getOptionDetail(Long pno);
    
    void updateProductStock(Long pno);
    
    void updateProductSalesVol(Long pno);
    
    List<OptionDetailDTO> getStockOptionDetails(Long pno);
    
    void decreaseOptionStock(Long odno, int cnt);

	void increaseOptionStock(Long odno, int cnt);
    
    List<OptionDetailDTO> getOptionsByProductId(Long pno);
    
    void addAnotherProductImage(Long pno, String imagePath, int imageOrder);
    
    void deleteImage(Long pno, int ord);
    
    void deleteAllImage(Long pno);
    
    void changeImageOrder(Long pno, int oldOrder, int newOrder);
	
	default Product dtoToEntity(ProductDTO dto, ModelMapper modelMapper) {
		Seller seller = Seller.builder().sno(dto.getSno()).build();
		
		List<OptionDetailDTO> odDTOList = dto.getOptionDetail();
		
		List<OptionDetail> odList = new ArrayList<>();
		
		for(OptionDetailDTO odDTO : odDTOList) {
			OptionDetail detail = convertDetailDTOToEntity(odDTO);
			odList.add(detail);
		}		
		
		List<ProductImageDTO> imgDTOList = dto.getProductImage();
		
		List<ProductImage> imgList = new ArrayList<>();
		
		for(ProductImageDTO imgDTO : imgDTOList) {
			ProductImage proImg = convertImageDTOToEntity(imgDTO);
			imgList.add(proImg);
		}
	
		Product product = modelMapper.map(dto, Product.class);

		return product.toBuilder().seller(seller).optionDetails(odList).productImage(imgList).build();
	}
	
	default ProductDTO entityToDTO(Product product, ModelMapper modelMapper) {
		Seller seller = Seller.builder().sno(product.getSeller().getSno()).build();
		List<OptionDetailDTO> odDTOList = new ArrayList<>();
		List<OptionDetail> odList = product.getOptionDetails();
		
		for(OptionDetail od : odList) {
			OptionDetailDTO detailDTO = convertDetailToDTO(od);
			odDTOList.add(detailDTO);
		}
		
		List<ProductImageDTO> imgDTOList = new ArrayList<>();
		
		List<ProductImage> imgList = product.getProductImage();
		
		for(ProductImage img : imgList) {
			ProductImageDTO ImgDTO = convertImageToDTO(img);
			imgDTOList.add(ImgDTO);
		}

		ProductDTO productDTO = modelMapper.map(product, ProductDTO.class);

		return productDTO.toBuilder().sno(seller.getSno()).businessName(seller.getBusinessName()).optionDetail(odDTOList).s_email(seller.getS_email()).productImage(imgDTOList).build();
	}
	
	default OptionDetail convertDetailDTOToEntity(OptionDetailDTO dto) {// optionDetail 의 dtoToEntity
		Product product = Product.builder().pno(dto.getPno()).build();
		
		OptionDetail optionDetail = OptionDetail.builder()
				.odno(dto.getOdno())
				.od_name(dto.getOd_name())
				.od_price(dto.getOd_price())
				.od_stock(dto.getOd_stock())
				.od_salesVol(dto.getOd_salesVol())
				.product(product)
				.build();
		
		return optionDetail;
	}
	
	default OptionDetailDTO convertDetailToDTO(OptionDetail optDetail) {// optionDetail 의 entityToDTO
		Long productNum =optDetail.getProduct().getPno();
		
		OptionDetailDTO optDetailDTO = OptionDetailDTO.builder()
				.odno(optDetail.getOdno())
				.od_name(optDetail.getOd_name())
				.od_price(optDetail.getOd_price())
				.od_stock(optDetail.getOd_stock())
				.od_salesVol(optDetail.getOd_salesVol())
				.pno(productNum)
				.build();
		
		return optDetailDTO;
	}
	
	default ProductImage convertImageDTOToEntity(ProductImageDTO dto) {
		Product product = Product.builder().pno(dto.getPno()).build();
		
		ProductImage productImg = ProductImage.builder()
				.pino(dto.getPino())
				.pi_name(dto.getPi_name())
				.pi_path(dto.getPi_path())
				.pi_ord(dto.getPi_ord())
				.product(product)
				.build();
		
		return productImg;
	}
	
	default ProductImageDTO convertImageToDTO(ProductImage proImage) {
		Long productNum =proImage.getProduct().getPno();
		
		ProductImageDTO dto = ProductImageDTO.builder()
				.pino(proImage.getPino())
				.pi_name(proImage.getPi_name())
				.pi_path(proImage.getPi_path())
				.pi_ord(proImage.getPi_ord())
				.pno(productNum)
				.build();
		
		return dto;
	}
}

