package org.edupoll.model.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateUserRequest {
	
	@NotNull
	private String email;
	@NotNull
	private String password;
	@NotNull
	private String name;
	
}
