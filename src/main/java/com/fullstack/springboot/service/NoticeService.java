package com.fullstack.springboot.service;

import com.fullstack.springboot.dto.PageRequestDTO;
import com.fullstack.springboot.dto.PageResponseDTO;
import com.fullstack.springboot.dto.admin.NoticeDTO;
import com.fullstack.springboot.entity.admin.Admin;
import com.fullstack.springboot.entity.admin.Notice;

public interface NoticeService {
	
	Long registerNotice(NoticeDTO noticeDTO);
	
	NoticeDTO readNotice(Long nno);
	
	void deleteNotice(Long nno);
	
	void modifyNotice(NoticeDTO noticeDTO); 
	
	PageResponseDTO<NoticeDTO> noticeList(PageRequestDTO pageRequestDTO); //페이징

	default Notice dtoToEntity(NoticeDTO dto) {
		Admin admin = Admin.builder().adno(dto.getAdno()).build();
		
		Notice notice = Notice.builder()
					.nno(dto.getNno())
					.title(dto.getTitle())
					.content(dto.getContent())
					.imgPath(dto.getImgPath())
					.isPublic(dto.isPublic())
					.admin(admin)
					.build();
		return notice;
	}
	
	default NoticeDTO entityToDTO(Notice notice, Admin admin) {
		NoticeDTO noticeDto = NoticeDTO.builder()
					.nno(notice.getNno())
					.title(notice.getTitle())
					.content(notice.getContent())
					.imgPath(notice.getImgPath())
					.isPublic(notice.isPublic())
					.adno(admin.getAdno())
					.build();
		return noticeDto;
	}
}
