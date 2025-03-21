package com.fullstack.springboot.entity.product;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fullstack.springboot.entity.BaseEntity;
import com.fullstack.springboot.entity.seller.Seller;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = {"seller", "optionDetails", "categoryProductBumpers", "productImage"})
@Table(name="product")
public class Product extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long pno;
	
	@Column(nullable = false)
	private String p_name; // 이름
	
	private String p_content; // 내용
	
	@Column(nullable = false)
	private Long p_price; // 가격
	
	private int p_salePer; // 할인율
	
	@Column(nullable = false)
	@Builder.Default
	private int p_stock = 0; // 재고
	
	@Builder.Default
	private Integer p_salesVol = 0; // 판매량
	
	@Column(columnDefinition = "TINYINT(1)")
	private boolean delFlag;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JsonIgnore
	private Seller seller; // 판매자
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy="pino")
	private List<ProductImage> productImage = new ArrayList<>();
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "odno")
	@JsonIgnore
	private List<OptionDetail> optionDetails = new ArrayList<>();
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "cpno")
	@JsonIgnore
	private List<CategoryProductBumper> categoryProductBumpers = new ArrayList<>();
	
	public void totalStockCount() {
		int totalStock = optionDetails.stream().mapToInt(OptionDetail::getOd_stock).sum();
		
		this.p_stock=totalStock;
	}
	
	//내가 추가 테스트
	public void changeStock(int stock) {
		this.p_stock = stock;
	}
	
}
