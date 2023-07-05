package org.edupoll.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data 
@AllArgsConstructor
public class ValidateUserResponse {
	//인증성공시 보내주는 응답객체
	//소셜로 보내고 보내고, 아이디비번으로 가입해도 보냄...

	private int code;
	private	String token;
	private String userEmail;
	
}
