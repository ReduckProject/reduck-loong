package net.reduck.loong.configuration;

import lombok.SneakyThrows;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Arrays;
import java.util.Random;

/**
 * @author Gin
 * @since 2024/2/27 12:05
 */
public class AesGcmEncryptor extends AbstractEncryptor {


    private byte[] secretKey;
    private final Random random = new Random();
    private byte[] iv = new byte[12];

    @Override
    public void initKey(byte[] secretKey) {
        this.secretKey = secretKey;
        System.arraycopy(secretKey, 0, iv, 0, 12);
    }

    private final String CIPHER_ALGORITHM = "AES/GCM/NoPadding";
    private final String KEY_TYPE = "AES";

    @SneakyThrows
    @Override
    protected byte[] doEncrypt(byte[] data) {
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        byte[] iv = new byte[12];
        random.nextBytes(iv);
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(secretKey, KEY_TYPE), new GCMParameterSpec(128, iv));
        return mergeArrays(iv, cipher.doFinal(data));
    }

    @SneakyThrows
    @Override
    protected byte[] doDecrypt(byte[] data) {
        byte[] iv = new byte[12];
        System.arraycopy(data, 0, iv, 0, 12);
        byte[] encData = new byte[data.length - 12];
        System.arraycopy(data, 12, encData, 0, encData.length);

        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(secretKey, KEY_TYPE), new GCMParameterSpec(128, iv));
        return cipher.doFinal(encData);
    }

}
