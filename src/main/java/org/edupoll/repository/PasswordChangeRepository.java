package org.edupoll.repository;


public interface PasswordChangeRepository {
    boolean verifyPassword(String userEmail, String password);
    boolean changePassword(String userEmail, String newPassword);
}