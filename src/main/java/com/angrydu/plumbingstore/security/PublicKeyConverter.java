package com.angrydu.plumbingstore.security;

import com.angrydu.plumbingstore.exception.CustomException;
import com.angrydu.plumbingstore.exception.ExceptionLocations;
import com.angrydu.plumbingstore.message.InternalizationMessageManagerConfig;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

@Component
@ConfigurationPropertiesBinding
public class PublicKeyConverter implements Converter<String, RSAPublicKey> {

    public static final String KEY_FOR_EXCEPTION_ALGORITHM_NOT_FOUND = "PublicKeyConverter.AlgorithmNotFound";
    public static final String KEY_FOR_EXCEPTION_INVALID_KEY_SPEC = "PublicKeyConverter.InvalidKeySpec";

    @Override
    public RSAPublicKey convert(String source) {
        String publicKeyPEM = source
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replaceAll(System.lineSeparator(), "")
                .replace("-----END PUBLIC KEY-----", "");

        byte[] encoded = Base64.decodeBase64(publicKeyPEM);

        KeyFactory keyFactory = null;
        try {
            keyFactory = KeyFactory.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            throw new CustomException(InternalizationMessageManagerConfig
                    .getExceptionMessage(KEY_FOR_EXCEPTION_ALGORITHM_NOT_FOUND),
                    ExceptionLocations.KEY_ERROR);
        }

        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(encoded);
        try {
            return (RSAPublicKey) keyFactory.generatePublic(keySpec);
        } catch (InvalidKeySpecException e) {
            throw new CustomException(InternalizationMessageManagerConfig
                    .getExceptionMessage(KEY_FOR_EXCEPTION_INVALID_KEY_SPEC),
                    ExceptionLocations.KEY_ERROR);
        }
    }
}
