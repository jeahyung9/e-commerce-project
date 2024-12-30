package com.fullstack.springboot.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.fullstack.springboot.dto.PageRequestDTO;
import com.fullstack.springboot.dto.PageResponseDTO;
import com.fullstack.springboot.dto.admin.NoticeDTO;
import com.fullstack.springboot.entity.admin.Admin;
import com.fullstack.springboot.entity.admin.Notice;
import com.fullstack.springboot.repository.AdminRepository;
import com.fullstack.springboot.repository.NoticeRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
@RequiredArgsConstructor
public class NoticeServiceImpl implements NoticeService {
	
	private final ModelMapper modelMapper;
	
	@Autowired
	NoticeRepository noticeRepository;
	
	@Autowired
	AdminRepository adminRepository;
	
	@Override
	public Long registerNotice(NoticeDTO noticeDTO) {
		Notice notice = dtoToEntity(noticeDTO);
		noticeRepository.save(notice);
		return notice.getNno();
	}

	@Override
	public NoticeDTO readNotice(Long nno) {
		Object[] result = noticeRepository.getNoticeListByAdmin(nno);
        Notice notice = (Notice) result[0];
        Admin admin = (Admin) result[1];

        return entityToDTO(notice, admin);
	}

	@Override
	public void deleteNotice(Long nno) {
		if(!noticeRepository.existsById(nno)) {
			throw new RuntimeException("공지 사항을 찾을 수 없음");
		}
		
	}

	@Override
	public void modifyNotice(NoticeDTO noticeDTO) {
		Notice notice = noticeRepository.findById(noticeDTO.getNno())
				.orElseThrow(()-> new RuntimeException("해당 번호의 공지 사항을 찾을 수 없음 번호 : " + noticeDTO.getNno()));
		
		notice = dtoToEntity(noticeDTO);
		
		noticeRepository.save(notice);
	}

	@Override
	public PageResponseDTO<NoticeDTO> noticeList(PageRequestDTO pageRequestDTO) {
		Pageable pageable = 
				PageRequest.of(
						pageRequestDTO.getPage() -1, 
						pageRequestDTO.getSize(), 
						Sort.by("nno").descending());
		
		Page<Notice> result = noticeRepository.findAll(pageable);
		
		List<NoticeDTO> dtoList = result.getContent().stream()
				.map(notice -> modelMapper.map(notice, NoticeDTO.class))
				.collect(Collectors.toList());
		
		long totalCount = result.getTotalElements();
		
		PageResponseDTO<NoticeDTO> responseDTO = PageResponseDTO.<NoticeDTO>withAll()
				.dtoList(dtoList)
				.pageRequestDTO(pageRequestDTO)
				.totalCount(totalCount)
				.build();
		
		return responseDTO;
	}
	
	

}
