package com.fullstack.springboot.entity.admin;

import com.fullstack.springboot.entity.BaseEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Builder
@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "notice_board")
public class Notice extends BaseEntity{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long nno;
	
	private String title;
	
	private String content;
	
	private String imgPath; // 이미지 경로
	
	private boolean isPublic; // 공개 비공개
	
	@ManyToOne
	private Admin admin;
}
