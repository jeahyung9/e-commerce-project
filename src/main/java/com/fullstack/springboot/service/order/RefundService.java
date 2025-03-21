package com.fullstack.springboot.service.order;

import java.util.List;

import org.springframework.data.domain.Page;

import com.fullstack.springboot.dto.order.IamportApiResult;
import com.fullstack.springboot.dto.order.RefundDTO;

public interface RefundService {
	
	
    // 환불 요청을 생성하는 메서드
    RefundDTO createRefundForOrder(Long ono, String refundReason, String impUid, String merchantUid);

    // 환불 요청을 승인하고 실제 환불을 처리하는 메서드
    IamportApiResult approveRefundRequest(Long rfno);

    List<RefundDTO> getMemberRefunds(Long mno);
    List<RefundDTO> getMemberRefundsByStatus(Long mno, boolean status);
    
    
}