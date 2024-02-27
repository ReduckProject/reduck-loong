package net.reduck.loong.configuration;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.Base64;

import static net.reduck.loong.configuration.PrivateKeyFinder.loadPrivateKey;
import static net.reduck.loong.configuration.PrivateKeyFinder.loadPublicKey;

@SpringBootTest
class ReduckLoongApplicationTests {

    @Test
    void contextLoads() {

        byte[] key = loadPrivateKey();

        System.out.println(key.length);
        byte[] enc = Base64.getEncoder().encode(new PrivateKeyFinder().encrypt(Base64.getDecoder().decode(key)));

        byte[] dec = Base64.getEncoder().encode(new PrivateKeyFinder().decrypt(Base64.getDecoder().decode(enc)));

        assert !Arrays.equals(key, enc);
        assert !Arrays.equals(key, dec);

        System.out.println(new String(key));
        System.out.println(new String(enc));
        System.out.println(new String(dec));

        byte[] secretKey = Base64.getEncoder().encode(
                RsaUtils.encrypt("reduck---project".getBytes(), Base64.getDecoder().decode(loadPublicKey())
                ));

        System.out.println(new String(secretKey));

        String origin = new String(RsaUtils.decrypt(Base64.getDecoder().decode(secretKey), Base64.getDecoder().decode(loadPrivateKey())));
        System.out.println(origin);
    }

}
