package com.fullstack.springboot.service.product;

import java.util.List;

import com.fullstack.springboot.dto.product.CategoryDTO;

public interface CategoryService {

	Long registerCategory(CategoryDTO categoryDTO);

	void modifyCategory(CategoryDTO categoryDTO);

	void deleteCategory(Long ctno);
	
	List<CategoryDTO> getCategoriesByDepth(int depth);

	List<CategoryDTO> getRandomCategories(int count, int depth); // count는 가져올 카테고리 개수

	List<CategoryDTO> getSubCategories(Long superCtno); // 하위 카테고리 가져오기

	CategoryDTO getSuperCategory(Long subCtno); // 하위 카테고리 가져오기
	
}
