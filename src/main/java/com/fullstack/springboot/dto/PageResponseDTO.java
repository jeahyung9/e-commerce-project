package com.fullstack.springboot.dto;


import lombok.Builder;
import lombok.Data;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.fullstack.springboot.dto.review.ReviewDTO;
import com.fullstack.springboot.entity.review.ProductReview;

@Data
public class PageResponseDTO<E> {

  private List<E> dtoList;

  private List<Integer> pageNumList;

  private PageRequestDTO pageRequestDTO;

  private boolean prev, next;

  private int totalCount, prevPage, nextPage, totalPage, current;

  @Builder(builderMethodName = "withAll")
  public PageResponseDTO(List<E> dtoList, PageRequestDTO pageRequestDTO, long totalCount) {

    this.dtoList = dtoList;
    this.pageRequestDTO = pageRequestDTO;
    this.totalCount = (int)totalCount;
    
//    if (pageRequestDTO.getSortBy() != null && !pageRequestDTO.getSortBy().isEmpty()) {
//        String sortBy = pageRequestDTO.getSortBy();
//        String sortOrder = pageRequestDTO.getSortOrder() != null ? pageRequestDTO.getSortOrder() : "desc";
//        
//        Comparator<E> comparator = getComparatorForField(sortBy);
//
//        if ("desc".equalsIgnoreCase(sortOrder)) {
//            comparator = comparator.reversed();
//        }
//
////        this.dtoList = this.dtoList.stream()
////                .sorted(comparator)
////                .collect(Collectors.toList());
//        System.out.println(5);
//    }

    int end =   (int)(Math.ceil( pageRequestDTO.getPage() / 10.0 )) *  10;

    int start = end - 9;

    int last =  (int)(Math.ceil((totalCount/(double)pageRequestDTO.getSize())));

    end =  end > last ? last: end;

    this.prev = start > 1;


    this.next =  totalCount > end * pageRequestDTO.getSize();

    this.pageNumList = IntStream.rangeClosed(start,end).boxed().collect(Collectors.toList());

    if(prev) {
        this.prevPage = start -1;
    }

    if(next) {
        this.nextPage = end + 1;
    }

    this.totalPage = this.pageNumList.size();

    this.current = pageRequestDTO.getPage();
  }
  
//  private Comparator<E> getComparatorForField(String sortBy) {
//      if ("reviewLike".equalsIgnoreCase(sortBy)) {
//          return Comparator.comparing(e -> ((ProductReview) e).getReviewLike());
//      }
//      return Comparator.comparing(e -> ((ProductReview) e).getRno());
//  }
}