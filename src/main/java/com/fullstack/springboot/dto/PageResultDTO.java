package com.fullstack.springboot.dto;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import lombok.Data;

/*
 * 요청된 페이지의 결과를 되돌려 주는 DTO
 * 이 클래스에서 할 일은, Page<Entity> 객체들을 DTO 로 변화해서 Collection 타입으로 리턴해주고
 * 화면 출력에 필요한 페이징 처리된 페이지 index 값도 되돌려 주도록 함
 * 
 * 이 클래스에서는 Paging  처리도 같이해서 보내야함. 1, 2, 3, 4, 5, 다음, 이전, 11, 12, 13, 다음
 * 
 * 당연히 페이징 처리 연산을 해야함
 * 
 * 다음은 연산 처리시 기준이 되는 내용
 * 
 * 시작 페이지 번호, 끝 페이지 번호(이건 실제 갯수와 페이지를 연산한 페이지여야함) 이전/다음, 현재페이지)
 * 
 * 연산법
 * 
 * 1. 가장 마지막 페이지 부터 연산 ex> tempEnd = (int)(Math.ceil(페이지번호 / 10.0) * 10);
 * 
 * 예를 들면 1페이지 같은 경우 10, 10페이지도 10, 11페이지 인 경우 20 리턴
 * 
 * 이렇게 하면 요청된 페이지에 따른 그룹에 마지막 페이지 번호가 산출됨
 * 
 * 이 때 주의할 점은 마지막 페이지가 항상 글 목록수와 비례해서 떨어지지 않음
 * ex>마지막 페이지가 33페이지 일 경우 
 * 33 / 10.0 * 10 = 40 즉 마지막 페이지가 40페이지 이나 이런 경우 33페이지 부터는 공갈 페이지가 됨
 *
 * Page 클래스에는 getTotalPage() 가 있음
 * 이 메서드를 이용해서 위 연산한 결과와 비교를 함
 * 
 * totalPage = pageRes.getTotalPage();
 * end = totalPage > (계산한 마지막 페이지값) ? (계산한 결과의 마지막) : totalPage 값 매핑
 * 
 * start page 검증식 : 마지막 페이지에서 -9를 하면 시작 페이지
 */
@Data
public class PageResultDTO<DTO,En> {
	
	//리턴할 항목을 담는 List 선언
	private List<DTO> dtoList;
	
	//총 페이지 번호
	private int totalPage;
	
	//현재 페이지 번호 
	private int page;
	
	//목록의 size
	private int size;
	
	//시작, 끝 페이지 번호
	private int start, end;
	
	//이건 Page 객체를 통해 이전 이후가 있는지를 알 수 있음
	private boolean prev, next;
	
	//페이지 번호 목록을 담은 list
	private List<Integer> pageList;
	
	public PageResultDTO(Page<En> result, Function<En, DTO> fn) {
		
		//Page<En> 에는 Entity 의 묶음이 들어가 있고 그걸 result 에 담음(select 등을 해온 결과)
		//이걸 추출하기 위해서는 루프나 스트림을 통해 얻어내야함
		//dto 리스트를 파라미터로 받은 result 에서 스트림을 통해 생성
		dtoList = result.stream().map(fn).collect(Collectors.toList());
		
		//totalPage 를 얻어냄 => result(Page 객체) 를 통해 얻어낼수 있음
		totalPage = result.getTotalPages();
		
		//페이징 처리를 수행하는 메서드 호출하여 결과값 get
		makePageList(result.getPageable());
	}
	
	private void makePageList(Pageable pageable) {
		this.page = pageable.getPageNumber() +1;
		this.size = pageable.getPageSize();
		
		int tempEnd = (int)(Math.ceil(page / 10.0) * 10);
		start = tempEnd - 9;
		
		prev = start > 1;
		
		//실제 마지막 페이지 번호 get
		end = totalPage > tempEnd ? tempEnd : totalPage;
		
		//다음은 페이지 블락 단위로 결정함 10페이지씩 이동
		next = totalPage > tempEnd;
		
		pageList = IntStream.rangeClosed(start, end).boxed().collect(Collectors.toList());
	}
}
