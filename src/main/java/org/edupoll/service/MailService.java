package org.edupoll.service;


import java.util.Date;
import java.util.Optional;

import org.edupoll.exception.AlreadyVerifiedException;
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
public class MailService {

	private final JavaMailSender mailsender;
	/*
	 * public void sendTestSimpleMail(MailTestRequest dto) {
	 * 
	 * SimpleMailMessage message = new SimpleMailMessage();
	 * 
	 * message.setFrom("edupoll@gmail.com"); message.setTo(dto.getEmail());
	 * message.setSubject("여긴 제목테스트");
	 * message.setText("메일 테스트중입니다.\n불편을 드려 죄송합니다.");
	 * 
	 * 
	 * mailsender.send(message);
	 * 
	 * } public void sendTestHtmlMail(MailTestRequest dto) throws MessagingException
	 * { //메일에 사진첨부
	 * 
	 * MimeMessage message = mailsender.createMimeMessage(); MimeMessageHelper
	 * helper = new MimeMessageHelper(message); //html설정
	 * 
	 * String uuid = UUID.randomUUID().toString();
	 * 
	 * Random random = new Random(); int randNum = random.nextInt(1_000_000); String
	 * code = String.format("%06d", randNum);
	 * 
	 * 
	 * //텍스트블락 엔터값을 포함해서 긴문자열을 만들 수 있는 자바13버전 String htmlTxt = """ <div> <h1>메일
	 * 테스트중</h1> <p style="color:orange"> HTML 메세지도 <b>전송</b> 가능하다. </p> <p> 쿠폰번호 :
	 * <i>#uuid</i> </p> </div>
	 * 
	 * """.replace("#uuid",uuid);
	 * 
	 * helper.setTo(dto.getEmail()); //helper.setFrom("edopoll@gmail.com"); 써도 되고
	 * 안써도 안터짐 helper.setSubject("메일 제목 테스트중"); helper.setText(htmlTxt,true);
	 * mailsender.send(message);
	 * 
	 * }
	 */
private final VerificationCodeRepository verificationCodeRepository;
	
	
	@Transactional
	public void sendVerifactionCode(VerifyEmailRequest req) throws AlreadyVerifiedException {
		// 이미 인증을 통과했는지 확인
		Optional<VerificationCode> found = verificationCodeRepository.findTop1ByEmailOrderByCreatedDesc(req.getEmail());
		
		if(found.isPresent() && found.get().getState() != null) {
			throw new AlreadyVerifiedException();
		}
		
		  
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
					
					인증코드 : #{code}
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








