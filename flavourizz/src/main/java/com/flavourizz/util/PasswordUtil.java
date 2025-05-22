package com.flavourizz.util;

import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class PasswordUtil {
    private static final int SALT_LENGTH_BYTE = 16;
    private static final int HASH_LENGTH_BIT = 256;
    private static final int ITERATIONS = 65536;

    private static byte[] getRandomSalt() {
        byte[] salt = new byte[SALT_LENGTH_BYTE];
        new SecureRandom().nextBytes(salt);
        return salt;
    }

    private static SecretKey getAESKeyFromPassword(char[] password, byte[] salt) throws Exception {
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        PBEKeySpec spec = new PBEKeySpec(password, salt, ITERATIONS, HASH_LENGTH_BIT);
        return factory.generateSecret(spec);
    }

    public static String hashPassword(String password) {
        try {
            byte[] salt = getRandomSalt();
            SecretKey key = getAESKeyFromPassword(password.toCharArray(), salt);
            byte[] hash = key.getEncoded();

            ByteBuffer buffer = ByteBuffer.allocate(salt.length + hash.length);
            buffer.put(salt);
            buffer.put(hash);

            return Base64.getEncoder().encodeToString(buffer.array());
        } catch (Exception ex) {
            Logger.getLogger(PasswordUtil.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public static boolean verifyPassword(String password, String stored) {
        try {
            byte[] decoded = Base64.getDecoder().decode(stored);
            ByteBuffer buffer = ByteBuffer.wrap(decoded);

            byte[] salt = new byte[SALT_LENGTH_BYTE];
            buffer.get(salt);

            byte[] storedHash = new byte[buffer.remaining()];
            buffer.get(storedHash);

            SecretKey key = getAESKeyFromPassword(password.toCharArray(), salt);
            byte[] computedHash = key.getEncoded();

            return MessageDigest.isEqual(storedHash, computedHash);
        } catch (Exception ex) {
            Logger.getLogger(PasswordUtil.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

	public static String encrypt(String password) {
		// TODO Auto-generated method stub
		return null;
	}
	
}