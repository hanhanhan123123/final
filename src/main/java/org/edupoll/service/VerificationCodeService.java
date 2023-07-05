package org.edupoll.service;


import java.util.Date;

import org.edupoll.model.dto.request.VerifyEmailRequest;
import org.edupoll.model.entity.VerificationCode;
import org.edupoll.repository.VerificationCodeRepository;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VerificationCodeService {

	private final JavaMailSender mailsender;
	private final VerificationCodeRepository verificationCodeRepository;
	
	
	@Transactional
	public void sendVerifactionCode(VerifyEmailRequest req) {
		// 보안코드 생성
		int secretNum = (int)(Math.random()*1_000_000);
		String code = String.format("%06d", secretNum);
		
		// 메일 생성 및 전송
		SimpleMailMessage message = new SimpleMailMessage();
		
		message.setTo(req.getEmail());
		message.setFrom("no-reply@gmail.com");
		message.setSubject("[파이날앱] 인증코드를 보내드립니다.");
		message.setText("""
					이메일 본인 인증 절차에 따라 인증코드를 보내드립니다.
					
					인증코드 :#{code}
				""".replace("#{code}", code));
		mailsender.send(message);

		// 리포지토리에 코드값 저장
		var one = new VerificationCode();
			one.setCode(code);
			one.setEmail(req.getEmail());
			one.setCreated(new Date());
		verificationCodeRepository.save(one);
	}
}



