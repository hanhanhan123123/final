package org.edupoll.repository;

import org.edupoll.repository.PasswordChangeRepository;
import org.springframework.stereotype.Repository;

@Repository
public class PasswordChangeRepositoryImpl implements PasswordChangeRepository {

    @Override
    public boolean verifyPassword(String userEmail, String password) {
		return false;
        // 비밀번호 검증 로직 구현
    }

    @Override
    public boolean changePassword(String userEmail, String newPassword) {
		return false;
        // 비밀번호 변경 로직 구현
    }
}