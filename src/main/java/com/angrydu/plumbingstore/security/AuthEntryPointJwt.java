package com.angrydu.plumbingstore.security;

import com.angrydu.plumbingstore.exception.CustomException;
import com.angrydu.plumbingstore.exception.ExceptionLocations;
import com.angrydu.plumbingstore.exception.ExceptionResponse;
import com.angrydu.plumbingstore.message.InternalizationMessageManagerConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
public class AuthEntryPointJwt implements AuthenticationEntryPoint {
    private final ObjectMapper mapper;

    @Autowired
    public AuthEntryPointJwt(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException {

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        ExceptionLocations location;

        if (authException instanceof InsufficientAuthenticationException) {
            location = ExceptionLocations.TOKEN_FORBIDDEN;
        } else {
            location = ExceptionLocations.TOKEN_NOT_FOUND;
        }

        final ExceptionResponse exceptionResponse = new ExceptionResponse(
                authException.getMessage(),
                InternalizationMessageManagerConfig
                        .getExceptionMessage(location.toString()),
                LocalDateTime.now().toString()
        );

        mapper.writeValue(response.getOutputStream(), exceptionResponse);

        throw new CustomException(authException.getMessage(), location);
    }
}

