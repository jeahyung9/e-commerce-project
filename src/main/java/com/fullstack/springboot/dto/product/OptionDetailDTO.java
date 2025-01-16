package com.fullstack.springboot.dto.product;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OptionDetailDTO {
	
	private Long odno;
	
	private String od_name; 
	
    private int od_stock;  
    
    private Long od_price;
    
    private Long pno;
    
    //추가 코드(테스트 용)
    private String p_name;
    
    private Long p_price;
    
    private int p_salePer;
    
    private int p_stock;
    
    private boolean delFlag;
    
    private Long sno;
    
    private String businessName;
    
}
