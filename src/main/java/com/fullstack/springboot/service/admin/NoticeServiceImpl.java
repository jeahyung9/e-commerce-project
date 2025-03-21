package com.fullstack.springboot.service.admin;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;

import com.fullstack.springboot.dto.PageRequestDTO;
import com.fullstack.springboot.dto.PageResponseDTO;
import com.fullstack.springboot.dto.admin.NoticeDTO;
import com.fullstack.springboot.entity.admin.Admin;
import com.fullstack.springboot.entity.admin.Notice;
import com.fullstack.springboot.repository.admin.AdminRepository;
import com.fullstack.springboot.repository.admin.NoticeRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
@RequiredArgsConstructor
public class NoticeServiceImpl implements NoticeService {
	
	private final ModelMapper modelMapper;

	private final NoticeRepository noticeRepository;
	
	private final AdminRepository adminRepository;
	
	@Override
	@Transactional
	public Long registerNotice(NoticeDTO noticeDTO) {
		
		if(!adminRepository.existsById(noticeDTO.getAdno())) {
			throw new RuntimeException("해당 관리자를 찾을 수 없음");
		}
		
		Notice notice = dtoToEntity(noticeDTO);
	
		noticeRepository.save(notice);
		return notice.getNno();

	}

	@Override
	public NoticeDTO readNotice(Long nno) {
		
        Notice notice = noticeRepository.findById(nno).get();

        Admin admin = adminRepository.findById(notice.getAdmin().getAdno()).get();
        
        return entityToDTO(notice, admin);
	}

	@Override
	@Transactional
	public void deleteNotice(Long nno) {
		if(!noticeRepository.existsById(nno)) {
			throw new RuntimeException("공지 사항을 찾을 수 없음");
		}
		
		noticeRepository.updateToPrivate(nno, true);
	}

	@Override
	@Transactional
	public void modifyNotice(NoticeDTO noticeDTO) {
		Notice notice = noticeRepository.findById(noticeDTO.getNno())
				.orElseThrow(()-> new RuntimeException("해당 번호의 공지 사항을 찾을 수 없음 번호 : " + noticeDTO.getNno()));
		
		notice = dtoToEntity(noticeDTO);
		
		noticeRepository.save(notice);
	}

	@Override
	public PageResponseDTO<NoticeDTO> noticeList(PageRequestDTO pageRequestDTO, String sort, Long adno, boolean order) {
		
		log.error("service");
		
		Order sortOrder = null;
		
		if (order) {
			sortOrder = Order.asc(sort);
		} else {
			sortOrder = Order.desc(sort);
		}
		
		
		Pageable pageable = 
				PageRequest.of(
						pageRequestDTO.getPage() -1, 
						pageRequestDTO.getSize(), 
						Sort.by(sortOrder).descending());
		
		Page<Notice> result = noticeRepository.findIsPublic(pageable);
		
		if(adno != null) {
			result = noticeRepository.getNoticeListByAdmin(adno, pageable);
		}
		
		List<NoticeDTO> dtoList = result.getContent().stream()
				.map(notice -> {
					NoticeDTO noticeDTO = modelMapper.map(notice, NoticeDTO.class);
					
					Admin admin = Admin.builder().adno(notice.getAdmin().getAdno()).build();
					
					NoticeDTO dto = noticeDTO.toBuilder().adno(admin.getAdno()).build();
					
					return dto;
				})
				.collect(Collectors.toList());
		
		long totalCount = result.getTotalElements();
		
		PageResponseDTO<NoticeDTO> responseDTO = PageResponseDTO.<NoticeDTO>withAll()
				.dtoList(dtoList)
				.pageRequestDTO(pageRequestDTO)
				.totalCount(totalCount)
				.build();
		System.out.println("==================" + responseDTO);
		return responseDTO;
	}
	
	@Override
	public List<NoticeDTO> noticeListAll() {
		List<Notice> noticeList = noticeRepository.findAll();
		
		List<NoticeDTO> noticeDTOList = new ArrayList<NoticeDTO>();
		
		for(Notice notice : noticeList) {
			Admin admin = adminRepository.findById(notice.getAdmin().getAdno()).get();
			noticeDTOList.add(entityToDTO(notice, admin));
		}
		
		return noticeDTOList;
	}
	
	

}
