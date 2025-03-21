package com.fullstack.springboot.entity.seller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fullstack.springboot.dto.admin.SuperAuth;
import com.fullstack.springboot.entity.BaseEntity;
import com.fullstack.springboot.entity.product.Product;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@Table(name = "seller")
public class Seller extends BaseEntity{

	//댓글번호, 댓글내용, 작성자를 필드로 추가
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long sno;
	
	@Column(nullable = false)
	private String s_id;
	
	@Column(nullable = false)
	private String s_pw;
	
	@Column(nullable = false)
	private String businessName;
	
	@Column(nullable = false)
	private String businessNum;
	
	private String s_num;
	
	private String s_email;
	
	private String s_addr;
	
	@Builder.Default
	private Long totalSales = 0L;
	
	@ElementCollection(fetch = FetchType.LAZY)
    @Builder.Default
    private Set<SuperAuth> superAuth = new HashSet<SuperAuth>();
   
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "pno")
    private List<Product> productList = new ArrayList<>();
   
    public void addSuperAuthSet(SuperAuth sellerAuth) {
       superAuth.add(sellerAuth);
    }
	
}
