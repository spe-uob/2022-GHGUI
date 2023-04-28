package uk.ac.bristol.util;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
/**
 * The AesEncryptionUtil class is a Java utility class that provides methods for encrypting
 * and decrypting text using the AES encryption algorithm.
 */
public class AesEncryptionUtil {
    /**
     *  The encryption algorithm used
     */
    private static final String ALGORITHM = "AES";
    /**
     * The mode and padding used
     */
    private static final String TRANSFORMATION = "AES/ECB/PKCS5Padding";




    /**
     * Encryption method
     * @param plainText  your want convert text
     * @param key  your Encryption  Key
     * @return   encrypt
     * @throws Exception exception
     */
    public static String encrypt(String plainText, String key) throws Exception {
        SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), ALGORITHM); // Create a SecretKeySpec object from the key
        Cipher cipher = Cipher.getInstance(TRANSFORMATION); // Create a Cipher object using the specified transformation
        cipher.init(Cipher.ENCRYPT_MODE, keySpec); // Initialize the cipher with the key and the mode
        byte[] encrypted = cipher.doFinal(plainText.getBytes()); // Encrypt the plaintext
        return Base64.getEncoder().encodeToString(encrypted); // Encode the encrypted data as a Base64 string
    }



}
