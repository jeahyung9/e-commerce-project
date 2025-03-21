package com.fullstack.springboot.external.payment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fullstack.springboot.dto.order.IamportApiResult;
import com.fullstack.springboot.dto.order.IamportResponseDTO;
import com.fullstack.springboot.external.payment.exception.ErrorCode;
import com.fullstack.springboot.external.payment.exception.PaymentException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class IamportClient {

    private final RestTemplate restTemplate;
    private final IamportConfig iamportConfig;
    
    private static final String API_URL = "https://api.iamport.kr";
    
    public String getAccessToken() {
        log.debug("아임포트 토큰 발급 요청 시작");

        try {
            String url = API_URL + "/users/getToken";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            String requestBody = String.format("{\"imp_key\":\"%s\", \"imp_secret\":\"%s\"}",
                    iamportConfig.getApiKey(), iamportConfig.getApiSecret());

            
            log.error("reqBody-->" + requestBody);
            HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
            ResponseEntity<Map> response = restTemplate.postForEntity(url, entity, Map.class);

            if (response.getBody() != null && (int) response.getBody().get("code") == 0) {
                Map<String, Object> responseData = (Map<String, Object>) response.getBody().get("response");
                String token = (String) responseData.get("access_token");
                log.debug("토큰 발급 성공 - 토큰: {}", token);
                return token;
            }

            log.error("토큰 발급 실패 - 응답 코드: {}, 메시지: {}",
                      response.getBody().get("code"), response.getBody().get("message"));
            throw new PaymentException(ErrorCode.IAMPORT_TOKEN_ISSUANCE_FAILED);

        } catch (Exception e) {
            log.error("토큰 발급 중 오류 발생: {}", e.getMessage(), e);
            throw new PaymentException(ErrorCode.IAMPORT_TOKEN_ISSUANCE_FAILED, e);
        }
    }
    
    
    
    

    public IamportResponseDTO paymentByImpUid(String imp_uid) {
        log.debug("아임포트 결제 정보 조회 요청 - imp_uid: {}", imp_uid);

        try {
            String url = API_URL + "/payments/" + imp_uid;
            String token = getAccessToken();

            log.debug("요청 URL: {}", url);
            log.debug("Bearer 토큰: {}", token);
            
            // HTTP 헤더 설정 (Bearer 인증 방식 사용)
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(token);
            HttpEntity<String> entity = new HttpEntity<>(headers);
            
            // 아임포트 API에 GET 요청을 보내고 응답 받기
            ResponseEntity<IamportApiResult> response =
                    restTemplate.exchange(url, HttpMethod.GET, entity, IamportApiResult.class);

            log.debug("HTTP 상태 코드: {}", response.getStatusCode());
            log.debug("응답 본문: {}", response.getBody());
            
            // 응답이 성공적이고 본문이 존재하는 경우 2xx -> 200번대 status 
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                IamportResponseDTO responseBody = response.getBody().getResponse();
                // 결제 상태가 'paid'인 경우 결제 정보 반환
                if ("paid".equals(responseBody.getStatus())) {
                    return responseBody;
                } else {
                	// 결제 상태가 'paid'가 아닌 경우 오류 로그 및 예외 발생
                    log.error("아임포트 결제 조회 실패 - 응답 코드: {}, 메시지: {}",
                              response.getBody().getCode(), response.getBody().getMessage());
                    throw new PaymentException(ErrorCode.IAMPORT_PAYMENT_INQUIRY_FAILED, 
                                               response.getBody().getMessage());
                }
            } else {
                log.error("아임포트 결제 조회 실패 - HTTP 상태 코드: {}", response.getStatusCode());
                throw new PaymentException(ErrorCode.IAMPORT_PAYMENT_INQUIRY_FAILED, 
                                           "HTTP 상태 코드: " + response.getStatusCode());
            }

        } catch (RestClientException e) {
        	// 네트워크 오류 발생 시 로그 및 예외 발생
            log.error("결제 조회 중 네트워크 오류 발생: {}", e.getMessage(), e);
            throw new PaymentException(ErrorCode.IAMPORT_PAYMENT_INQUIRY_FAILED, e);
        } catch (Exception e) {
        	// 기타 오류 발생 시 로그 및 예외 발생
            log.error("결제 조회 중 오류 발생: {}", e.getMessage(), e);
            throw new PaymentException(ErrorCode.IAMPORT_PAYMENT_INQUIRY_FAILED, e);
        }
    }
    
    
    
    
    
    
    public long getPaymentAmount(String imp_uid) {
        return paymentByImpUid(imp_uid).getAmount();
    }

