package org.hamster.sunflower_v2.services;

import java.util.UUID;

/**
 * Created by ONB-CZEIDE on 03/06/2018
 */
public class CustomKeyGenerator {

    public static String generateSeed() {
        StringBuilder id = new StringBuilder();
        id.append(UUID.randomUUID().toString().replace("-", "").toUpperCase());
        id.setLength(16);

        for (int i = 0, j = 0; i < id.length(); i++, j++) {
            if (j % 3 == 0 && j != 0 && i != id.length() - 1) {
                id.insert(i + 1, "-");
                i++;
                j = -1;
            }
        }

        return id.toString();
    }

    public static String generateWallet() {
        StringBuilder id = new StringBuilder();
        id.append(UUID.randomUUID().toString().replace("-", "").toUpperCase());

        return id.toString();
    }
}
