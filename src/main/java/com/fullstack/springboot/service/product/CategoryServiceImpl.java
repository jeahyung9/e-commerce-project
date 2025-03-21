package com.fullstack.springboot.service.product;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.fullstack.springboot.dto.product.CategoryDTO;
import com.fullstack.springboot.entity.product.Category;
import com.fullstack.springboot.repository.product.CategoryRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

	private final CategoryRepository categoryRepository;

	@Override
	public Long registerCategory(CategoryDTO categoryDTO) {
		Category category = categoryRepository.findById(categoryDTO.getCtno())
				.orElseThrow(() -> new RuntimeException("해당 카테고리를 찾을 수 없음"));

		category = dtoToEntity(categoryDTO);

		Category res = categoryRepository.save(category);

		return res.getCtno();
	}

	@Override
	public void modifyCategory(CategoryDTO categoryDTO) {
		Category category = categoryRepository.findById(categoryDTO.getCtno())
				.orElseThrow(() -> new RuntimeException("해당 카테고리를 찾을 수 없음"));

		category = dtoToEntity(categoryDTO);

		categoryRepository.save(category);
	}

	@Override
	public void deleteCategory(Long ctno) {
		Category category = categoryRepository.findById(ctno).get();

		List<Category> underCateList = category.getCateList();
		if (!underCateList.isEmpty() && underCateList != null) {
			for (Category underCate : underCateList) {

				List<Category> deepestList = underCate.getCateList();

				if (!deepestList.isEmpty() && deepestList != null) {

					for (Category deepestCate : deepestList) {
						categoryRepository.deleteById(deepestCate.getCtno()); // 최하위 삭제
						log.error("최하위 삭제됨");
					}

				}
				categoryRepository.deleteById(underCate.getCtno()); // 하위 삭제
				log.error("하위 삭제됨");
			}
		}
		categoryRepository.deleteById(ctno); // 선택 카테 삭제
		log.error("선택 카테고리 삭제됨");
	}
	
	@Override
	public List<CategoryDTO> getCategoriesByDepth(int depth) {
		List<Category> depthNCategories = categoryRepository.getCateWithDepth(depth);
		
		return depthNCategories.stream().map(cate -> entityToDTO(cate)).collect(Collectors.toList());
	}

	@Override
	public List<CategoryDTO> getRandomCategories(int count, int depth) {
		List<Category> depthNCategories = categoryRepository.getCateWithDepth(depth);

		if (depthNCategories.size() < count) {
			count = depthNCategories.size();
		}

		Collections.shuffle(depthNCategories);

		return depthNCategories.stream().limit(count).map(cate -> entityToDTO(cate)).collect(Collectors.toList());
	}

	@Override
	public List<CategoryDTO> getSubCategories(Long superCtno) {
		Category category = categoryRepository.getCateWithCateList(superCtno);
		return category.getCateList().stream().map(cate -> entityToDTO(cate)).collect(Collectors.toList());
	}
	
	@Override
	public CategoryDTO getSuperCategory(Long subCtno) {
		Category category = categoryRepository.getCateWithCateList(subCtno);
		CategoryDTO superDTO = CategoryDTO.builder().ctno(category.getSuperCate().getCtno()).build();
		return superDTO;
	}

	private CategoryDTO entityToDTO(Category category) {
		List<CategoryDTO> ctDTOList = new ArrayList<>();
		List<Category> ctList = category.getCateList();

		for (Category ct : ctList) {
			// depth2 카테고리 정보 (superCate)
			CategoryDTO superDTO = null;
			if (ct.getSuperCate() != null) {
				// depth1 카테고리 정보 (superCate의 superCate)
				CategoryDTO superSuperDTO = null;
				if (ct.getSuperCate().getSuperCate() != null) {
					superSuperDTO = CategoryDTO.builder()
							.ctno(ct.getSuperCate().getSuperCate().getCtno())
							.cateName(ct.getSuperCate().getSuperCate().getCateName())
							.cateDepth(ct.getSuperCate().getSuperCate().getCateDepth())
							.build();
				}
				
				superDTO = CategoryDTO.builder()
						.ctno(ct.getSuperCate().getCtno())
						.cateName(ct.getSuperCate().getCateName())
						.cateDepth(ct.getSuperCate().getCateDepth())
						.superCate(superSuperDTO)  // depth1 정보 포함
						.build();
			}

			CategoryDTO ctDTO = CategoryDTO.builder()
					.ctno(ct.getCtno())
					.cateName(ct.getCateName())
					.cateDepth(ct.getCateDepth())
					.superCate(superDTO)  // depth2 정보 포함
					.build();
			ctDTOList.add(ctDTO);
		}

		// 현재 카테고리의 상위 카테고리 정보
		CategoryDTO superDTO = null;
		if (category.getSuperCate() != null) {
			CategoryDTO superSuperDTO = null;
			if (category.getSuperCate().getSuperCate() != null) {
				superSuperDTO = CategoryDTO.builder()
						.ctno(category.getSuperCate().getSuperCate().getCtno())
						.cateName(category.getSuperCate().getSuperCate().getCateName())
						.cateDepth(category.getSuperCate().getSuperCate().getCateDepth())
						.build();
			}

			superDTO = CategoryDTO.builder()
					.ctno(category.getSuperCate().getCtno())
					.cateName(category.getSuperCate().getCateName())
					.cateDepth(category.getSuperCate().getCateDepth())
					.superCate(superSuperDTO)
					.build();
		}

		return CategoryDTO.builder()
				.ctno(category.getCtno())
				.cateName(category.getCateName())
				.cateDepth(category.getCateDepth())
				.superCate(superDTO)
				.cateDTOList(ctDTOList)
				.build();
	}

	private Category dtoToEntity(CategoryDTO dto) {

		Category existingCategory = categoryRepository.findById(dto.getCtno())
				.orElseThrow(() -> new RuntimeException("해당 번호의 상품 찾을수 없음"));

		Category superCate = null;
		if (dto.getSuperCate() != null) {
			superCate = categoryRepository.findById(dto.getSuperCate().getCtno())
					.orElseThrow(() -> new RuntimeException("상위 카테고리를 찾을 수 없음"));
		}

//		List<CategoryDTO> ctDTOList = dto.getCateDTOList();

		List<Category> ctList = new ArrayList<>();

		if (dto.getCateDTOList() != null) {
			for (CategoryDTO cateDTO : dto.getCateDTOList()) {
				Category subCategory = Category.builder().ctno(cateDTO.getCtno()).cateName(cateDTO.getCateName())
						.cateDepth(cateDTO.getCateDepth()).superCate(existingCategory) // 현재 카테고리를 상위 카테고리로 설정
						.build();
				ctList.add(subCategory);
			}
		}

		return Category.builder().ctno(dto.getCtno()).cateName(dto.getCateName()).cateDepth(dto.getCateDepth())
				.superCate(superCate).cateList(ctList).build();
	}

}
