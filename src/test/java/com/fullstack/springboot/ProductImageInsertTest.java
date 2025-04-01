package com.fullstack.springboot;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import com.fullstack.springboot.entity.product.Product;
import com.fullstack.springboot.entity.product.ProductImage;
import com.fullstack.springboot.repository.product.CategoryProductRepository;
import com.fullstack.springboot.repository.product.ProductImageRepository;

import lombok.extern.log4j.Log4j2;

@SpringBootTest
@Log4j2
public class ProductImageInsertTest {
	
	@Autowired
	private ProductImageRepository productImageRepository;
	
	@Autowired
	private CategoryProductRepository categoryProductRepository;
	
	@Value("${com.fullstack.springboot.uploadPath}")
	private String uploadPath;

	// @Test
	void insertImage() {
		for(int i = 1; i <= 30; i++) {
			//여성 의류 - 3, 여성 신발 - 4, 여성 가방/잡화 - 5
			//남성 의류 - 7, 신발 - 8, 가방/잡화 - 9
			//남녀 공용 티셔트 - 11, 맨투맨/후드티 - 12, 셔츠 - 13, 바지 - 14, 트레이닝복 - 15, 후드집업/집업류 - 16,
			//       니트류 - 17, 아우터 - 18, 테마의류 - 19, 커플룩/패밀리룩 - 20, 빅사이즈 - 21
			//속옷/잠옷 여성속옷 - 23, 남성속옷 - 24, 기타속옷용품 - 25, 잠옷/파자마 - 26
			//유아동 베이비 - 28, 여아 - 29, 남아 - 30
			List<Product> products = new ArrayList<Product>();
			int index = 1;
			int cnt = 1;
			
			switch (i) {
			case 3:
				products = categoryProductRepository.getProductInfoWithCate((long)i);
				for(Product p : products) {
					String fileName = "girl_clothes_" + cnt + ".jpg";					
					Path filePath = Paths.get(uploadPath, fileName);
					ProductImage productImage = ProductImage.builder().pi_name(fileName).pi_ord(0).pi_path(filePath.toString()).product(p).build();
					productImageRepository.save(productImage);
					index++;
					cnt++;
					if(cnt == 9) {
						cnt = 1;
					}
				}
				break;
			case 4:
				products = categoryProductRepository.getProductInfoWithCate((long)i);
				for(Product p : products) {
					String fileName = "girl_shose_" + cnt + ".jpg";					
					Path filePath = Paths.get(uploadPath, fileName);
					ProductImage productImage = ProductImage.builder().pi_name(fileName).pi_ord(0).pi_path(filePath.toString()).product(p).build();
					productImageRepository.save(productImage);
					index++;
					cnt++;
					if(cnt == 9) {
						cnt = 1;
					}
				}
				break;
			case 5:
				products = categoryProductRepository.getProductInfoWithCate((long)i);
				for(Product p : products) {
					String fileName = "girl_etc_" + cnt + ".jpg";					
					Path filePath = Paths.get(uploadPath, fileName);
					ProductImage productImage = ProductImage.builder().pi_name(fileName).pi_ord(0).pi_path(filePath.toString()).product(p).build();
					productImageRepository.save(productImage);
					index++;
					cnt++;
					if(cnt == 9) {
						cnt = 1;
					}
				}
				break;
			case 7:
				products = categoryProductRepository.getProductInfoWithCate((long)i);
				for(Product p : products) {
					String fileName = "men_clothes_" + cnt + ".jpg";					
					Path filePath = Paths.get(uploadPath, fileName);
					ProductImage productImage = ProductImage.builder().pi_name(fileName).pi_ord(0).pi_path(filePath.toString()).product(p).build();
					productImageRepository.save(productImage);
					index++;
					cnt++;
					if(cnt == 9) {
						cnt = 1;
					}
				}
				break;
			case 8:
				products = categoryProductRepository.getProductInfoWithCate((long)i);
				for(Product p : products) {
					String fileName = "men_shose_" + cnt + ".jpg";					
					Path filePath = Paths.get(uploadPath, fileName);
					ProductImage productImage = ProductImage.builder().pi_name(fileName).pi_ord(0).pi_path(filePath.toString()).product(p).build();
					productImageRepository.save(productImage);
					index++;
					cnt++;
					if(cnt == 9) {
						cnt = 1;
					}
				}
				break;
			case 9:
				products = categoryProductRepository.getProductInfoWithCate((long)i);
				for(Product p : products) {
					String fileName = "men_etc_" + cnt + ".jpg";					
					Path filePath = Paths.get(uploadPath, fileName);
					ProductImage productImage = ProductImage.builder().pi_name(fileName).pi_ord(0).pi_path(filePath.toString()).product(p).build();
					productImageRepository.save(productImage);
					index++;
					cnt++;
					if(cnt == 9) {
						cnt = 1;
					}
				}
				break;
			case 11:
				products = categoryProductRepository.getProductInfoWithCate((long)i);
				for(Product p : products) {
					String fileName = "unisex_t-shirt_" + cnt + ".jpg";					
					Path filePath = Paths.get(uploadPath, fileName);
					ProductImage productImage = ProductImage.builder().pi_name(fileName).pi_ord(0).pi_path(filePath.toString()).product(p).build();
					productImageRepository.save(productImage);
					index++;
					cnt++;
					if(cnt == 9) {
						cnt = 1;
					}
				}
				break;
			case 12:
				products = categoryProductRepository.getProductInfoWithCate((long)i);
				for(Product p : products) {
					String fileName = "unisex_hooded_" + cnt + ".jpg";					
					Path filePath = Paths.get(uploadPath, fileName);
					ProductImage productImage = ProductImage.builder().pi_name(fileName).pi_ord(0).pi_path(filePath.toString()).product(p).build();
					productImageRepository.save(productImage);
					index++;
					cnt++;
					if(cnt == 9) {
						cnt = 1;
					}
				}
				break;
			case 13:
				products = categoryProductRepository.getProductInfoWithCate((long)i);
				for(Product p : products) {
					String fileName = "unisex_shirt_" + cnt + ".jpg";					
					Path filePath = Paths.get(uploadPath, fileName);
					ProductImage productImage = ProductImage.builder().pi_name(fileName).pi_ord(0).pi_path(filePath.toString()).product(p).build();
					productImageRepository.save(productImage);
					index++;
					cnt++;
					if(cnt == 9) {
						cnt = 1;
					}
				}
				break;
			case 14:
				products = categoryProductRepository.getProductInfoWithCate((long)i);
				for(Product p : products) {
					String fileName = "unisex_pants_" + cnt + ".jpg";					
					Path filePath = Paths.get(uploadPath, fileName);
					ProductImage productImage = ProductImage.builder().pi_name(fileName).pi_ord(0).pi_path(filePath.toString()).product(p).build();
					productImageRepository.save(productImage);
					index++;
					cnt++;
					if(cnt == 9) {
						cnt = 1;
					}
				}
				break;
			case 15:
				products = categoryProductRepository.getProductInfoWithCate((long)i);
				for(Product p : products) {
					String fileName = "unisex_sportwaer_" + cnt + ".jpg";					
					Path filePath = Paths.get(uploadPath, fileName);
					ProductImage productImage = ProductImage.builder().pi_name(fileName).pi_ord(0).pi_path(filePath.toString()).product(p).build();
					productImageRepository.save(productImage);
					index++;
					cnt++;
					if(cnt == 9) {
						cnt = 1;
					}
				}
				break;
			case 16:
				products = categoryProductRepository.getProductInfoWithCate((long)i);
				for(Product p : products) {
					String fileName = "unisex_zip-up_" + cnt + ".jpg";					
					Path filePath = Paths.get(uploadPath, fileName);
					ProductImage productImage = ProductImage.builder().pi_name(fileName).pi_ord(0).pi_path(filePath.toString()).product(p).build();
					productImageRepository.save(productImage);
					index++;
					cnt++;
					if(cnt == 9) {
						cnt = 1;
					}
				}
				break;
			case 17:
				products = categoryProductRepository.getProductInfoWithCate((long)i);
				for(Product p : products) {
					String fileName = "unisex_kint_" + cnt + ".jpg";					
					Path filePath = Paths.get(uploadPath, fileName);
					ProductImage productImage = ProductImage.builder().pi_name(fileName).pi_ord(0).pi_path(filePath.toString()).product(p).build();
					productImageRepository.save(productImage);
					index++;
					cnt++;
					if(cnt == 9) {
						cnt = 1;
					}
				}
				break;
			case 18:
				products = categoryProductRepository.getProductInfoWithCate((long)i);
				for(Product p : products) {
					String fileName = "unisex_outer_" + cnt + ".jpg";					
					Path filePath = Paths.get(uploadPath, fileName);
					ProductImage productImage = ProductImage.builder().pi_name(fileName).pi_ord(0).pi_path(filePath.toString()).product(p).build();
					productImageRepository.save(productImage);
					index++;
					cnt++;
					if(cnt == 9) {
						cnt = 1;
					}
				}
				break;
			case 19:
				products = categoryProductRepository.getProductInfoWithCate((long)i);
				for(Product p : products) {
					String fileName = "unisex_costume_" + cnt + ".jpg";					
					Path filePath = Paths.get(uploadPath, fileName);
					ProductImage productImage = ProductImage.builder().pi_name(fileName).pi_ord(0).pi_path(filePath.toString()).product(p).build();
					productImageRepository.save(productImage);
					index++;
					cnt++;
					if(cnt == 9) {
						cnt = 1;
					}
				}
				break;
			case 20:
				products = categoryProductRepository.getProductInfoWithCate((long)i);
				for(Product p : products) {
					String fileName = "unisex_couple_" + cnt + ".jpg";					
					Path filePath = Paths.get(uploadPath, fileName);
					ProductImage productImage = ProductImage.builder().pi_name(fileName).pi_ord(0).pi_path(filePath.toString()).product(p).build();
					productImageRepository.save(productImage);
					index++;
					cnt++;
					if(cnt == 9) {
						cnt = 1;
					}
				}
				break;
			case 21:
				products = categoryProductRepository.getProductInfoWithCate((long)i);
				for(Product p : products) {
					String fileName = "unisex_bigsize_" + cnt + ".jpg";					
					Path filePath = Paths.get(uploadPath, fileName);
					ProductImage productImage = ProductImage.builder().pi_name(fileName).pi_ord(0).pi_path(filePath.toString()).product(p).build();
					productImageRepository.save(productImage);
					index++;
					cnt++;
					if(cnt == 9) {
						cnt = 1;
					}
				}
				break;
			case 23:
				products = categoryProductRepository.getProductInfoWithCate((long)i);
				for(Product p : products) {
					String fileName = "underwear_girl_" + cnt + ".jpg";					
					Path filePath = Paths.get(uploadPath, fileName);
					ProductImage productImage = ProductImage.builder().pi_name(fileName).pi_ord(0).pi_path(filePath.toString()).product(p).build();
					productImageRepository.save(productImage);
					index++;
					cnt++;
					if(cnt == 9) {
						cnt = 1;
					}
				}
				break;
			case 24:
				products = categoryProductRepository.getProductInfoWithCate((long)i);
				for(Product p : products) {
					String fileName = "underwear_men_" + cnt + ".jpg";					
					Path filePath = Paths.get(uploadPath, fileName);
					ProductImage productImage = ProductImage.builder().pi_name(fileName).pi_ord(0).pi_path(filePath.toString()).product(p).build();
					productImageRepository.save(productImage);
					index++;
					cnt++;
					if(cnt == 9) {
						cnt = 1;
					}
				}
				break;
//			case 25:
//				products = categoryProductRepository.getProductInfoWithCate((long)i);
//				for(Product p : products) {
//					String fileName = "girl_clothes_" + cnt + ".jpg";					
//					Path filePath = Paths.get(uploadPath, fileName);
//					ProductImage productImage = ProductImage.builder().pi_name(fileName).pi_ord(0).pi_path(filePath.toString()).product(p).build();
//					productImageRepository.save(productImage);
//					index++;
//					cnt++;
//					if(cnt == 9) {
//						cnt = 1;
//					}
//				}
//				break;
			case 26:
				products = categoryProductRepository.getProductInfoWithCate((long)i);
				for(Product p : products) {
					String fileName = "underwear_pajamas_" + cnt + ".jpg";					
					Path filePath = Paths.get(uploadPath, fileName);
					ProductImage productImage = ProductImage.builder().pi_name(fileName).pi_ord(0).pi_path(filePath.toString()).product(p).build();
					productImageRepository.save(productImage);
					index++;
					cnt++;
					if(cnt == 9) {
						cnt = 1;
					}
				}
				break;
			case 28:
				products = categoryProductRepository.getProductInfoWithCate((long)i);
				for(Product p : products) {
					String fileName = "baby_" + cnt + ".jpg";					
					Path filePath = Paths.get(uploadPath, fileName);
					ProductImage productImage = ProductImage.builder().pi_name(fileName).pi_ord(0).pi_path(filePath.toString()).product(p).build();
					productImageRepository.save(productImage);
					index++;
					cnt++;
					if(cnt == 9) {
						cnt = 1;
					}
				}
				break;
			case 29:
				products = categoryProductRepository.getProductInfoWithCate((long)i);
				for(Product p : products) {
					String fileName = "boy_" + cnt + ".jpg";					
					Path filePath = Paths.get(uploadPath, fileName);
					ProductImage productImage = ProductImage.builder().pi_name(fileName).pi_ord(0).pi_path(filePath.toString()).product(p).build();
					productImageRepository.save(productImage);
					index++;
					cnt++;
					if(cnt == 9) {
						cnt = 1;
					}
				}
				break;
			case 30:
				products = categoryProductRepository.getProductInfoWithCate((long)i);
				for(Product p : products) {
					String fileName = "girl_" + cnt + ".jpg";					
					Path filePath = Paths.get(uploadPath, fileName);
					ProductImage productImage = ProductImage.builder().pi_name(fileName).pi_ord(0).pi_path(filePath.toString()).product(p).build();
					productImageRepository.save(productImage);
					index++;
					cnt++;
					if(cnt == 9) {
						cnt = 1;
					}
				}
				break;
			default: break;
			}
		}
	}
	
	
}
