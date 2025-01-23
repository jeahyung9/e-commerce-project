package com.fullstack.springboot.service.product;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.fullstack.springboot.dto.PageRequestDTO;
import com.fullstack.springboot.dto.PageResponseDTO;
import com.fullstack.springboot.dto.product.OptionDetailDTO;
import com.fullstack.springboot.dto.product.ProductDTO;
import com.fullstack.springboot.entity.product.OptionDetail;
import com.fullstack.springboot.entity.product.Product;
import com.fullstack.springboot.repository.product.OptionDetailRepository;
import com.fullstack.springboot.repository.product.ProductRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
	
	private final ModelMapper modelMapper;
	
	private final ProductRepository productRepository;
	
	private final OptionDetailRepository optionDetailRepository;
	
	@Override
	public Long registerProduct(ProductDTO productDTO) {
		Product product = dtoToEntity(productDTO);
		Product result = productRepository.save(product);
		return result.getPno();
	}

	@Override
	public ProductDTO readProduct(Long pno) {
		Optional<Product> result = productRepository.getProductInfo(pno);
		
		Product product = result.orElseThrow();
		
		ProductDTO productDTO = entityToDTO(product);
		
		return productDTO;
	}

	@Override
	public void deleteProduct(Long pno) {
		productRepository.updateToDelete(pno, true);
	}

	@Override
	public void modifyProduct(ProductDTO productDTO) {
		Optional<Product> result = productRepository.findById(productDTO.getPno());
		
		Product product = result.orElseThrow();
		
		product.changeName(productDTO.getP_name());
		product.changeContent(productDTO.getP_content());
		product.changePrice(productDTO.getP_price());
		// 옵션도 바꿀 수 있게 추가해야 함
		// 이미지도 구현 필요
		
		productRepository.save(product);
	}

	@Override
	public PageResponseDTO<ProductDTO> productList(PageRequestDTO pageRequestDTO) {
		Pageable pageable = 
				PageRequest.of(
						pageRequestDTO.getPage() -1, 
						pageRequestDTO.getSize(), 
						Sort.by("pno").descending());
		
		Page<Product> result = productRepository.findAll(pageable);
		
		List<ProductDTO> dtoList = result.getContent().stream()
				.map(product -> modelMapper.map(product, ProductDTO.class))
				.collect(Collectors.toList());
		
		long totalCount = result.getTotalElements();
		
		PageResponseDTO<ProductDTO> responseDTO = PageResponseDTO.<ProductDTO>withAll()
				.dtoList(dtoList)
				.pageRequestDTO(pageRequestDTO)
				.totalCount(totalCount)
				.build();
		
		return responseDTO;
	}

	@Override
	public OptionDetailDTO getOptionDetail(Long opno, Long pno) {
		
		Optional<OptionDetail> result = optionDetailRepository.getOptDetailWithOptAndPro(pno);
		
		OptionDetail optionDetail = result.orElseThrow();
		
		OptionDetailDTO dto = convertDetailToDTO(optionDetail);
		
		log.error("optional-optionaDetail : " + dto);
		
		return dto;
	}

	@Override
	public void updateProductStock(Long pno) {
		productRepository.updateProductStockByOptionDetail(pno);
		
		log.error("stockUpdate" + productRepository.updateProductStockByOptionDetail(pno));
	}

	@Override
	public List<OptionDetailDTO> getStockOptionDetails(Long pno) {
		
		List<OptionDetail> optionDetailList = optionDetailRepository.getProductStockByPno(pno);
		log.error("OptionDetail List : " + optionDetailList);
		return optionDetailList.stream()
				.map(this::convertDetailToDTO)
				.collect(Collectors.toList());
	}

	@Override
	public void decreaseOptionStock(Long odno) {
		int updateRows = optionDetailRepository.decreaseOptionStock(odno);
		if(updateRows > 0) {
			log.error("Stock decreased, odno : " + odno);
		}
		
	}
	
	@Override
	public List<OptionDetailDTO> getOptionsByProductId(Long pno) {
      List<OptionDetail> options = optionDetailRepository.getProductByPno(pno);
      return options.stream()
            .map(this::convertDetailToDTO)
            .collect(Collectors.toList());
	}
	
}
