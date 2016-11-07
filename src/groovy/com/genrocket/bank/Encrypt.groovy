package com.genrocket.bank

import org.apache.commons.codec.binary.Base64

class Encrypt {

    static String encrypt(text) {
        String encrypted = text
        if (encrypted) {
            try {
                byte[] encodedBytes = Base64.encodeBase64(text.getBytes());
                encrypted = new String(encodedBytes)
            } catch (Exception e) {
                println("failed to encrypt password." + e.getMessage())
            }
        }
        return encrypted
    }

    static String decrypt(text) {
        String textData = text
        byte[] encodedBytes = textData.getBytes()
        String decrypted = new String(Base64.decodeBase64(encodedBytes))
        return decrypted;
    }

}
