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
    
    private int od_salesVol;
    
    private Long od_price;
    
    private Long pno;
    
}
