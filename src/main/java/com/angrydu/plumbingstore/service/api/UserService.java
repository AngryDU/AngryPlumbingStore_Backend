package com.angrydu.plumbingstore.service.api;

import com.angrydu.plumbingstore.dto.UserDtoForResponse;
import com.angrydu.plumbingstore.dto.UserDtoForSave;
import com.angrydu.plumbingstore.entity.User;
import jakarta.servlet.http.Cookie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface UserService {
    User getById(UUID id);

    UserDtoForResponse getByEmail(String email);

    Page<User> getAll(Pageable pageable);

    User create(User user);

    void delete(UUID id);

    void registerUser(UserDtoForSave dto);

    Cookie loginUser(String username, String password);

    void verify(String username, String code);

    void activateUser(UUID userId);

    void recoveryPassword(String email);

    void changePassword(UUID userId, String newPassword);

    void updatePassword(UUID userId, String oldPassword, String newPassword);
}
