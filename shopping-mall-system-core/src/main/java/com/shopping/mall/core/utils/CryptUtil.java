package com.shopping.mall.core.utils;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.CharEncoding;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;

/**
 * <pre>
 * Description : 암복호화 유틸리티
 * Date : 2023/08/03 10:00 AM
 * Company :
 * Author : jypark
 * </pre>
 */
public class CryptUtil {

    public static String getHash(String target, byte[] salt) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        // digest반복횟수(1000번은 최소 권장횟수)
        int ITERATION_NUMBER = 1000;

        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        digest.reset();
        digest.update(salt);
        byte[] input = digest.digest(target.getBytes(StandardCharsets.UTF_8));
        for (int i = 0; i < ITERATION_NUMBER; i++) {
            digest.reset();
            input = digest.digest(input);
        }

        return Base64.encodeBase64String(input);
    }

    /**
     * AES 암호화 (CBC모드, Base64 문자열)
     *
     * @param key
     * @param src
     * @return
     */
    public static String encodeAES256(String key, String src) {
        String newKey;

        // 키값이 256Bit 미만이면 패딩해서 채움
        if (key.length() < 32) {
            newKey = StringUtils.rightPad(key, 32, '_');
        } else {
            newKey = key.substring(0, 32);
        }

        // 256bit 키값이 아닌 경우 디버그용 Print
        System.out.println(String.format("[%s]", newKey));

        // IV 값은 KEY 값의 16 바이트만 사용
        String iv = newKey.substring(0, 16);
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(newKey.getBytes(), "AES"), new IvParameterSpec(iv.getBytes()));
            return encodeBase64String(cipher.doFinal(src.getBytes(CharEncoding.UTF_8)));
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * Bytes 배열을 Base64 인코딩
     *
     * @param bytes
     * @return
     */
    public static String encodeBase64String(byte[] bytes) {
        try {
            return new String(java.util.Base64.getEncoder().encode(bytes), CharEncoding.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * AES 복호화 (CBC모드, Bytes 배열)
     *
     * @param key   암호화키
     * @param bytes 바이트배열
     * @return 복호화된 바이트배열
     * @throws InvalidAlgorithmParameterException
     * @throws InvalidKeyException
     * @throws BadPaddingException
     */
    public static byte[] decodeAES(String key, byte[] bytes) {
        // IV 값은 KEY 값의 16 바이트만 사용
        String iv = key.substring(0, 16);
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key.getBytes(), "AES"), new IvParameterSpec(iv.getBytes()));
            return cipher.doFinal(bytes);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * AES 암호화된 문자열(Base64)을 복호화
     *
     * @param key
     * @param src
     * @return
     */
    public static String decodeAES256(String key, String src) {

        String newKey;

        // 키값이 256Bit 미만이면 패딩해서 채움
        if (key.length() < 32) {
            newKey = StringUtils.rightPad(key, 32, '_');
        } else {
            newKey = key.substring(0, 32);
        }

        String decodeString;
        try {
            decodeString = new String(decodeAES(newKey, decodeBase64(src)), CharEncoding.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        return decodeString;
    }

    /**
     * 문자열을 Base64 Bytes배열로 디코딩
     *
     * @param src
     * @return
     */
    public static byte[] decodeBase64(String src) {
        try {
            return java.util.Base64.getDecoder().decode(src.getBytes(CharEncoding.UTF_8));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
