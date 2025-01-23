package com.fullstack.springboot.entity.product;

import java.util.ArrayList;
import java.util.List;

import com.fullstack.springboot.entity.BaseEntity;

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
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
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
	private int p_stock; // 재고
	
	private int p_salesVol; // 판매량
	
	private boolean delFlag;
	
	@ManyToOne(fetch = FetchType.LAZY)
	private Seller seller; // 판매자 
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy="pino")
	private List<ProductImage> productImage = new ArrayList<>();
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "odno")
	private List<OptionDetail> optionDetails = new ArrayList<>();
	
	public void changeDel(boolean delFlag) {
		this.delFlag = delFlag;
	}
	public void changeName(String name) {
		this.p_name = name;
	}
	
	public void changePrice(Long price) {
		this.p_price = price;
	}
	
	public void changeContent(String content) {
		this.p_content = content;
	}
	
	public void totalStockCount() {
		int totalStock = optionDetails.stream().mapToInt(OptionDetail::getOd_stock).sum();
		
		this.p_stock=totalStock;
	}
	
}
