package com.fullstack.springboot.service.admin;

import java.util.List;

import com.fullstack.springboot.dto.PageRequestDTO;
import com.fullstack.springboot.dto.PageResponseDTO;
import com.fullstack.springboot.dto.admin.NoticeDTO;
import com.fullstack.springboot.entity.admin.Admin;
import com.fullstack.springboot.entity.admin.Notice;

public interface NoticeService {
	
	Long registerNotice(NoticeDTO noticeDTO);
	
	NoticeDTO readNotice(Long adno);
	
	void deleteNotice(Long nno);
	
	void modifyNotice(NoticeDTO noticeDTO); 
	
	PageResponseDTO<NoticeDTO> noticeList(PageRequestDTO pageRequestDTO, String sort, Long adno, boolean order); //페이징

	List<NoticeDTO> noticeListAll();
	
	default Notice dtoToEntity(NoticeDTO dto) {
		Admin admin = Admin.builder().adno(dto.getAdno()).build();
		
		Notice notice = Notice.builder()
					.nno(dto.getNno())
					.title(dto.getTitle())
					.content(dto.getContent())
					.imgPath(dto.getImgPath())
					.isPrivate(dto.isPrivate())
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
					.isPrivate(notice.isPrivate())
					.adno(admin.getAdno())
					.build();
		return noticeDto;
	}
}
