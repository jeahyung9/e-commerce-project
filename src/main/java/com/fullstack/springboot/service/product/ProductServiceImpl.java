package com.fullstack.springboot.service.product;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fullstack.springboot.dto.PageRequestDTO;
import com.fullstack.springboot.dto.PageResponseDTO;
import com.fullstack.springboot.dto.product.CategoryProductBumperDTO;
import com.fullstack.springboot.dto.product.OptionDetailDTO;
import com.fullstack.springboot.dto.product.ProductDTO;
import com.fullstack.springboot.dto.product.ProductImageDTO;
import com.fullstack.springboot.entity.product.Category;
import com.fullstack.springboot.entity.product.CategoryProductBumper;
import com.fullstack.springboot.entity.product.OptionDetail;
import com.fullstack.springboot.entity.product.Product;
import com.fullstack.springboot.entity.product.ProductImage;
import com.fullstack.springboot.entity.seller.Seller;
import com.fullstack.springboot.repository.product.CategoryProductRepository;
import com.fullstack.springboot.repository.product.CategoryRepository;
import com.fullstack.springboot.repository.product.OptionDetailRepository;
import com.fullstack.springboot.repository.product.ProductImageRepository;
import com.fullstack.springboot.repository.product.ProductRepository;
import com.fullstack.springboot.repository.seller.SellerRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
	
	private final ModelMapper modelMapper;
	
	private final ProductRepository productRepository;
	
	private final OptionDetailRepository optionDetailRepository;
	
	private final SellerRepository sellerRepository;
	
	private final CategoryRepository categoryRepository;
	
	private final CategoryProductRepository categoryProductRepository;
	
	private final ProductImageRepository productImageRepository;
	
	@Value("${com.fullstack.springboot.uploadPath}")
	private String uploadPath;
	
	@Override
	@Transactional
	public Long registerProduct(ProductDTO productDTO, List<Long> ctnoList, List<MultipartFile> images, List<OptionDetailDTO> opts) throws IOException {
		
		Product product = dtoToEntity(productDTO, modelMapper);
		
		if(!sellerRepository.existsById(product.getSeller().getSno())) {
			throw new RuntimeException("판매자 존재 하지 않음");
		}
		
		Product result = productRepository.save(product);
		
		for (int i = 0; i < images.size(); i++) {
            MultipartFile image = images.get(i);
            
            // 파일 이름 설정 및 경로 생성
            String fileName = image.getOriginalFilename();
            Path filePath = Paths.get(uploadPath, fileName);
            
            // 파일 저장
            Files.createDirectories(filePath.getParent());
            image.transferTo(filePath);
            
            // ProductImage 엔티티 생성
            ProductImage productImage = ProductImage.builder()
                    .product(result)
                    .pi_path(filePath.toString()) // 파일 경로 저장
                    .pi_ord(i) // 이미지 순서
                    .build();
            
            // 이미지 저장
            productImageRepository.save(productImage);
        }
		
		for(OptionDetailDTO optDTO : opts ) {
			OptionDetail opt = convertDetailDTOToEntity(optDTO);
			
			optionDetailRepository.save(opt);
		}
		
		for(Long ctno : ctnoList) {
			Category category = categoryRepository.findById(ctno).get();
			
			CategoryProductBumper cpb = CategoryProductBumper.builder()
					.category(category)
					.product(product)
					.build();
			
			categoryProductRepository.save(cpb);
		}	
		
		return result.getPno();
	}

	@Override
	public ProductDTO readProduct(Long pno) {
		Optional<Product> result = productRepository.findById(pno);
	      
	      Product product = result.orElseThrow();
	      
	      ProductDTO productDTO = entityToDTO(product, modelMapper);
	      
	      Seller seller = Seller.builder().sno(product.getSeller().getSno()).businessName(product.getSeller().getBusinessName()).s_email(product.getSeller().getS_email()).build();// Seller 추가 
	      
	      List<OptionDetailDTO> odDTOList = optionDetailRepository.getOptByPno(product.getPno()).stream()
	            .map(optDetail -> convertDetailToDTO(optDetail))
	            .collect(Collectors.toList());  //OptionDetail 추가 
	      
	      List<CategoryProductBumperDTO> cpbDTOList = categoryProductRepository.getCpbListWithPno(product.getPno()).stream()
	            .map(cpb -> {
	               CategoryProductBumperDTO cpbDTO = CategoryProductBumperDTO.builder()
	                     .cpno(cpb.getCpno())
	                     .ctno(cpb.getCategory().getCtno())
	                     .pno(product.getPno())
	                     .build();
	               return cpbDTO;
	            })
	            .collect(Collectors.toList());
	      
	      List<ProductImageDTO> imgDTOList = productImageRepository.getProductImgByPno(product.getPno()).stream()
	    		  .map(img -> convertImageToDTO(img)).collect(Collectors.toList());
	      
	      ProductDTO dto = productDTO.toBuilder().sno(seller.getSno()).businessName(seller.getBusinessName()).s_email(seller.getS_email()).optionDetail(odDTOList).categoryProductBumpers(cpbDTOList).productImage(imgDTOList).build();
	      
	      return dto;
	}

	@Override
	public void updateToDelFlag(Long pno, boolean status) {
		
		productRepository.findById(pno).orElseThrow(()-> new RuntimeException("해당 상품을 찾을 수 없음"));
		
		productRepository.updateToDelFlag(pno, status);	
	}

	@Override
	public void modifyProduct(ProductDTO productDTO, List<MultipartFile> newImages, List<Integer> delImgOrders) throws IOException {
		
		Product product = productRepository.findById(productDTO.getPno())
				.orElseThrow(()-> new RuntimeException("해당 번호의 상품 찾을수 없음"));
		
		product = dtoToEntity(productDTO, modelMapper);
		productRepository.save(product);
		
		if (delImgOrders != null && !delImgOrders.isEmpty()) { //삭제 이미지 순서 리스트가 비어있지 않다면, 특정 이미지 삭제
	        for (Integer ord : delImgOrders) {
	            productImageRepository.delProImg(productDTO.getPno(), ord);
	        }
	    }
		
		if(newImages != null && !newImages.isEmpty()) { // 새 이미지 리스트가 비어있지 않다면, 이미지 추가
			int existingImageCount = productImageRepository.getProductImgCountByPno(productDTO.getPno());
			
			for (int i = 0; i < newImages.size(); i++) {
	            MultipartFile image = newImages.get(i);
	            
	            // 파일 이름 설정 및 경로 생성
	            String fileName = image.getOriginalFilename();
	            Path filePath = Paths.get(uploadPath, fileName);
	            
	            // 파일 저장
	            Files.createDirectories(filePath.getParent());
	            image.transferTo(filePath);
	            
	            // ProductImage 엔티티 생성
	            ProductImage productImage = ProductImage.builder()
	                    .product(product)
	                    .pi_path(filePath.toString()) // 파일 경로 저장
	                    .pi_ord(existingImageCount + i) // 순서를 기존 이미지 개수 뒤로 추가
	                    .build();
	            
	            // 이미지 저장
	            productImageRepository.save(productImage);
	        }
		}
		
	}

	@Override
	public PageResponseDTO<ProductDTO> productList(PageRequestDTO pageRequestDTO, String sort, Long ctno, boolean order, String keyword) {

      //기본 : false => desc, true => asc
      Order sortOrder = null;
      
      if (order) {
         sortOrder = Order.asc(sort);
      } else {
         sortOrder = Order.desc(sort);
      }
      
      if(pageRequestDTO.getSortBy().equals("p_price")) {
    	  sortOrder = Order.asc(sort);
      }
      
      Pageable pageable = 
            PageRequest.of(
                  pageRequestDTO.getPage() -1, 
                  pageRequestDTO.getSize(),
                  sort.length() != 0 ? Sort.by(sortOrder) : Sort.by("pno").descending());
      log.error("serviceImpl : " + keyword);
      log.error("Request Sort : " + sort);
      
      Page<Product> result = null;
      
      if(keyword !=  null) { // 검색어가 있다면
         log.error("serviceImpl / keyword is not null ");
         if(ctno != 0L) {
            
            List<Long> depthThirdList = categoryRepository.getCtnoWithDepth(3);
            
            if(!depthThirdList.contains(ctno)) {
               List<Long> depthSecondList = categoryRepository.getCtnoWithDepth(2);
               List<Long> depthFirstList = categoryRepository.getCtnoWithDepth(1);
               if(depthSecondList.contains(ctno)) { //2
                  result = categoryProductRepository.getProductsInDepth2Category(ctno, keyword, pageable);
                  log.error("not null : " + result + " keyword : " + keyword);
               }else if(depthFirstList.contains(ctno)){ // 1
                  result = categoryProductRepository.getProductsInDepth1Category(ctno, keyword, pageable);
                  log.error("not null : " + result + " keyword : " + keyword);
               }
            }else {
               result = categoryProductRepository.getProductInfoWithCate(ctno, keyword, pageable);
               log.error("not null : " + result + " keyword : " + keyword);
            }
         }else {
            result = productRepository.findByName(keyword, pageable);
            log.error("not null : " + result + " keyword : " + keyword);
         }
         
         
      }else { //검색어가 없다면
         log.error("serviceImpl / keyword is null ");
         if(ctno != 0L) {
            List<Long> depthThirdList = categoryRepository.getCtnoWithDepth(3);
            
            if(!depthThirdList.contains(ctno)) {
               List<Long> depthSecondList = categoryRepository.getCtnoWithDepth(2);
               List<Long> depthFirstList = categoryRepository.getCtnoWithDepth(1);
               if(depthSecondList.contains(ctno)) { //2
                  result = categoryProductRepository.getProductsInDepth2Category(ctno, pageable);
               }else if(depthFirstList.contains(ctno)){ // 1
                  result = categoryProductRepository.getProductsInDepth1Category(ctno, pageable);
               }
            }else {
               result = categoryProductRepository.getProductInfoWithCate(ctno, pageable);
            }
         }else {
            result = productRepository.getDelFlagFalse(pageable);
         }
      }
      
      
      List<ProductDTO> dtoList = result.getContent().stream()
            .map(product -> {
               ProductDTO productDTO = entityToDTO(product, modelMapper);
               
               Seller seller = Seller.builder().sno(product.getSeller().getSno()).build(); // Seller 추가 
               
               List<OptionDetailDTO> odDTOList = optionDetailRepository.getOptByPno(product.getPno()).stream()
                     .map(optDetail -> convertDetailToDTO(optDetail))
                     .collect(Collectors.toList());  //OptionDetail 추가 

               List<CategoryProductBumperDTO> cpbDTOList = categoryProductRepository.getCpbListWithPno(product.getPno()).stream()
                     .map(cpb -> {
                        CategoryProductBumperDTO cpbDTO = CategoryProductBumperDTO.builder()
                              .cpno(cpb.getCpno())
                              .ctno(cpb.getCategory().getCtno())
                              .pno(product.getPno())
                              .build();
                        return cpbDTO;
                     }) // 카테고리 추가
                     .collect(Collectors.toList());
               List<ProductImageDTO> imgDTOList = productImageRepository.getProductImgByPno(product.getPno()).stream()
                        .map(img -> convertImageToDTO(img)).collect(Collectors.toList());

               ProductDTO dto = productDTO.toBuilder().sno(seller.getSno()).optionDetail(odDTOList).categoryProductBumpers(cpbDTOList).productImage(imgDTOList).build();
               
               return dto;
            })
            .collect(Collectors.toList());

      long totalCount = result.getTotalElements();
      
      //log.error("resDTO");
      
      PageResponseDTO<ProductDTO> responseDTO = PageResponseDTO.<ProductDTO>withAll()
            .dtoList(dtoList)
            .pageRequestDTO(pageRequestDTO)
            .totalCount(totalCount)
            .build();
      
      //log.error("res : " + responseDTO);
      
      return responseDTO;
	}

	@Override
	public PageResponseDTO<ProductDTO> superProductList(PageRequestDTO pageRequestDTO, Long ctno, Boolean delFlag, String keyword){

		Pageable pageable = 
				PageRequest.of(
						pageRequestDTO.getPage() -1, 
						pageRequestDTO.getSize(),
						Sort.by("pno").descending());
		// log.error("serviceImpl : " + keyword);
		// log.error("delFlag : " + delFlag);
		
		Page<Product> result = null;
		
		if(delFlag == null) {
			if(keyword !=  null) { // 검색어가 있다면
				//log.error("serviceImpl / keyword is not null ");
				if(ctno != 0L) {
					
					List<Long> depthThirdList = categoryRepository.getCtnoWithDepth(3);
					
					if(!depthThirdList.contains(ctno)) {
						List<Long> depthSecondList = categoryRepository.getCtnoWithDepth(2);
						List<Long> depthFirstList = categoryRepository.getCtnoWithDepth(1);
						if(depthSecondList.contains(ctno)) { //2
							result = categoryProductRepository.superProductsInDepth2Category(ctno, keyword, pageable);
							log.error("not null : " + result + " keyword : " + keyword);
						}else if(depthFirstList.contains(ctno)){ // 1
							result = categoryProductRepository.superProductsInDepth1Category(ctno, keyword, pageable);
							log.error("not null : " + result + " keyword : " + keyword);
						}
					}else {
						result = categoryProductRepository.superProductInfoWithCate(ctno, keyword, pageable);
						log.error("not null : " + result + " keyword : " + keyword);
					}
				}else {
					result = productRepository.superFindByName(keyword, pageable);
					log.error("not null : " + result + " keyword : " + keyword);
				}
				
				
			}else { //검색어가 없다면
				log.error("serviceImpl / keyword is null ");
				if(ctno != 0L) {
					List<Long> depthThirdList = categoryRepository.getCtnoWithDepth(3);
					
					if(!depthThirdList.contains(ctno)) {
						List<Long> depthSecondList = categoryRepository.getCtnoWithDepth(2);
						List<Long> depthFirstList = categoryRepository.getCtnoWithDepth(1);
						if(depthSecondList.contains(ctno)) { //2
							result = categoryProductRepository.superProductsInDepth2Category(ctno, pageable);
						}else if(depthFirstList.contains(ctno)){ // 1
							result = categoryProductRepository.superProductsInDepth1Category(ctno, pageable);
						}
					}else {
						result = categoryProductRepository.superProductInfoWithCate(ctno, pageable);
					}
				}else {
					result = productRepository.findAll(pageable);
				}
			}
		}else if(!delFlag) {
			result = productRepository.getDelFlagFalse(pageable);
		}else if(delFlag){
			result = productRepository.getDelFlagTrue(pageable);
		}else {
			throw new RuntimeException("슈퍼리스트 오류 발생");
		}	
		
		List<ProductDTO> dtoList = result.getContent().stream()
				.map(product -> {
					ProductDTO productDTO = entityToDTO(product, modelMapper);
					
					Seller seller = Seller.builder().sno(product.getSeller().getSno()).build(); // Seller 추가 
					
					List<OptionDetailDTO> odDTOList = optionDetailRepository.getOptByPno(product.getPno()).stream()
							.map(optDetail -> convertDetailToDTO(optDetail))
							.collect(Collectors.toList());  //OptionDetail 추가 

					List<CategoryProductBumperDTO> cpbDTOList = categoryProductRepository.getCpbListWithPno(product.getPno()).stream()
							.map(cpb -> {
								CategoryProductBumperDTO cpbDTO = CategoryProductBumperDTO.builder()
										.cpno(cpb.getCpno())
										.ctno(cpb.getCategory().getCtno())
										.pno(product.getPno())
										.build();
								return cpbDTO;
							}) // 카테고리 추가
							.collect(Collectors.toList());
					List<ProductImageDTO> imgDTOList = productImageRepository.getProductImgByPno(product.getPno()).stream()
				    		  .map(img -> convertImageToDTO(img)).collect(Collectors.toList());

					ProductDTO dto = productDTO.toBuilder().sno(seller.getSno()).optionDetail(odDTOList).categoryProductBumpers(cpbDTOList).productImage(imgDTOList).build();
					
					return dto;
				})
				.collect(Collectors.toList());

		long totalCount = result.getTotalElements();
		
		//log.error("resDTO");
		
		PageResponseDTO<ProductDTO> responseDTO = PageResponseDTO.<ProductDTO>withAll()
				.dtoList(dtoList)
				.pageRequestDTO(pageRequestDTO)
				.totalCount(totalCount)
				.build();
		
		//log.error("res : " + responseDTO);
		
		return responseDTO;
	}

	@Override
	public OptionDetailDTO getOptionDetail(Long pno) {
		
		Optional<OptionDetail> result = optionDetailRepository.getOptDetailWithOptAndPro(pno);
		
		OptionDetail optionDetail = result.orElseThrow();
		
		OptionDetailDTO dto = convertDetailToDTO(optionDetail);
		
		//log.error("optional-optionaDetail : " + dto);
		
		return dto;
	}

	@Override
	public void updateProductStock(Long pno) {
		productRepository.updateProductStockByOptionDetail(pno);
		
		log.error("stockUpdate" + productRepository.updateProductStockByOptionDetail(pno));
	}
	
	@Override
	public void updateProductSalesVol(Long pno) {
		productRepository.updateProductSalesVolByOptionDetail(pno);
		
		//log.error("salesVolUpdate" + productRepository.updateProductSalesVolByOptionDetail(pno));
	}

	@Override
	public List<OptionDetailDTO> getStockOptionDetails(Long pno) {
		
		List<OptionDetail> optionDetailList = optionDetailRepository.getProductStockByPno(pno);
		//log.error("OptionDetail List : " + optionDetailList);
		return optionDetailList.stream()
				.map(this::convertDetailToDTO)
				.collect(Collectors.toList());
	}

	@Override
	public void decreaseOptionStock(Long odno, int cnt) {
		int updateRows = optionDetailRepository.decreaseOptionStock(odno, cnt);
		if(updateRows > 0) {
			log.error("Stock decreased, odno : " + odno);
		}
		
	}

	@Override
	public void increaseOptionStock(Long odno, int cnt) {
		int updateRows = optionDetailRepository.increaseOptionStock(odno, cnt);
		if(updateRows > 0) {
			log.error("Stock increased, odno : " + odno);
		}
		
	}

	@Override
	public List<OptionDetailDTO> getOptionsByProductId(Long pno) {
		List<OptionDetail> options = optionDetailRepository.getProductByPno(pno);
		return options.stream()
				.map(this::convertDetailToDTO)
				.collect(Collectors.toList());
	}
	


	@Override
	public void addAnotherProductImage(Long pno, String imagePath, int imageOrder) { // 상품 이미지 리스트에 다른 이미지 추가
		productRepository.findById(pno).orElseThrow(()-> new RuntimeException("해당 상품을 찾을 수 없음"));
	}

	@Override
	public void deleteImage(Long pno, int ord) {
		productRepository.findById(pno).orElseThrow(()-> new RuntimeException("해당 상품을 찾을 수 없음"));
		
		productImageRepository.delProImg(pno, ord);
		
		productImageRepository.increaseOrdWithNewOrd(pno, ord);
		
	}
	
	@Override
	public void deleteAllImage(Long pno) {
		
		productImageRepository.delAllProImg(pno);
	}

	@Override
	public void changeImageOrder(Long pno, int oldOrder, int newOrder) {
		productRepository.findById(pno).orElseThrow(()-> new RuntimeException("해당 상품을 찾을 수 없음"));
		
		if(oldOrder > -1 && newOrder > -1) {
			if(oldOrder > newOrder) {
				productImageRepository.increaseProImgOrd(pno, oldOrder, newOrder);
			}else if(oldOrder < newOrder) {
				productImageRepository.decreaseProImgOrd(pno, oldOrder, newOrder);
			}else if(oldOrder == newOrder) {
				throw new RuntimeException("기존 순서와 다른 순서를 입력하시오");
			}
		}else {
			throw new RuntimeException("0 이상의 수를 입력해주세요");
		}
		
	}
	
}
