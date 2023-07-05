package org.edupoll.model.dto.request;

import jakarta.validation.constraints.Email;
import lombok.Data;

//인증코드 발급 요청을 받기 위한 Request DTO (email 포함)
@Data
public class VerifyEmailRequest {
	@Email //email양식인지 아닌지 체크해줌
	private String email;
	

}
