package com.fullstack.springboot.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;

/*
 * BaseEntity Bean : DB 에 C, Insert, Update 할 때마다 등록 시간이나, 수정시간등을 쿼리로 직접 작성해야하는 불편함을 해소시키는 Entity
 * 
 * 이 클래스 Entity 는 자동으로 위의 값을 DB 에 등록합니다. => 관리하기 편함
 * 
 * 즉 엔티티가 변경되면, 변경된 시간값을 자동으로 저장. 이 값이 DB 에 쓰이게 됨.
 * 사용법은 어노테이션으로 설정.
 */

//BaseEntity 로 만들기 시작
@MappedSuperclass
//아래의 리스너를 활성화 하기 위해서는 반드시 main 클래스에 @EnableJpaAuditing 을 반드시 선언해야함

@EntityListeners(value = {AuditingEntityListener.class})
//여기까지가 BaseEntity 로 선언하는 부분
@Getter

//이 클래스는 모든 하위 Entity 의 Insert 시간과 Update 되는 시간값을 자동 관리하는 엔티티로 선언함.
//때문에, 모든 엔티티가 위 시간값을 자동 관리 되려면, 이 클래스를 상속 받으면 됨.
//그리고, 신규/수정 시간은 이 클래스가 관리 하기때문에 필드를 여기서 선언 함. 그리고 getter 를 만들어서 하위에서 필요시에 사용할수 있도록 함.
public abstract class BaseEntity {

	//아래의 객체를 자동 주입하도록 날짜객체 @ 사용
	@CreatedDate
	@Column(name = "regdate", updatable = false)
	private LocalDateTime regDate;//신규 방명록 글의 등록시간.
	
	@CreatedDate
	@LastModifiedDate
	@Column(name = "moddate", updatable = true)
	private LocalDateTime modDate;//수정된 글의 시간..
}