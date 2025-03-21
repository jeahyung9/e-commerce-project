package com.fullstack.springboot.service.order;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fullstack.springboot.dto.order.OrderDetailDTO;
import com.fullstack.springboot.dto.product.ProductImageDTO;
import com.fullstack.springboot.entity.order.Order;
import com.fullstack.springboot.entity.order.OrderDetail;
import com.fullstack.springboot.entity.product.OptionDetail;
import com.fullstack.springboot.entity.product.Product;
import com.fullstack.springboot.external.payment.exception.ErrorCode;
import com.fullstack.springboot.external.payment.exception.PaymentException;
import com.fullstack.springboot.repository.order.OrderDetailRepository;
import com.fullstack.springboot.repository.order.OrderRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderDetailServiceImpl implements OrderDetailService {

    private final OrderDetailRepository orderDetailRepository;
    private final OrderRepository orderRepository;

    @Override
    @Transactional
    public OrderDetailDTO createOrderDetail(OrderDetailDTO orderDetailDTO) {
        Order order = orderRepository.findById(orderDetailDTO.getOno())
                .orElseThrow(() -> new EntityNotFoundException("주문을 찾을 수 없습니다: " + orderDetailDTO.getOno()));

        OrderDetail orderDetail = OrderDetail.builder()
                .or_price(orderDetailDTO.getOr_price())
                .or_count(orderDetailDTO.getOr_count())
                //.review_flag(false)
                .rno(null)  // review_flag 대신 rno 사용
                .order(order)
                .build();

        OrderDetail savedOrderDetail = orderDetailRepository.save(orderDetail);
        return convertToDTO(savedOrderDetail);
    }
    
    

    @Override
    public List<OrderDetailDTO> getOrderDetails(Long ono) {
        log.info("========== 주문 상세 목록 조회 시작 ==========");
        log.debug("주문번호: {}", ono);

        try {
            List<OrderDetail> orderDetailList = orderDetailRepository.findOrderDetailsWithAllInfo(ono);
            log.info("조회된 주문 상세 수: {}", orderDetailList.size());
            
            List<OrderDetailDTO> orderDetailDTOList = orderDetailList.stream()
                    .map(detail -> {
                        try {
                            return convertToDTO(detail);
                        } catch (Exception e) {
                            log.error("주문 상세 변환 중 오류 - orno: {}, error: {}", 
                                    detail.getOrno(), e.getMessage());
                            return null;
                        }
                    })
                    .filter(dto -> dto != null)
                    .collect(Collectors.toList());

            log.info("변환된 DTO 수: {}", orderDetailDTOList.size());
            return orderDetailDTOList;
        } catch (Exception e) {
            log.error("주문 상세 조회 중 오류 발생: {}", e.getMessage(), e);
            throw new RuntimeException("주문 상세 정보 조회 중 오류가 발생했습니다.", e);
        }
    }

    @Override
    @Transactional
    public OrderDetailDTO updateReviewStatus(Long orno, Long rno) {
        log.info("========== 리뷰 번호 업데이트 시작 ==========");
        log.debug("주문 상세 번호: {}, 리뷰 번호: {}", orno, rno);

        OrderDetail orderDetail = orderDetailRepository.findById(orno)
                .orElseThrow(() -> {
                    log.error("주문 상세를 찾을 수 없음 - 상세번호: {}", orno);
                    return new EntityNotFoundException("주문 상세를 찾을 수 없습니다: " + orno);
                });

        orderDetail.setRno(rno);
        OrderDetail updatedOrderDetail = orderDetailRepository.save(orderDetail);
        
        log.info("리뷰 번호 업데이트 완료 - 상세번호: {}, 리뷰번호: {}", orno, rno);
        return convertToDTO(updatedOrderDetail);
    }
    
    	

    @Override
    public List<OrderDetailDTO> getReviewableOrders(Long mno) {
        log.info("========== 리뷰 작성 가능 목록 조회 시작 ==========");
        log.debug("회원번호: {}", mno);

        List<OrderDetail> orderDetailList = orderDetailRepository.findReviewableByMno(mno);
        List<OrderDetailDTO> orderDetailDTOList = orderDetailList.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        log.info("리뷰 작성 가능 목록 조회 완료 - 항목 수: {}", orderDetailDTOList.size());
        return orderDetailDTOList;
    }

    
    
    
    
    
    
    
    
    
    private OrderDetailDTO convertToDTO(OrderDetail orderDetail) {
        try {
            log.info("Converting OrderDetail to DTO - orno: {}", orderDetail.getOrno());
            
            Order order = orderDetail.getOrder();
            log.info("Order info - ono: {}", order.getOno());
            
            OptionDetail optionDetail = orderDetail.getOptionDetail();
            log.info("OptionDetail info - odno: {}", optionDetail != null ? optionDetail.getOdno() : "null");
            
            if (optionDetail == null) {
                log.warn("주문 상세 번호 {}의 옵션 정보가 없습니다.", orderDetail.getOrno());
                return buildBasicOrderDetailDTO(orderDetail, order);
            }

            Product product = optionDetail.getProduct();
            log.info("Product info - pno: {}", product != null ? product.getPno() : "null");
            
            if (product == null) {
                log.warn("옵션 번호 {}의 상품 정보가 없습니다.", optionDetail.getOdno());
                return buildBasicOrderDetailDTO(orderDetail, order);
            }

            log.info("Product images size: {}", 
                product.getProductImage() != null ? product.getProductImage().size() : "null");

            // ProductImage를 ProductImageDTO로 변환 (null 체크 강화)
            List<ProductImageDTO> productImageDTOs = new ArrayList<>();
            if (product.getProductImage() != null && !product.getProductImage().isEmpty()) {
                try {
                    productImageDTOs = product.getProductImage().stream()
                            .filter(image -> image != null)
                            .map(image -> {
                                log.info("Converting image - pino: {}", image.getPino());
                                return ProductImageDTO.builder()
                                        .pino(image.getPino())
                                        .pi_name(image.getPi_name())
                                        .pi_path(image.getPi_path())
                                        .pi_ord(image.getPi_ord())
                                        .pno(product.getPno())
                                        .build();
                            })
                            .collect(Collectors.toList());
                    log.info("Converted {} product images", productImageDTOs.size());
                } catch (Exception e) {
                    log.error("이미지 변환 중 오류 발생: {}", e.getMessage());
                }
            }

            return OrderDetailDTO.builder()
                    .orno(orderDetail.getOrno())
                    .or_price(orderDetail.getOr_price())
                    .or_count(orderDetail.getOr_count())
                    .odno(optionDetail.getOdno())
                    .ono(order.getOno())
                    .rno(orderDetail.getRno())
                    // Product 정보
                    .p_name(product.getP_name())
                    .pno(product.getPno())
                    .p_content(product.getP_content())
                    .p_price(product.getP_price())
                    .p_salePer(product.getP_salePer())
                    .p_stock(product.getP_stock())
                    .p_salesVol(product.getP_salesVol())
                    // 옵션 정보
                    .od_name(optionDetail.getOd_name())
                    .od_price(optionDetail.getOd_price())
                    // 상품 이미지
                    .productImage(productImageDTOs)
                    .build();
        } catch (Exception e) {
            log.error("DTO 변환 중 오류 발생: {}", e.getMessage(), e);
            throw new RuntimeException("주문 상세 정보 변환 중 오류가 발생했습니다: " + e.getMessage(), e);
        }
    }
    
    
    private OrderDetailDTO buildBasicOrderDetailDTO(OrderDetail orderDetail, Order order) {
        return OrderDetailDTO.builder()
                .orno(orderDetail.getOrno())
                .or_price(orderDetail.getOr_price())
                .or_count(orderDetail.getOr_count())
                .odno(null)
                .ono(order.getOno())
                .rno(orderDetail.getRno())
                // Product 정보 기본값
                .p_name("")
                .pno(null)
                .p_content("")
                .p_price(0L)
                .p_salePer(0)
                .p_stock(0)
                .p_salesVol(0)
                // 옵션 정보 기본값
                .od_name("")
                .od_price(0L)
                // 빈 이미지 리스트
                .productImage(new ArrayList<>())
                .build();
    }
}