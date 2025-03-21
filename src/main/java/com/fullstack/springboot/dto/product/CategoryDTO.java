package com.fullstack.springboot.dto.product;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDTO {
   
   private Long ctno;
   
   private String cateName;
   
   @JsonProperty("superCate")
   private CategoryDTO superCate;
   
   @JsonProperty("cateList")
   private List<CategoryDTO> cateDTOList;
   
   private int cateDepth;

}