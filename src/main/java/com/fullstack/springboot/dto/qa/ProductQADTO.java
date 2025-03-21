package com.fullstack.springboot.dto.qa;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class ProductQADTO {

    private Long qno;
    private String title;
    private String content;
    private boolean replyCheck;
    private boolean secret;
    private LocalDateTime regDate;
    private Long mno;
    private String m_nickName;
    private Long pno;
    private ProQAReplyDTO proQAReply;
    
}
