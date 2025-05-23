package com.fullstack.springboot.entity.member;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import com.fullstack.springboot.dto.member.Membership;
import com.fullstack.springboot.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "member")
public class Member extends BaseEntity{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long mno; 

	@Column(nullable = false)
	private String m_pw;
	
	@Column(nullable = false)
	private String m_name;
	
	@Column(nullable = false)
	private String m_nickName;

	private LocalDate birth;// 생일
	
	private String m_phoNum;
	
	@Column(nullable = false, unique = true)
	private String m_email;
	
	private String def_addr; //주소(기본 배송지)
	
	private Boolean isMan; // 성별 true 면 남자, false 면 여자
	
	private boolean ad_agree; // 약관-광고 동의
	
	private boolean info_agree; // 약관-정보제공 동의
	
	@Builder.Default
	private boolean isBan = false;
	
	@Builder.Default
	private Long totalPay = 0L;
	
	private boolean formSns;
	
	//권한 값 컬렉션 선언. Default 로 선언해서 값이 자동으로 대입되도록 합니다.
	@ElementCollection(fetch = FetchType.LAZY)
	@Builder.Default
	private Set<Membership> membership = new HashSet<Membership>();
	
	//권한을 설정하는 메서드 정의
	public void addMembershipSet(Membership userMembership) {
		membership.add(userMembership);
	}
	
	public void banUser() {
        this.isBan = true;
    }

    public void unbanUser() {
        this.isBan = false;
    }
    
    public void setPw(String newPw) {
    	this.m_pw = newPw;
    }

	public void setM_name(String name) {
		this.m_name = name;
		
	}
	public void setM_phoNum(String phoNum) {
		this.m_phoNum = phoNum;
	}
	public void setM_def_addr(String def_addr) {
		this.def_addr = def_addr;
	}
	public void setM_nickName(String nickName) {
		this.m_nickName = nickName;
	}
	
}
