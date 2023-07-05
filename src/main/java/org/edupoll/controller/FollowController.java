package org.edupoll.controller;


//필요없어진 컨트롤러 삭제해도 됨
import org.edupoll.service.JWTService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
@Slf4j
public class FollowController {
	
	private final JWTService jwtService;
	
	@GetMapping("/{user}/following")
	public ResponseEntity<Void> getFlollowingList(@PathVariable String user
			, @RequestHeader(name="token") String token) {
		if(token == null ) {
			return  new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}
		// 토큰 검증 
		jwtService.verifyToken(token);
		
		
		return new ResponseEntity<>(HttpStatus.OK);
	}
}


