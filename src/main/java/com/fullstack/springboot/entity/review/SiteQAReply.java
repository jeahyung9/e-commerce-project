package com.fullstack.springboot.entity.review;

import com.fullstack.springboot.entity.BaseEntity;
import com.fullstack.springboot.entity.admin.Admin;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name="site_qa_reply")
public class SiteQAReply extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long sqrno;
	
	private String content;
	
	@OneToOne
	private SiteQA siteQa;
	
	@ManyToOne
	private Admin admin;
}
