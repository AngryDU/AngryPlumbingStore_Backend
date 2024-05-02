package com.angrydu.plumbingstore.mapper;

import com.angrydu.plumbingstore.dto.UserDtoForResponse;
import com.angrydu.plumbingstore.dto.UserDtoForSave;
import com.angrydu.plumbingstore.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public User userDtoForSaveToUser(UserDtoForSave dto) {
        User user = new User();
        user.setEmail(dto.email());
        user.setPassword(dto.password());
        user.setFirstName(dto.firstName());
        user.setLastName(dto.lastName());
        user.setStatus(dto.status());
        return user;
    }

    public UserDtoForResponse userDtoToUserDtoForResponse(User user) {
        return new UserDtoForResponse(
                user.getId(),
                user.getEmail(),
                user.getFirstName(),
                user.getStatus()
        );
    }
}
