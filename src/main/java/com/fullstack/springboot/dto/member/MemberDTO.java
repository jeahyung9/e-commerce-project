package com.fullstack.springboot.dto.member;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fullstack.springboot.entity.member.Member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberDTO {
//public class MemberDTO extends User {
	   
	   private Long mno;
	   private String m_pw;
	   private String m_name;
	   private String m_nickname;
	   private String m_phoNum;
	   @JsonProperty("email")
	   private String m_email;
	   private String def_addr;
	   
	   @JsonFormat(pattern = "yyyy-MM-dd")  // 날짜 형식 지정
	   private LocalDate birth;
	   
	   private Boolean isMan;
	   private boolean ad_agree;
	   private boolean info_agree;
	   @JsonProperty("isBan")
	   private boolean isBan;
	   private Long totalPay = 0L;
	   private Set<Membership> membership = new HashSet<>();
	   private boolean formSns;
	   
	   public MemberDTO(Member member) {
	        this.mno = member.getMno();
	        this.m_email = member.getM_email();
	        this.m_name = member.getM_name();
	        this.m_pw = member.getM_pw();
	        this.m_nickname = member.getM_nickName();
	        this.m_phoNum = member.getM_phoNum();
	        this.def_addr = member.getDef_addr();
	        this.birth = member.getBirth();
	        this.ad_agree = member.isAd_agree();
	        this.info_agree = member.isInfo_agree();
	        this.totalPay = member.getTotalPay();
	        this.membership = member.getMembership();  // List<Membership>에 대한 매핑이 필요할 경우
	        this.isBan = member.isBan();
	        this.isMan = member.getIsMan();
	        this.formSns = member.isFormSns();
	    }

	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return null;
	}
	
   public Map<String, Object> getClaims() {
      Map<String, Object> dataMap = new HashMap<>();
      
      dataMap.put("mno", mno);
        dataMap.put("m_pw", m_pw);
        dataMap.put("m_name", m_name);
        dataMap.put("m_nickname", m_nickname);
        dataMap.put("m_phoNum", m_phoNum);
        dataMap.put("m_email", m_email);
        dataMap.put("def_addr", def_addr);
        dataMap.put("birth", birth);
        dataMap.put("isMan", isMan);
        dataMap.put("ad_agree", ad_agree);
        dataMap.put("info_agree", info_agree);
        dataMap.put("isBan", isBan);
        dataMap.put("totalPay", totalPay);
        dataMap.put("membership", membership);
        
        return dataMap;
   }
   
	   
}