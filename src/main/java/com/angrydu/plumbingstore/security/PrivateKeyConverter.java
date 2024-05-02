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
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;

@Component
@ConfigurationPropertiesBinding
public class PrivateKeyConverter implements Converter<String, RSAPrivateKey> {
    public static final String KEY_FOR_EXCEPTION_ALGORITHM_NOT_FOUND = "PrivateKeyConverter.AlgorithmNotFound";
    public static final String KEY_FOR_EXCEPTION_INVALID_KEY_SPEC = "PrivateKeyConverter.InvalidKeySpec";

    @Override
    public RSAPrivateKey convert(String key) {
        String privateKeyPEM = key
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replaceAll(System.lineSeparator(), "")
                .replace("-----END PRIVATE KEY-----", "");

        byte[] encoded = Base64.decodeBase64(privateKeyPEM);

        KeyFactory keyFactory = null;
        try {
            keyFactory = KeyFactory.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            throw new CustomException(InternalizationMessageManagerConfig
                    .getExceptionMessage(KEY_FOR_EXCEPTION_ALGORITHM_NOT_FOUND),
                    ExceptionLocations.KEY_ERROR);
        }

        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encoded);
        try {
            return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
        } catch (InvalidKeySpecException e) {
            throw new CustomException(InternalizationMessageManagerConfig
                    .getExceptionMessage(KEY_FOR_EXCEPTION_INVALID_KEY_SPEC),
                    ExceptionLocations.KEY_ERROR);
        }
    }
}
