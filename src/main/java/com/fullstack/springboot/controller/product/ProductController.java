package com.fullstack.springboot.controller.product;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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
	
	@GetMapping("/{encodedId}")
	public ProductDTO readProduct(@PathVariable("encodedId") String encodedId) {
		Long pno = ConvertBase64Util.decoding(encodedId);
		
		return productService.readProduct(pno);
	}
	
	@GetMapping("/option/{encodedId}")
	public List<OptionDetailDTO> OptionList(@PathVariable("encodedId") String encodedId){
		Long pno = ConvertBase64Util.decoding(encodedId);
		
		return productService.getOptionsByProductId(pno);
	}
	
	@GetMapping("/{encodedProId}/option")
	public OptionDetailDTO getOptionDetail(@PathVariable("encodedId") String encodedProId) {
		Long pno = ConvertBase64Util.decoding(encodedProId);
		
		return productService.getOptionDetail(pno);
	}
	
	@GetMapping("/{encodedId}/selectableOpt")
	public List<OptionDetailDTO> getStockOptionDetail(@PathVariable("encodedId") String encodedId){
		Long pno = ConvertBase64Util.decoding(encodedId);
		
		return productService.getStockOptionDetails(pno);
	}
	
	@GetMapping("/list")
	public PageResponseDTO<ProductDTO> list(PageRequestDTO pageRequestDTO,
			@RequestParam(value = "sort", required = false, defaultValue = "pno") String sort, // sort field
			@RequestParam(value = "ctno", required = false, defaultValue = "0") Long ctno, // category number 
			@RequestParam(value = "order", required = false) boolean order, // false = desc, true = asc
			@RequestParam(value = "keyword", required = false) String keyword
			) {
		return productService.productList(pageRequestDTO, sort, ctno, order, keyword);
	}
	
	// @PostMapping("/")
	// public void register(
	// 		@RequestPart("dto") ProductDTO dto, 
	// 		@RequestParam("ctnoList") List<Long> ctnoList, 
	// 		@RequestParam("images") List<MultipartFile> images, 
	// 		@RequestParam("opts") List<OptionDetailDTO> opts) throws IOException {
	// 	log.error("ProductDTO : " + dto);
	// 	//오류떠서 잠시 주석처리
	// 	Long pno = productService.registerProduct(dto, ctnoList, images, opts);
		
	// }

	@PostMapping("/")
  	public Map<String, Long> register(
		@RequestPart("dto") String dtoJson,
		@RequestPart("ctnoList") String ctnoListJson,
		@RequestPart("images") List<MultipartFile> images,
		@RequestPart("opts") String optsJson) throws IOException {

		log.info("optsJson : " + optsJson);
		log.info("ctnoListJson : " + ctnoListJson);
		log.info("dtoJson : " + dtoJson);

		// JSON 데이터를 파싱하여 DTO 객체로 변환
		ObjectMapper objectMapper = new ObjectMapper();
		ProductDTO dto = objectMapper.readValue(dtoJson, ProductDTO.class);
		List<Long> ctnoList = objectMapper.readValue(ctnoListJson, new TypeReference<List<Long>>() {});
		List<OptionDetailDTO> opts = objectMapper.readValue(optsJson, new TypeReference<List<OptionDetailDTO>>() {});

		log.info("dto : " + dto);
		log.info("ctnoList : " + ctnoList);
		log.info("images : " + images);
		log.info("opts : " + opts);

		Long pno = productService.registerProduct(dto, ctnoList, images, opts);
		
		return Map.of("PNO", pno);
	}
	
	@PutMapping("/{encodedId}")
	public Map<String, String> modifyProduct(
			@PathVariable("encodedId") String encodedId,
			@RequestBody ProductDTO productDTO,
			@RequestPart(value = "newImages", required = false) List<MultipartFile> newImages,
			@RequestPart(value = "deleteOrdList", required = false) List<Integer> deleteOrdList) throws IOException {
		Long pno = ConvertBase64Util.decoding(encodedId);
		
		ProductDTO modifiedProductDTO = productDTO.toBuilder().pno(pno).build();
		
		log.error("Modify : " + modifiedProductDTO);
		
		productService.modifyProduct(modifiedProductDTO, newImages,deleteOrdList);
		
		return Map.of("RESULT", "SUCCESS");
	}
	
	@PutMapping("/{encodedId}/updateStock")
	public Map<String, String> updateStock(@PathVariable("encodedId") String encodedId){
		Long pno = ConvertBase64Util.decoding(encodedId);
		
		productService.updateProductStock(pno);
		
		return Map.of("RESULT", "SUCCESS");
	}
	
	@PutMapping("/option/{encodedId}/decreaseStock/{cnt}")
	public Map<String, String> decreaseOptionStock(@PathVariable("encodedId") String encodedId, @PathVariable("cnt") int cnt){
		Long odno = ConvertBase64Util.decoding(encodedId);
		
		productService.decreaseOptionStock(odno, cnt);
		
		return Map.of("RESULT", "SUCCESS");
	}

	@PutMapping("/option/{encodedId}/increaseStock/{cnt}")
	public Map<String, String> increaseOptionStock(@PathVariable("encodedId") String encodedId, @PathVariable("cnt") int cnt){
		Long odno = ConvertBase64Util.decoding(encodedId);
		
		productService.increaseOptionStock(odno, cnt);
		
		return Map.of("RESULT", "SUCCESS");
	}
	
	@PutMapping("/del/{encodedId}") // db에서 완전 삭제가 아닌 삭제 상태 false -> true 로 변경
	public Map<String, String> deleteProduct(@PathVariable("encodedId") String encodedId, boolean status) {
		Long pno = ConvertBase64Util.decoding(encodedId);
		
		log.error("Delete : " + pno);
		
		productService.updateToDelFlag(pno, status);
		
		return Map.of("RESULT", "SUCCESS");
	}
	
}
