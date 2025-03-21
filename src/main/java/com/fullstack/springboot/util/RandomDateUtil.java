package com.fullstack.springboot.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.ThreadLocalRandom;

public class RandomDateUtil {
	public static LocalDate generateRandomDate(LocalDate startDay, LocalDate endDay) {
		
		//시작일과 종료일의 차
		long betweenDate = ChronoUnit.DAYS.between(startDay, endDay);
		
		//중간날짜 중 랜덤값
		long randomDate = ThreadLocalRandom.current().nextLong(betweenDate);
		
		//시작일에 랜덤날짜를 더해 사이값을 만듦
		LocalDate randomDay = startDay.plusDays(randomDate);
		
		return randomDay; 
	}
	
	public static LocalDateTime generateRandomDateTime(LocalDateTime startDay, LocalDateTime endDay) {
		//시작일과 종료일을 MilliSecond 로 변환
		long startDateMilli = startDay.toInstant(ZoneOffset.UTC).toEpochMilli();
		long endDateMilli = endDay.toInstant(ZoneOffset.UTC).toEpochMilli();
		
		//MilliSecond 로 변환한 시작일 및 종료일 사이에 랜덤한 값
		long randomDateMilli = ThreadLocalRandom.current().nextLong(startDateMilli, endDateMilli);
		
		//랜덤한 MilliSecond 값을 LocalDateTime 값으로 변환
		LocalDateTime randomDate = LocalDateTime.ofInstant(java.time.Instant.ofEpochMilli(randomDateMilli), ZoneOffset.UTC);
		
		return randomDate;
	}
}
