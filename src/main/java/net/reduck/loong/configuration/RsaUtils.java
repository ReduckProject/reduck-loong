package net.reduck.loong.configuration;

import lombok.SneakyThrows;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * @author Gin
 * @since 2021/6/5 17:23
 */
public class RsaUtils {

    public static byte[] encrypt(byte[] data, PublicKey key) {
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return cipher.doFinal(data);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | BadPaddingException | IllegalBlockSizeException |
                 InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }

    @SneakyThrows
    public static byte[] encrypt(byte[] data, byte[] key) {
        return encrypt(data, getPublicKey(key));
    }


    public static byte[] decrypt(byte[] data, PrivateKey key) {
        Cipher cipher;
        try {
            cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, key);
            return cipher.doFinal(data);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | BadPaddingException | IllegalBlockSizeException |
                 InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] decrypt(byte[] data, byte[] key) {
        try {
            return decrypt(data, getPrivateKey(key));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @SneakyThrows
    public static PrivateKey getPrivateKey(byte[] bytes) throws IOException {
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(bytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePrivate(spec);
    }

    @SneakyThrows
    public static PublicKey getPublicKey(byte[] bytes) throws IOException {
        X509EncodedKeySpec spec = new X509EncodedKeySpec(bytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(spec);
    }

    public static KeyPair getKeyPair(int keyLength) throws Exception {
        //BC即BouncyCastle加密包，EC为ECC算法
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(keyLength, new SecureRandom());
        return keyPairGenerator.generateKeyPair();
    }
}
