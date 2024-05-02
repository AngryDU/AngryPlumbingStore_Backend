package com.angrydu.plumbingstore.dto;

import com.angrydu.plumbingstore.entity.User;

import java.util.UUID;

public record UserDtoForResponse(
        UUID id,
        String email,
        String nickName,
        User.Status status) {
}