//    public void cancelPayment(String imp_uid, String merchant_uid, String reason) {
//        log.debug("아임포트 결제 취소 요청 - imp_uid: {}, merchant_uid: {}, 사유: {}", imp_uid, merchant_uid, reason);
//
//        if (imp_uid == null || imp_uid.isEmpty()) {
//            log.error("결제 취소 실패 - imp_uid가 비어 있습니다.");
//            throw new PaymentException("imp_uid가 비어 있습니다.");
//        }
//
//        if (merchant_uid == null || merchant_uid.isEmpty()) {
//            log.error("결제 취소 실패 - merchant_uid가 비어 있습니다.");
//            throw new PaymentException("merchant_uid가 비어 있습니다.");
//        }
//
//        try {
//            String url = API_URL + "/payments/cancel";
//            String token = getAccessToken();
//
//            HttpHeaders headers = new HttpHeaders();
//            headers.setBearerAuth(token);
//            headers.setContentType(MediaType.APPLICATION_JSON);
//
//            Map<String, String> requestBody = new HashMap<>();
//            requestBody.put("imp_uid", imp_uid);
//            requestBody.put("merchant_uid", merchant_uid);
//            requestBody.put("reason", reason);
//
//            HttpEntity<Map<String, String>> entity = new HttpEntity<>(requestBody, headers);
//
//            log.error("++++++++++++++++");
//            
//            log.debug("결제 취소 요청 데이터: {}", entity.toString());
//
//            //ResponseEntity<Map> response = restTemplate.postForEntity(url, entity, Map.class);
//            
//            restTemplate.postForEntity(url, entity, Map.class);
//            
//            
//            log.debug("결제 취소 요청 데이터: {}", url);
//            log.debug("결제 취소 요청 데이터: {}", Map.class);
//            log.debug(token);
////            if (response.getBody() != null) {
////                int responseCode = (int) response.getBody().get("code");
////                String responseMessage = (String) response.getBody().get("message");
////                
////
////                if (responseCode == 0) {
////                    log.debug("결제 취소 성공");
////                } else {
////                	log.debug("xxxxxxxxxxx" + imp_uid);
////                    log.debug("xxxxxxxxxxx" + merchant_uid);
////                    log.error("결제 취소 실패 - 응답 코드: {}, 메시지: {}", responseCode, responseMessage);
////                    throw new PaymentException(ErrorCode.IAMPORT_PAYMENT_CANCELLATION_FAILED, responseMessage);
////                }
////            } else {
////                log.error("결제 취소 실패 - 응답 본문이 비어 있습니다.");
////                throw new PaymentException(ErrorCode.IAMPORT_PAYMENT_CANCELLATION_FAILED, "응답 본문이 비어 있습니다.");
////            }
//
//        } catch (Exception e) {
//            log.error("결제 취소 중 오류 발생: {}", e.getMessage(), e);
//            throw new PaymentException(ErrorCode.IAMPORT_PAYMENT_CANCELLATION_FAILED, e);
//        }
//    }
    
