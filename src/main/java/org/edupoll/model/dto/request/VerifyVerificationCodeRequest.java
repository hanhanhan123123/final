package org.edupoll.model.dto.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter

//인증코드를 검증하는 API 엔드포인트 - Request DTO:
public class VerifyVerificationCodeRequest {

	private String code;
	private String email;
 }
