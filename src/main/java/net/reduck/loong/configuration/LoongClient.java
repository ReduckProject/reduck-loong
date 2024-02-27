package net.reduck.loong.configuration;

import java.util.Base64;

/**
 * @author Gin
 * @since 2024/2/26 12:56
 */
public class LoongClient {

    private final byte[] secretKey;

    private final Encryptor encryptor;
    private final EncryptionWrapperDetector detector;

    private final String prefix;
    private final String suffix;


    public LoongClient(byte[] secretKey, Encryptor encryptor, String prefix, String suffix) {
        this.secretKey = secretKey;
        this.encryptor = encryptor;
        this.prefix = prefix;
        this.suffix = suffix;

        encryptor.initKey(secretKey);
        detector = new EncryptionWrapperDetector(prefix, suffix);
    }

    public LoongClient(String secretKey, Encryptor encryptor, String prefix, String suffix) {
        this.secretKey = PrivateKeyFinder.getSecretKey(secretKey);
        this.encryptor = encryptor;
        this.prefix = prefix;
        this.suffix = suffix;

        encryptor.initKey(this.secretKey);
        detector = new EncryptionWrapperDetector(prefix, suffix);
    }

    public String getEncryptSecretKey() {
        return Base64.getEncoder().encodeToString(RsaUtils.encrypt(secretKey, Base64.getDecoder().decode(PrivateKeyFinder.loadPublicKey())));
    }

    public String encrypt(String property) {
        return detector.wrapper(encryptor.encrypt(property));
    }

    public String decrypt(String property) {
        if (detector.detected(property)) {
            return encryptor.decrypt(detector.unWrapper(property));
        }

        return property;
    }
}
