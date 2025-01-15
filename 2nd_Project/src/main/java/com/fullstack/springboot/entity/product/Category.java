package com.fullstack.springboot.entity.product;


import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString(exclude = "cateList")
@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name="category")
public class Category {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long ctno;
	
	private String cateName;
	
	//셀프 참조 부모 정의
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "superCate")
	private Category superCate;
	
	private int cateDepth;
	
	//셀프 참조 자식 정의, 하위 카테고리를 리스트로 받음(Set 불가)
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "superCate")
	private List<Category> cateList; // depth 3 정도까지 가면 하위 카테고리 리스트가 어떻게 튀어나오느냐
}
