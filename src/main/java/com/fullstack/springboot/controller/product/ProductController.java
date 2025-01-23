package com.fullstack.springboot.controller.product;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fullstack.springboot.dto.PageRequestDTO;
import com.fullstack.springboot.dto.PageResponseDTO;
import com.fullstack.springboot.dto.product.OptionDetailDTO;
import com.fullstack.springboot.dto.product.ProductDTO;
import com.fullstack.springboot.repository.product.OptionDetailRepository;
import com.fullstack.springboot.service.product.ProductService;
import com.fullstack.springboot.util.ConvertBase64Util;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;


@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/api/product")
public class ProductController {
	
	private final ProductService productService;
	
	private final OptionDetailRepository optionDetailRepository;
	
	//내가 테스트 하려고 넣음
	@GetMapping("/read/{mno}")
	public List<OptionDetailDTO> readOne(@PathVariable("mno") Long mno) {
		return optionDetailRepository.getProductWithOption(mno);
	}
	
	@GetMapping("/{encodedId}")
	public ProductDTO readProduct(@PathVariable String encodedId) {
		Long pno = ConvertBase64Util.decoding(encodedId);
		
		return productService.readProduct(pno);
	}
	
	@GetMapping("/{encodedId}/option")
	public List<OptionDetailDTO> OptionList(@PathVariable String encodedId){
		Long pno = ConvertBase64Util.decoding(encodedId);
		
		return productService.getOptionsByProductId(pno);
	}
	
	@GetMapping("/{encodedProId}/option/{encodedOptId}")
	public OptionDetailDTO getOptionDetail(@PathVariable String encodedProId, @PathVariable String encodedOptId) {
		Long pno = ConvertBase64Util.decoding(encodedProId);
		Long opno = ConvertBase64Util.decoding(encodedOptId);
		
		return productService.getOptionDetail(opno, pno);
	}
	
	@GetMapping("/{encodedId}/selectableOpt")
	public List<OptionDetailDTO> getStockOptionDetail(@PathVariable String encodedId){
		Long pno = ConvertBase64Util.decoding(encodedId);
		
		return productService.getStockOptionDetails(pno);
	}
	
	@GetMapping("/list")
	public PageResponseDTO<ProductDTO> list(PageRequestDTO pageRequestDTO) {
		log.error(pageRequestDTO);
		
		return productService.productList(pageRequestDTO);
	}
	
	@PostMapping("/")
	public Map<String, Long> register(@RequestBody ProductDTO dto) {
		log.error("ProductDTO : " + dto);
		
		Long pno = productService.registerProduct(dto);
		
		return Map.of("PNO", pno);
	}
	
	@PutMapping("/{encodedId}")
	public Map<String, String> modifyProduct(
			@PathVariable String encodedId,
			@RequestBody ProductDTO productDTO) {
		Long pno = ConvertBase64Util.decoding(encodedId);
		
		productDTO.setPno(pno);
		
		log.error("Modify : " + productDTO);
		
		productService.modifyProduct(productDTO);
		
		return Map.of("RESULT", "SUCCESS");
	}
	
	@PutMapping("/{encodedId}/updateStock")
	public Map<String, String> updateStock(@PathVariable String encodedId){
		Long pno = ConvertBase64Util.decoding(encodedId);
		
		productService.updateProductStock(pno);
		
		return Map.of("RESULT", "SUCCESS");
	}
	
	@PutMapping("/option/{encodedId}/decreaseStock/")
	public Map<String, String> decreaseOptionStock(@PathVariable String encodedId){
		Long odno = ConvertBase64Util.decoding(encodedId);
		
		productService.decreaseOptionStock(odno);
		
		return Map.of("RESULT", "SUCCESS");
	}
	
	@DeleteMapping("/{encodedId}")
	public Map<String, String> deleteProduct(@PathVariable String encodedId) {
		Long pno = ConvertBase64Util.decoding(encodedId);
		
		log.error("Delete : " + pno);
		
		productService.deleteProduct(pno);
		
		return Map.of("RESULT", "SUCCESS");
	}
	
}
