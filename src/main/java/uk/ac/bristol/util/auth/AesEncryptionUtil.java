package uk.ac.bristol.util.auth;

import java.io.File;
import java.io.FileOutputStream;
import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import lombok.experimental.UtilityClass;
import uk.ac.bristol.util.GitInfo;

/**
 * The AesEncryptionUtil class is a Java utility class that provides methods for encrypting and
 * decrypting text using the AES encryption algorithm.
 */
@UtilityClass
public class AesEncryptionUtil {
  /** The encryption algorithm used. */
  private static final String ALGORITHM = "AES";
  /** The mode and padding used. */
  private static final String TRANSFORMATION = "AES/GCM/NoPadding";
  /** Size of the GCM Initial Vector. */
  private static final int GCM_IV_LENGTH = 12;
  /** Size of the GCM Tag. */
  private static final int GCM_TAG_LENGTH = 16;

  /**
   * Write the contents of the current git credentials into a file.
   *
   * @param file The file to write to
   * @param key The key to use to unlock the file
   */
  public static void writeToFile(final File file, final String key) {
    final byte[] iv = new byte[GCM_IV_LENGTH];
    final SecureRandom random = new SecureRandom();
    random.nextBytes(iv);
    try (FileOutputStream st = new FileOutputStream(file, false)) {
      final GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH * 8, iv);
      final SecretKey secretKey = new SecretKeySpec(key.getBytes(), ALGORITHM);
      final Cipher cipher = Cipher.getInstance(TRANSFORMATION);
      cipher.init(Cipher.ENCRYPT_MODE, secretKey, gcmParameterSpec);
      st.write(iv);
      try (CipherOutputStream cst = new CipherOutputStream(st, cipher)) {
        for (var cred : GitInfo.getHttpAuth().values()) {
          cst.write(cred.toByteStream());
        }
        for (var cred : GitInfo.getSshAuth().values()) {
          cst.write(cred.toByteStream());
        }
      }
    } catch (Exception e) {
      // TODO: HANDLE
    }
  }

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
