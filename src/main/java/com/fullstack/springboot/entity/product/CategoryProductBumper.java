package com.fullstack.springboot.entity.product;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name="category_product_bumper")
public class CategoryProductBumper {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long cpno;
	
	@ManyToOne
	@JoinColumn(name = "cate_ctno")
	private Category category;
	
	@ManyToOne
	@JoinColumn(name = "product_pno")
	private Product product;
}