//    public void cancelPayment(String imp_uid, String merchant_uid, String reason) {
//        log.debug("아임포트 결제 취소 요청 시작 - imp_uid: {}, merchant_uid: {}, 사유: {}", imp_uid, merchant_uid, reason);
//
//        validateUids(imp_uid, merchant_uid);
//
//        try {
//            String url = API_URL + "/payments/cancel";
//            String token = getAccessToken();
//
//            log.debug("토큰 발급 완료 - 토큰: {}", token);
//
//            HttpHeaders headers = new HttpHeaders();
//            headers.setBearerAuth(token);
//            headers.setContentType(MediaType.APPLICATION_JSON);
//
//            Map<String, String> requestBody = new HashMap<>();
//            requestBody.put("imp_uid", imp_uid);
//            requestBody.put("merchant_uid", merchant_uid);
//            requestBody.put("reason", reason);
//
//            // ObjectMapper를 사용하여 JSON 직렬화
//            ObjectMapper objectMapper = new ObjectMapper();
//            String jsonRequestBody = objectMapper.writeValueAsString(requestBody);
//
//            log.debug("JSON 직렬화된 요청 데이터: {}", jsonRequestBody);
//
//            HttpEntity<String> entity = new HttpEntity<>(jsonRequestBody, headers);
//
//            log.debug("HttpEntity 데이터: {}", entity.getBody());
//
//            ResponseEntity<IamportApiResult> response = restTemplate.postForEntity(url, entity, IamportApiResult.class);
//
//            log.debug("API 응답 수신 - 상태 코드: {}, 응답 본문: {}", response.getStatusCode(), response.getBody());
//
//            handleApiResponse(response);
//
//        } catch (PaymentException e) {
//            log.error("결제 취소 실패: {}", e.getMessage());
//            throw e;
//        } catch (Exception e) {
//            log.error("결제 취소 중 예기치 않은 오류 발생: {}", e.getMessage(), e);
//            throw new PaymentException(ErrorCode.IAMPORT_PAYMENT_CANCELLATION_FAILED, e);
//        }
//    }
//
//    private void validateUids(String imp_uid, String merchant_uid) {
//        if (imp_uid == null || imp_uid.isEmpty()) {
//            log.error("결제 취소 실패 - imp_uid가 비어 있습니다.");
//            throw new PaymentException(ErrorCode.PAYMENT_CANCELLATION_FAILED, "imp_uid가 비어 있습니다.");
//        }
//        if (merchant_uid == null || merchant_uid.isEmpty()) {
//            log.error("결제 취소 실패 - merchant_uid가 비어 있습니다.");
//            throw new PaymentException(ErrorCode.PAYMENT_CANCELLATION_FAILED, "merchant_uid가 비어 있습니다.");
//        }
//    }
    
    
 // ... existing code ...
    public IamportApiResult cancelPayment(String imp_uid, String merchant_uid, String reason) {
        log.debug("아임포트 결제 취소 요청 시작 - imp_uid: {}, merchant_uid: {}, 사유: {}", imp_uid, merchant_uid, reason);

        validateUids(imp_uid, merchant_uid);

        try {
            String url = API_URL + "/payments/cancel";
            String token = getAccessToken();

            log.debug("토큰 발급 완료 - 토큰: {}", token);

            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(token);
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, String> requestBody = new HashMap<>();
            requestBody.put("imp_uid", imp_uid);
            requestBody.put("merchant_uid", merchant_uid);
            requestBody.put("reason", reason);

            ObjectMapper objectMapper = new ObjectMapper();
            String jsonRequestBody = objectMapper.writeValueAsString(requestBody);

            log.debug("JSON 직렬화된 요청 데이터: {}", jsonRequestBody);

            HttpEntity<String> entity = new HttpEntity<>(jsonRequestBody, headers);
            ResponseEntity<IamportApiResult> response = restTemplate.postForEntity(url, entity, IamportApiResult.class);

            log.debug("API 응답 수신 - 상태 코드: {}, 응답 본문: {}", response.getStatusCode(), response.getBody());

            IamportApiResult apiResult = response.getBody();
            if (apiResult == null) {
                log.error("결제 취소 실패 - 응답 본문이 비어 있습니다.");
                throw new PaymentException(ErrorCode.IAMPORT_PAYMENT_CANCELLATION_FAILED, "응답 본문이 비어 있습니다.");
            }

            if (apiResult.getCode() != 0) {
                log.error("결제 취소 실패 - 응답 코드: {}, 메시지: {}", apiResult.getCode(), apiResult.getMessage());
                throw new PaymentException(ErrorCode.IAMPORT_PAYMENT_CANCELLATION_FAILED, apiResult.getMessage());
            }

            log.debug("결제 취소 성공");
            return apiResult;

        } catch (PaymentException e) {
            log.error("결제 취소 실패: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("결제 취소 중 예기치 않은 오류 발생: {}", e.getMessage(), e);
            throw new PaymentException(ErrorCode.IAMPORT_PAYMENT_CANCELLATION_FAILED, e);
        }
    }

    private void validateUids(String imp_uid, String merchant_uid) {
        if (imp_uid == null || imp_uid.isEmpty()) {
            log.error("결제 취소 실패 - imp_uid가 비어 있습니다.");
            throw new PaymentException(ErrorCode.PAYMENT_CANCELLATION_FAILED, "imp_uid가 비어 있습니다.");
        }
        if (merchant_uid == null || merchant_uid.isEmpty()) {
            log.error("결제 취소 실패 - merchant_uid가 비어 있습니다.");
            throw new PaymentException(ErrorCode.PAYMENT_CANCELLATION_FAILED, "merchant_uid가 비어 있습니다.");
        }
    }
    // ... existing code ...
    
    
    
    
    
    
    

    private void handleApiResponse(ResponseEntity<IamportApiResult> response) {
        IamportApiResult apiResult = response.getBody();
        if (apiResult != null) {
            if (apiResult.getCode() == 0) {
                log.debug("결제 취소 성공");
            } else {
                log.error("결제 취소 실패 - 응답 코드: {}, 메시지: {}", apiResult.getCode(), apiResult.getMessage());
                throw new PaymentException(ErrorCode.IAMPORT_PAYMENT_CANCELLATION_FAILED, apiResult.getMessage());
            }
        } else {
            log.error("결제 취소 실패 - 응답 본문이 비어 있습니다.");
            throw new PaymentException(ErrorCode.IAMPORT_PAYMENT_CANCELLATION_FAILED, "응답 본문이 비어 있습니다.");
        }
    }
    
    
    
    
    
    
    
    
}