package com.angrydu.plumbingstore.controller;

import com.angrydu.plumbingstore.dto.UserDtoForAuth;
import com.angrydu.plumbingstore.dto.UserDtoForRecoveryPass;
import com.angrydu.plumbingstore.dto.UserDtoForSave;
import com.angrydu.plumbingstore.dto.UserDtoForUpdatePass;
import com.angrydu.plumbingstore.message.InternalizationMessageManagerConfig;
import com.angrydu.plumbingstore.security.JwtUtils;
import com.angrydu.plumbingstore.service.api.TokenLinkService;
import com.angrydu.plumbingstore.service.api.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import java.net.URI;
import java.util.UUID;

import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

@CrossOrigin(origins = "http://localhost:3000",
        methods = {GET, POST, PUT, DELETE},
        allowedHeaders = {"Origin", "Content-Type", "Accept", "Authorization"},
        allowCredentials = "true", maxAge = 3600)
@RestController
@RequestMapping("/auth")
public class AuthRestController {

    private static final String USER_RECOVERY_SEND_TO_EMAIL = "AuthRestController.TokenSentToEmail";
    private static final String USER_UPDATE_SUCCESSFULLY = "AuthRestController.PasswordUpdatedSuccessfully";

    private final UserService userService;
    private final TokenLinkService tokenLinkService;
    private final JwtUtils jwtUtils;

    @Value("${app.frontToResetPass}")
    private String resetPassRedirect;
    @Value("${app.frontToLoginPage}")
    private String loginRedirect;

    @Autowired
    public AuthRestController(UserService userService, TokenLinkService tokenLinkService, JwtUtils jwtUtils) {
        this.userService = userService;
        this.tokenLinkService = tokenLinkService;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/login")
    @ResponseStatus(value = HttpStatus.OK)
    public void authenticateUser(@Valid @RequestBody UserDtoForAuth dtoForAuth,
                                 HttpServletResponse response) {
        Cookie cookie = userService.loginUser(dtoForAuth.email(), dtoForAuth.password());
        response.addCookie(cookie);
    }

    @PostMapping("/signout")
    @ResponseStatus(HttpStatus.OK)
    public void logoutUser(HttpServletResponse response) {
        SecurityContextHolder.clearContext();
        Cookie cookie = jwtUtils.getCleanJwtCookie();
        response.addCookie(cookie);
    }

    @PostMapping("/registration")
    public ResponseEntity<RedirectView> registration(@Valid @RequestBody UserDtoForSave user) {
        userService.registerUser(user);
        RedirectView redirectView = new RedirectView(loginRedirect);
        return new ResponseEntity<>(redirectView, HttpStatus.SEE_OTHER);
    }

    @GetMapping("/activate/{token}/{userId}")
    public ResponseEntity<?> activateUser(@PathVariable String token, @PathVariable UUID userId) {
        tokenLinkService.activate(token);
        userService.activateUser(userId);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(loginRedirect));
        return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);
    }

    @PostMapping("/recoveryPass")
    public ResponseEntity<?> performRecoveryPass(@Valid @RequestBody UserDtoForRecoveryPass user) {
        userService.recoveryPassword(user.email());
        return new ResponseEntity<>(InternalizationMessageManagerConfig
                .getExceptionMessage(USER_RECOVERY_SEND_TO_EMAIL), HttpStatus.OK);
    }

    @GetMapping("/recoveryPass/{token}/{userId}")
    public ResponseEntity<?> recoveryPass(@PathVariable String token, @PathVariable Long userId) {
        tokenLinkService.activate(token);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(resetPassRedirect + userId));
        return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);
    }

    @PostMapping("/changePassword")
    public ResponseEntity<?> changePassword(@Valid @RequestBody UserDtoForUpdatePass user) {
        userService.changePassword(user.id(), user.newPassword());
        return new ResponseEntity<>(InternalizationMessageManagerConfig
                .getExceptionMessage(USER_UPDATE_SUCCESSFULLY), HttpStatus.OK);
    }

    @PostMapping("/updatePassword")
    public ResponseEntity<?> updatePassword(@Valid @RequestBody UserDtoForUpdatePass user) {
        userService.updatePassword(user.id(), user.oldPassword(), user.newPassword());
        return new ResponseEntity<>(InternalizationMessageManagerConfig
                .getExceptionMessage(USER_UPDATE_SUCCESSFULLY), HttpStatus.OK);
    }
}