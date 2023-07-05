package org.edupoll.model.dto.request;

import lombok.Data;

@Data 
public class KakaoAuthroizedCallbackRequest {

	private String code;
	private String error;
}
