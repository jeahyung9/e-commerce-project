package com.fullstack.springboot.dto.qa;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProQAReplyDTO {
    
    private Long qrno;
    private Long qno;
    private Long sno;
    private String qr_content;
    private LocalDateTime regDate;

}
