package uk.ac.bristol.util.auth;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.SecureRandom;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.PBEKeySpec;
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
  private static final String ALGORITHM = "PBKDF2WithHmacSHA256";
  /** The mode and padding used. */
  private static final String TRANSFORMATION = "AES/GCM/NoPadding";
  /** Size of the AES key. */
  private static final int AES_KEY_LENGTH = 256;
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
      // Probably shouldn't be reusing the iv bytes as a salt, but since both the iv and the salt
      // are designed to be known, I see no harm. Haven't seen anyone say it's unsafe either yet.
      final PBEKeySpec pbeKeySpec = new PBEKeySpec(key.toCharArray(), iv, 1000, AES_KEY_LENGTH);
      final var tmp = SecretKeyFactory.getInstance(ALGORITHM).generateSecret(pbeKeySpec);
      final SecretKeySpec secretKey = new SecretKeySpec(tmp.getEncoded(), "AES");
      final Cipher cipher = Cipher.getInstance(TRANSFORMATION);
      cipher.init(Cipher.ENCRYPT_MODE, secretKey, gcmParameterSpec);
      st.write(iv);
      try (ObjectOutputStream cst = new ObjectOutputStream(new CipherOutputStream(st, cipher))) {
        for (var cred : GitInfo.getHttpAuth().values()) {
          cst.writeObject(cred);
        }
        for (var cred : GitInfo.getSshAuth().values()) {
          cst.writeObject(cred);
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
      // TODO: HANDLE
    }
  }

  /**
   * Write the contents of the current git credentials into a file.
   *
   * @param file The file to write to
   * @param key The key to use to unlock the file
   */
  public static void readFromFile(final File file, final String key) {
    try (FileInputStream st = new FileInputStream(file)) {
      final byte[] iv = new byte[GCM_IV_LENGTH];
      st.read(iv);
      final GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH * 8, iv);
      final PBEKeySpec pbeKeySpec = new PBEKeySpec(key.toCharArray(), iv, 1000, AES_KEY_LENGTH);
      final var tmp = SecretKeyFactory.getInstance(ALGORITHM).generateSecret(pbeKeySpec);
      final SecretKeySpec secretKey = new SecretKeySpec(tmp.getEncoded(), "AES");
      final Cipher cipher = Cipher.getInstance(TRANSFORMATION);
      cipher.init(Cipher.DECRYPT_MODE, secretKey, gcmParameterSpec);
      try (ObjectInputStream cst = new ObjectInputStream(new CipherInputStream(st, cipher))) {
        while (true) {
          if (cst.readObject() instanceof Credentials creds) {
            creds.reimport();
          }
        }
      }
    } catch (EOFException e) {
      return;
    } catch (Exception e) {
      e.printStackTrace();
      // TODO: HANDLE
    }
  }
}
