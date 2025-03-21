package com.fullstack.springboot.dto.order;

import java.time.LocalDateTime;
import java.util.List;

import com.fullstack.springboot.dto.product.ProductImageDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailDTO {
    private Long orno;              // 주문상세번호
    private int or_price;          	// 상품가격
    private int or_count;          	// 상품수량
    private Long odno;				// 상품 옵션 번호
    private Long ono;         		// 주문번호(FK)
    
    
    //private boolean review_flag;    // 리뷰 작성 여부
    private Long rno;
    
    // Order 
    private Long o_totalPrice;
    private String o_address;
    private String o_reciver;
    private String o_reciverPhoNum;
    private String status;
    private LocalDateTime orderDate;
    
    // Product 정보 추가
    private String p_name;   //product에 있어.
    private Long pno;
    private String p_content;
    private Long p_price;
    private Integer p_salePer;
    private Integer p_stock;
    private Integer p_salesVol;
    
    //옵션 정보
    private String od_name;  //option detail에 있음
    private Long od_price;        // 옵션 가격

    private List<ProductImageDTO> productImage;  // productImages -> productImage로 변경
    
    
}