package net.reduck.loong.configuration;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

/**
 * @author Gin
 * @since 2024/2/26 15:26
 */
public class LoongClientTest {

    @SneakyThrows
    @Test
    public void test() {
        int len = RsaUtils.getKeyPair(2048).getPublic().getEncoded().length;
        System.out.println(len);
//        LoongClient client = new LoongClient("1234567812345678".getBytes(), new AesEncryptor(), "$LOONG{", "}");
        LoongClient client = new LoongClient(
                "jNv+9zaswpyg137xDXAJHvCTST3j9/14w4qhvbxkgoVeAkcNQbc9iPH+THPP36HKtaetl2dQnbK125GL6fyKIqGkpw7HLCLzSaRav7Tud+Vfh2WCfjnQGpBU41yz1DXajHlO8o2SOqksJzQ0lRq7uu4xDVgdjVRfDuJyTC3OyDnBN3wz7dC9GHNxAAqMdSAVtCjMaR27tBkL74sel1tL3UpbhvchiHpxaA3znRuYuEp55Rkl6xNJZJtZ4TDzJAUiPLe7Z6MO2Jw7fsyriz2LNknHyOeIwYS5HKJZ872uKM47FRmsrCLqPgSwiV0ahqtV+1bTjZ0E3SviytitC/mW9g=="
                , new AesGcmEncryptor(), "$LOONG{", "}");
        String enc = client.encrypt("Hello World");
        String dec = client.decrypt(enc);

        System.out.println(enc);
        System.out.println(dec);
        System.out.println(client.getEncryptSecretKey());

        System.out.println(new String(PrivateKeyFinder.getSecretKey(client.getEncryptSecretKey())));

        print(client, "test1111222222333344445555");
        print(client, "test2222");
        print(client, "test3333");
        print(client, "test4444");
        print(client, "test5555");
    }

    void print(LoongClient client, String property) {
        System.out.println(client.decrypt(client.encrypt(property)) + "\n=" + client.encrypt(property));
    }
}
