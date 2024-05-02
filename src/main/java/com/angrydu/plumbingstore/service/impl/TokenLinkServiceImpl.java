package com.angrydu.plumbingstore.service.impl;

import com.angrydu.plumbingstore.entity.SecurityUser;
import com.angrydu.plumbingstore.entity.TokenLink;
import com.angrydu.plumbingstore.exception.CustomException;
import com.angrydu.plumbingstore.exception.ExceptionLocations;
import com.angrydu.plumbingstore.repository.TokenLinkRepository;
import com.angrydu.plumbingstore.service.api.TokenLinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;

@Service("linkService")
public class TokenLinkServiceImpl implements TokenLinkService {
    public static final String TOKEN_NOT_FOUND = "TokenLinkService.TokenNotFound";
    public static final String TOKEN_ALREADY_ACTIVATED = "TokenLinkService.TokenAlreadyActivated";
    public static final String TOKEN_EXPIRED = "TokenLinkService.TokenExpired";
    private static final String ISSUER = "Angryangrydu.plumbingstoreDU";
    private final TokenLinkRepository tokenLinkRepository;
    private final JwtEncoder encoder;
    private final JwtDecoder decoder;

    @Autowired
    public TokenLinkServiceImpl(TokenLinkRepository tokenLinkRepository, JwtEncoder encoder, JwtDecoder decoder) {
        this.tokenLinkRepository = tokenLinkRepository;
        this.encoder = encoder;
        this.decoder = decoder;
    }

    @Override
    public String generateToken(Authentication authentication, int seconds) {
        TokenLink tokenLink = setTokenLink(authentication, seconds);
        tokenLinkRepository.save(tokenLink);
        return tokenLink.getToken();
    }

    @Override
    public String generateToken(int seconds, String username) {
        TokenLink tokenLink = setTokenLinkForRegistration(seconds, username);
        tokenLinkRepository.save(tokenLink);
        return tokenLink.getToken();
    }

    @Override
    public void activate(String token) {
        TokenLink existingToken = tokenLinkRepository.findByEmailToken(token).orElseThrow(() -> {
            throw new CustomException(TOKEN_NOT_FOUND, ExceptionLocations.TOKEN_NOT_FOUND);
        });
        if (existingToken.isActive()) {
            throw new CustomException(TOKEN_ALREADY_ACTIVATED, ExceptionLocations.TOKEN_SERVICE_CONFLICT);
        }
        if (isExpired(existingToken)) {
            throw new CustomException(TOKEN_EXPIRED, ExceptionLocations.TOKEN_FORBIDDEN);
        }
        existingToken.setActive(true);
        tokenLinkRepository.save(existingToken);
    }

    private boolean isExpired(TokenLink token) {
        Instant expirationTime = decoder.decode(token.getToken()).getClaim("exp");
        return Instant.now().isAfter(expirationTime);
    }

    private TokenLink setTokenLink(Authentication authentication, int seconds) {
        TokenLink tokenLink = new TokenLink();
        tokenLink.setToken(generateTokenLinkForLogin(authentication, seconds));
        return tokenLink;
    }

    private TokenLink setTokenLinkForRegistration(int seconds, String username) {
        TokenLink tokenLink = new TokenLink();
        tokenLink.setToken(generateTokenLinkForRegistration(seconds, username));
        tokenLink.setActive(false);
        return tokenLink;
    }

    private String generateTokenLinkForLogin(Authentication authentication, int seconds) {
        Instant now = Instant.now();
        String scope = authentication.getAuthorities()
                .stream().map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));
        SecurityUser userDetails = (SecurityUser) authentication.getPrincipal();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer(ISSUER)
                .issuedAt(now)
                .expiresAt(now.plus(seconds, ChronoUnit.SECONDS))
                .id(userDetails.getId().toString())
                .claim("email", authentication.getName())
                .claim("scope", scope)
                .build();
        return this.encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    private String generateTokenLinkForRegistration(int seconds, String username) {
        Instant now = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer(ISSUER)
                .subject(username)
                .issuedAt(now)
                .expiresAt(now.plus(seconds, ChronoUnit.SECONDS))
                .build();
        return this.encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }
}
