package net.reduck.loong.configuration;

/**
 * @author Gin
 * @since 2023/5/6 14:54
 */
public interface Encryptor {

    default void initKey(byte[] secretKey) {
    }

    ;

    String encrypt(String message);

    String decrypt(String message);
}
