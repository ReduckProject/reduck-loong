package net.reduck.loong.configuration;

import lombok.SneakyThrows;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Random;

/**
 * @author Gin
 * @since 2023/5/23 19:35
 */
public class AesEncryptor extends AbstractEncryptor {

    private byte[] secretKey;
    private final byte[] iv = new byte[16];
    private final Random random = new Random();

    @Override
    public void initKey(byte[] secretKey) {
        this.secretKey = secretKey;
        System.arraycopy(secretKey, 0, iv, 0, 16);
    }

    private final String CIPHER_ALGORITHM = "AES/CBC/PKCS5Padding";
    private final String KEY_TYPE = "AES";

    @SneakyThrows
    @Override
    protected byte[] doEncrypt(byte[] data) {
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(secretKey, KEY_TYPE), new IvParameterSpec(iv));
        return cipher.doFinal(data);
    }

    @SneakyThrows
    @Override
    protected byte[] doDecrypt(byte[] data) {
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(secretKey, KEY_TYPE), new IvParameterSpec(iv));
        return cipher.doFinal(data);
    }
}
