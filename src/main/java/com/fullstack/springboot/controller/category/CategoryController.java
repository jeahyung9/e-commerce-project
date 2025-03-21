package com.fullstack.springboot.controller.category;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fullstack.springboot.dto.product.CategoryDTO;
import com.fullstack.springboot.service.product.CategoryService;
import com.fullstack.springboot.util.ConvertBase64Util;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/api/categories")
public class CategoryController {
	
	private final CategoryService categoryService;
	
	@GetMapping("/random")
	public List<CategoryDTO> getRandomCategories(@RequestParam("count") int count, @RequestParam("depth") int depth) {
		return categoryService.getRandomCategories(count, depth);
	}
	
	@GetMapping("/sub/{ctno}")
	public List<CategoryDTO> getSubCategories(@PathVariable("ctno") Long ctno) {
		return categoryService.getSubCategories(ctno);
	}
	
	@GetMapping("/super/{ctno}")
	public CategoryDTO getSuperCategories(@PathVariable("ctno") Long ctno) {
		return categoryService.getSuperCategory(ctno);
	}
	
	@GetMapping("/depth/{depth}")
	public List<CategoryDTO> getCategoriesByDepth(@PathVariable("depth") int depth) {
		return categoryService.getCategoriesByDepth(depth);
	}
	
	@PostMapping("/")
	public Map<String, Long> categoryRegister(@RequestBody CategoryDTO dto){
		log.error("CateDTO : " + dto);
		
		Long ctno = categoryService.registerCategory(dto);
		
		return Map.of("CTNO", ctno);
	}
	
	@PutMapping("/{encodedId}")
	public Map<String, String> categoryModify(@PathVariable String encodedId, @RequestBody CategoryDTO dto){
		Long ctno = ConvertBase64Util.decoding(encodedId);
		
		CategoryDTO updateDTO = dto.toBuilder().ctno(ctno).build();
		
		log.error("modify : " + updateDTO);
		
		categoryService.modifyCategory(updateDTO);
		
		return Map.of("RESULT", "SUCCESS");
	}
	
	@DeleteMapping("/{encodedId}")
	public Map<String, String> categoryDelete(@PathVariable String encodedId){
		Long ctno = ConvertBase64Util.decoding(encodedId);
		
		categoryService.deleteCategory(ctno);
		
		return Map.of("RESULT", "SUCCESS");
	}
	
}
