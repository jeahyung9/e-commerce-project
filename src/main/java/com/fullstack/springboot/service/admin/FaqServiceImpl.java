package com.fullstack.springboot.service.admin;

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
import com.fullstack.springboot.dto.admin.FaqDTO;
import com.fullstack.springboot.entity.admin.Admin;
import com.fullstack.springboot.entity.admin.Faq;
import com.fullstack.springboot.repository.admin.AdminRepository;
import com.fullstack.springboot.repository.admin.FaqRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
@RequiredArgsConstructor
public class FaqServiceImpl implements FaqService {

	private final ModelMapper modelMapper;
	
	private final FaqRepository faqRepository;
	
	private final AdminRepository adminRepository;
	
	@Override
	public Long registerFAQ(FaqDTO faqDTO) {
		Faq faq = dtoToEntity(faqDTO);
		faqRepository.save(faq);
		return faq.getFno();
	}

	@Override
	public FaqDTO readFAQ(Long fno) {
		Faq faq = faqRepository.findById(fno).get();
		
		Admin admin = adminRepository.findById(faq.getAdmin().getAdno()).get();
		
		return entityToDTO(faq, admin);
	}

	@Override
	@Transactional
	public void deleteFAQ(Long fno) {
		if(!faqRepository.existsById(fno)) {
			throw new RuntimeException("해당 질문을 찾을수 없음");
		}
		
		faqRepository.deleteById(fno);
	}

	@Override
	@Transactional
	public void modifyFAQ(FaqDTO faqDTO) {
		Faq faq = faqRepository.findById(faqDTO.getFno())
				.orElseThrow(()->new RuntimeException("해당 번호의 질문을 찾을 수 없음 번호 : " + faqDTO.getFno()));

		faq = dtoToEntity(faqDTO);
		
		faqRepository.save(faq);
	}

	@Override
	public PageResponseDTO<FaqDTO> faqList(PageRequestDTO pageRequestDTO, String sort, Long adno, boolean order) {

		log.error("service");
		
		Order sortOrder =  null;
		
		if(order) {
			sortOrder = Order.asc(sort);
		}else {
			sortOrder = Order.desc(sort);
		}
		
		Pageable pageable =
				PageRequest.of(
						pageRequestDTO.getPage()-1,
						pageRequestDTO.getSize(),
						Sort.by(sortOrder).descending());
		
		
		Page<Faq> res = faqRepository.findIsPublic(pageable);
		
		if(adno != null) {
			res = faqRepository.getFaqListByAdmin(adno, pageable);
		}
		
		List<FaqDTO> dtoList = res.getContent().stream()
				.map(faq -> {
					FaqDTO faqDTO = modelMapper.map(faq, FaqDTO.class);
					
					Admin admin = Admin.builder().adno(faq.getAdmin().getAdno()).build();
					
					FaqDTO dto = faqDTO.toBuilder().adno(admin.getAdno()).build();
					
					return dto;
				})
				.collect(Collectors.toList());
		
		long totalCount = res.getTotalElements();
		
		PageResponseDTO<FaqDTO> responseDTO = PageResponseDTO.<FaqDTO>withAll()
					.dtoList(dtoList)
					.pageRequestDTO(pageRequestDTO)
					.totalCount(totalCount)
					.build();
		
		return responseDTO;
	}

}
