package net.reduck.loong.configuration;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.Random;

/**
 * @author Gin
 * @since 2024/2/27 11:34
 */
public abstract class AbstractEncryptor implements Encryptor {
    private int saltLen = 16;
    private final Random random = new SecureRandom();

    @Override
    public String encrypt(String message) {
        return Base64.getEncoder().encodeToString(doEncrypt(wrapData(message.getBytes())));
    }

    @Override
    public String decrypt(String message) {
        return new String(unwrapData(doDecrypt(Base64.getDecoder().decode(message))));
    }

    protected byte[] generateSalt() {
        byte[] salt = new byte[saltLen];
        random.nextBytes(salt);
        return salt;
    }

    protected byte[] wrapData(byte[] data) {
        return mergeArrays(generateSalt(), data);
    }

    protected byte[] unwrapData(byte[] saltData) {
        byte[] data = new byte[saltData.length - saltLen];
        System.arraycopy(saltData, saltLen, data, 0, data.length);
        return data;
    }

    protected abstract byte[] doEncrypt(byte[] data);

    protected abstract byte[] doDecrypt(byte[] data);

    protected byte[] mergeArrays(byte[] array1, byte[] array2) {
        byte[] mergedArray = new byte[array1.length + array2.length];
        System.arraycopy(array1, 0, mergedArray, 0, array1.length);
        System.arraycopy(array2, 0, mergedArray, array1.length, array2.length);
        return mergedArray;
    }
}
