package uk.ac.bristol.util;

import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import lombok.experimental.UtilityClass;

/**
 * The AesEncryptionUtil class is a Java utility class that provides methods for encrypting and
 * decrypting text using the AES encryption algorithm.
 */
@UtilityClass
public class AesEncryptionUtil {
  /** The encryption algorithm used. */
  private static final String ALGORITHM = "AES";
  /** The mode and padding used. */
  private static final String TRANSFORMATION = "AES/ECB/PKCS5Padding";

  /**
   * Encryption method.
   *
   * @param plainText your want convert text
   * @param key your Encryption Key
   * @return encrypt
   * @throws Exception exception
   */
  public static String encrypt(final String plainText, final String key) throws Exception {
    // Create a SecretKeySpec object from the key
    final SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), ALGORITHM);
    // Create a Cipher object using the specified transformation
    final Cipher cipher = Cipher.getInstance(TRANSFORMATION);
    cipher.init(Cipher.ENCRYPT_MODE, keySpec); // Initialize the cipher with the key and the mode
    final byte[] encrypted = cipher.doFinal(plainText.getBytes()); // Encrypt the plaintext
    // Encode the encrypted data as a Base64 string
    return Base64.getEncoder().encodeToString(encrypted);
  }

  /**
   * Decryption method.
   *
   * @param encryptedText your encryption text
   * @param key your Key
   * @return decrypt text
   * @throws Exception exception
   */
  public static String decrypt(final String encryptedText, final String key) throws Exception {
    // Create a SecretKeySpec object from the key
    final SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), ALGORITHM);
    // Create a Cipher object using the specified transformation
    final Cipher cipher = Cipher.getInstance(TRANSFORMATION);
    cipher.init(Cipher.DECRYPT_MODE, keySpec); // Initialize the cipher with the key and the mode
    // Decrypt the encrypted text
    final byte[] decrypted = cipher.doFinal(Base64.getDecoder().decode(encryptedText));
    return new String(decrypted); // Return the decrypted plaintext as a string
  }
}
