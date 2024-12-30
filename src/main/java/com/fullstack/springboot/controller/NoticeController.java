package com.fullstack.springboot.controller.admin;

import java.util.Map;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fullstack.springboot.dto.PageRequestDTO;
import com.fullstack.springboot.dto.PageResponseDTO;
import com.fullstack.springboot.dto.admin.NoticeDTO;
import com.fullstack.springboot.service.NoticeService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RestController
@RequestMapping("/api/notice")
@RequiredArgsConstructor
@Log4j2
public class NoticeController {
	
	private final NoticeService noticeService;
	
	@GetMapping("/{nno}")
	public NoticeDTO readNotice(@PathVariable(name = "nno") Long nno) {
		
		return noticeService.readNotice(nno);
	}
	
	@GetMapping("/list")
	public PageResponseDTO<NoticeDTO> list(PageRequestDTO pageRequestDTO){
		log.error(pageRequestDTO);
		
		return noticeService.noticeList(pageRequestDTO);
	}
	
	
	@PostMapping("/")
	public Map<String, Long> register(@RequestBody NoticeDTO dto){
		log.error("NoticeDTO : " + dto);
		
		Long nno = noticeService.registerNotice(dto);
		
		return Map.of("NNO", nno);
	}
	
	@PutMapping("/{nno}")
	public Map<String, String> modifyNotice(
			@PathVariable(name="nno") Long nno,
			@RequestBody NoticeDTO noticeDTO){
		
		noticeDTO.setNno(nno);
		
		log.error("Modify : " + noticeDTO);
		
		noticeService.modifyNotice(noticeDTO);
		
		return Map.of("RESULT", "SUCCESS");
	}
	
	@DeleteMapping("/{nno}")
	public Map<String, String> deleteNotice( @PathVariable(name="nno") Long nno ){
		
		log.error("Remove : " + nno);
		
		noticeService.deleteNotice(nno);
		
		return Map.of("RESULT", "SUCCESS");
	}
	
}
