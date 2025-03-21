package com.fullstack.springboot;

import java.io.File;
import java.nio.file.Path;
import java.util.List;
import java.util.Random;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.fullstack.springboot.dto.PageRequestDTO;
import com.fullstack.springboot.entity.product.Category;
import com.fullstack.springboot.entity.product.CategoryProductBumper;
import com.fullstack.springboot.entity.product.Product;
import com.fullstack.springboot.repository.product.CategoryProductRepository;
import com.fullstack.springboot.repository.product.CategoryRepository;
import com.fullstack.springboot.repository.product.ProductRepository;
import com.fullstack.springboot.service.product.ProductService;

import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;

@SpringBootTest
@Log4j2
public class ProductTest {
	
	@Autowired
	ProductRepository productRepository;
	
	@Autowired
	ProductService productService;
	
	@Autowired
	CategoryRepository categoryRepository;
	
	@Autowired
	CategoryProductRepository categoryProductRepository;
	
	//@Test
	@Transactional
	void getProduct() {
		PageRequestDTO pageRequestDTO = PageRequestDTO.builder().page(1).size(10).build();
		
		//System.out.println("나오냐" + productService.productList(pageRequestDTO, "pno", 1L, false));
	}
	
	//@Test
	void insertProduct() {
		
		Random random = new Random();
	       
	       for(int i = 101; i <= 400; i++) {
	          
	          List<Long> ctnoList = categoryRepository.getCtnoWithDepth(3);
	          int randomIndexNum = random.nextInt(ctnoList.size()); 
	          Long randomCtno = ctnoList.get(randomIndexNum);
	          
	          Category category = Category.builder().ctno(randomCtno).build();
	          Product product = Product.builder().pno((long)i).build();
	          
	          CategoryProductBumper cpBumper = CategoryProductBumper.builder()
	                            .category(category)
	                            .product(product)
	                            .build();
	          
	          categoryProductRepository.save(cpBumper);
	       }

		
	}
	
	//@Test
	void createFolder() {
		for(int i = 1; i <= 1000; i++) {
			String path = "c:\\upload\\" + i;
			File folder = new File(path);
			if(!folder.exists()) {
				folder.mkdir();
			}			
		}
	}

}
