package com.fullstack.springboot.service.qa;

import com.fullstack.springboot.dto.qa.ProQAReplyDTO;

public interface ProductQAReplyService {

	public void register(ProQAReplyDTO proQAReplyDTO);
	
	public void changeQAReply(ProQAReplyDTO proQAReplyDTO);
	
	public void deleteQAReply(Long qrno);
	
}
