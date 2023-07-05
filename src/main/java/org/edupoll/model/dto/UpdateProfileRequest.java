package org.edupoll.model.dto;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;
/*
 * 서블릿으로 처맇라때랑은 다르게 Spring boot에서는 
 * Multipart 요청도 처리할 수 있게 기본 설정이 되있다.
 * File은 타입을 MultipartFile
 * Text는 알아서 자료형
 */

@Data
public class UpdateProfileRequest {

	private String name;
	private MultipartFile profile;
	
	//같은 이름으로 여러개를 넘긴다면 배열을 쓴다거나private MultipartFile[] images;
	//혹은 프라이빗의 리스트 멀티파일 private List<MultipartFile> images;
}
