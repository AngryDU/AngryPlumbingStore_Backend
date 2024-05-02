package com.angrydu.plumbingstore.dto;

import com.angrydu.plumbingstore.entity.User;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.Length;

import static com.angrydu.plumbingstore.service.impl.UserServiceImpl.EMAIL_NOT_CORRECT;
import static com.angrydu.plumbingstore.service.impl.UserServiceImpl.NAME_NOT_CORRECT;
import static com.angrydu.plumbingstore.service.impl.UserServiceImpl.PASSWORD_NOT_CORRECT;
import static com.angrydu.plumbingstore.service.impl.UserServiceImpl.STATUS_NOT_SPECIFIED;
import static com.angrydu.plumbingstore.service.impl.UserServiceImpl.THE_LENGTH_OF_THE_NAME;

public record UserDtoForSave(
        @NotBlank(message = EMAIL_NOT_CORRECT)
        @Pattern(message = "Bad formed user username: ${validatedValue}",
                regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9-]+\\.[A-Za-z0-9-.]+$")
        @Length(min = 2, max = 255, message = THE_LENGTH_OF_THE_NAME)
        String email,
        @NotBlank(message = PASSWORD_NOT_CORRECT)
        @Pattern(message = "Bad formed user password",
                regexp = "^[a-zA-Z0-9!@#$%^&*()-_+=~`{}\\[\\]|\\\\:;'<>,.?\\/]{8,24}$")
        @Size(min = 8)
        String password,
        @NotBlank(message = NAME_NOT_CORRECT)
        @Length(min = 2, max = 255, message = THE_LENGTH_OF_THE_NAME)
        String firstName,

        @NotBlank(message = NAME_NOT_CORRECT)
        @Length(min = 2, max = 255, message = THE_LENGTH_OF_THE_NAME)
        String lastName,

        @NotNull(message = STATUS_NOT_SPECIFIED)
        @Enumerated(EnumType.STRING)
        User.Status status) {
}
