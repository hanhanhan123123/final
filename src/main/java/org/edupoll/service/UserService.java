package org.edupoll.service;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Base64;
import java.util.Optional;

import org.edupoll.exception.ExistUserEmailException;
import org.edupoll.exception.InvalidPasswordException;
import org.edupoll.exception.NotExistUserException;
import org.edupoll.exception.VerifyCodeException;
import org.edupoll.model.dto.KakaoAccount;
import org.edupoll.model.dto.UserWrapper;
import org.edupoll.model.dto.request.CreateUserRequest;
import org.edupoll.model.dto.request.DeleteUserRequest;
import org.edupoll.model.dto.request.UpdateProfileRequest;
import org.edupoll.model.dto.request.ValidateUserRequest;
import org.edupoll.model.dto.request.VerifyCodeRequest;
import org.edupoll.model.dto.request.VerifyEmailRequest;
import org.edupoll.model.entity.ProfileImage;
import org.edupoll.model.entity.User;
import org.edupoll.model.entity.VerificationCode;
import org.edupoll.repository.ProfileImagaRepository;
import org.edupoll.repository.UserRepository;
import org.edupoll.repository.VerificationCodeRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileUrlResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.transaction.NotSupportedException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

	private final UserRepository userRepository;
	private final VerificationCodeRepository verificationCodeRepository;
	private final ProfileImagaRepository profileImagaRepository;

	@Value("${upload.server}")
	String uploadServer;
	@Value("${upload.basedir}")
	String baseDir;
	
	@Transactional
	public void registerNewUser(CreateUserRequest dto) throws ExistUserEmailException, VerifyCodeException {
//		User found = userRepository.findByEmail(dto.getEmail());
		if (userRepository.existsByEmail(dto.getEmail())) {
			throw new ExistUserEmailException();
		}
		// 인증절차를 거쳤는지 확인
		VerificationCode found = verificationCodeRepository.findTop1ByEmailOrderByCreatedDesc(dto.getEmail())
				.orElseThrow(() -> new VerifyCodeException("인증코드 검증 기록이 존재하지 않습니다."));
		if (found.getState() == null) {
			throw new VerifyCodeException("아직 미 인증 상태입니다.");
		}

		// 회원정보 등록
		User one = new User();
		one.setEmail(dto.getEmail());
		one.setName(dto.getName());
		one.setPassword(dto.getPassword());
		userRepository.save(one);

	}

	@Transactional
	public void validateUser(ValidateUserRequest req) throws NotExistUserException, InvalidPasswordException {
		User found = userRepository.findByEmail(req.getEmail()).orElseThrow(() -> new NotExistUserException());

//		if(found == null) {
//			throw new NotExistUserException();
//		}

		boolean isSame = found.getPassword().equals(req.getPassword());
		if (!isSame) {
			throw new InvalidPasswordException();
		}
		// .............
	}

	@Transactional
	public void verfiySpecificCode(@Valid VerifyCodeRequest req) throws VerifyCodeException {
		Optional<VerificationCode> result = verificationCodeRepository
				.findTop1ByEmailOrderByCreatedDesc(req.getEmail());

		VerificationCode found = result.orElseThrow(() -> new VerifyCodeException("인증코드 발급받은 적이 없습니다."));

		long elapsed = System.currentTimeMillis() - found.getCreated().getTime();
		if (elapsed > 1000 * 60 * 10) {
			throw new VerifyCodeException("인증코드 유효시간이 만료되었습니다.");
		}
		if (!found.getCode().equals(req.getCode())) {
			throw new VerifyCodeException("인증코드가 일치하지 않습니다.");
		}

		found.setState("passed");
		verificationCodeRepository.save(found);
	}

	@Transactional
	public void deleteSpecificSocialUser(String userEmail) throws NotExistUserException {
		var user = userRepository.findByEmail(userEmail).orElseThrow(() -> new NotExistUserException());
		userRepository.delete(user);
	}

	@Transactional
	public void deleteSpecificUser(String userEmail, DeleteUserRequest req)
			throws NotExistUserException, InvalidPasswordException {
		var user = userRepository.findByEmail(userEmail).orElseThrow(() -> new NotExistUserException());

		if (!user.getPassword().equals(req.getPassword())) {

			throw new InvalidPasswordException();
		}

		userRepository.delete(user);
		verificationCodeRepository.deleteByEmail(userEmail);
	}

	@Transactional
	public void emailAvailableCheck(@Valid VerifyEmailRequest request) throws ExistUserEmailException {
		boolean rst = userRepository.existsByEmail(request.getEmail());
		if (rst) {
			throw new ExistUserEmailException();
		}
	}

	@Transactional
	public void updateKakaoUser(KakaoAccount account, String accessToken) {
		// 인증코드를 확보한 카카오유저에 해당하는 정보를 UserRepository에 찾는데
		// 있다면 update - (accessToken)
		Optional<User> _user = userRepository.findByEmail(account.getEmail());
		if (_user.isPresent()) {
			User saved = _user.get();
			saved.setSocial(accessToken);
			userRepository.save(saved);
		} else {
			// 그게 아니면 save
			User user = new User();
			user.setEmail(account.getEmail());
			user.setName(account.getNickname());
			user.setProfileImage(account.getProfileImage());
			user.setSocial(accessToken);
			userRepository.save(user);
		}
	}

	@Transactional
	// 특정유저 정보 업데이트
	public void modifySpecificUser(String userEmail, UpdateProfileRequest request)
			throws IOException, NotSupportedException {

		// log.info("req.name = {}", request.getName());
		// log.info("req.profile exist {}", request.getProfile() != null);
		var foundUser = userRepository.findByEmail(userEmail).get(); // 있는지 없는지 체크
		foundUser.setName(request.getName());
		
		if (request.getProfile() != null) {
			// 리퀘스트 객체에서 파일 정보를 뽑자
			MultipartFile multi = request.getProfile();
			// 해당 파일이 컨텐츠 타입이 이미지인 경우에만 처리
			if (!multi.getContentType().startsWith("image/")) {
				throw new NotSupportedException("이미지 파일만 설정가능합니다.");
			}

			// 파일을 옮기는 작업
			// 기본 세이브경로는 properties에서
			String emailEncoded = new String(Base64.getEncoder().encode(userEmail.getBytes()));

			File saveDir = new File(baseDir + "/profile/" + emailEncoded);
			saveDir.mkdirs();

			// 파일명은 로그인사용자의 이메일주소를 활용해서
			String filename = System.currentTimeMillis()
					+ multi.getOriginalFilename().substring(multi.getOriginalFilename().lastIndexOf("."));

			File dest = new File(saveDir, filename);

			// 두개 조합해서 옮길 장소 설정
			// 옮겨두기
			multi.transferTo(dest); // 업로드 됨.
			// 파일 정보를 DB에 insert
			foundUser.setProfileImage(uploadServer + "/resource/profile/" + emailEncoded + "/" + filename);
		}

		userRepository.save(foundUser);
	}

	public Resource loadResource(String url) throws NotExistUserException, MalformedURLException {
		log.warn("resource url {} ", url);
		var found = profileImagaRepository.findTop1ByUrl(url).orElseThrow(() -> new NotExistUserException());

		return new FileUrlResource(found.getFileAddress());
	}

	public UserWrapper searchUserByEmail(String tokenEmailValue) throws NotExistUserException {
		var found = userRepository.findByEmail(tokenEmailValue) //
				.orElseThrow(() -> new NotExistUserException());
		return new UserWrapper(found);
	}

}
