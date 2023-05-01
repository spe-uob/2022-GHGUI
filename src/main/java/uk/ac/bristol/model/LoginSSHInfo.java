package uk.ac.bristol.model;

import lombok.Getter;
import lombok.Setter;
import uk.ac.bristol.util.AesEncryptionUtil;

/** ssh info. */
public class LoginSSHInfo {

  @Getter @Setter private String key;
  @Getter @Setter private String id;
  @Getter @Setter private String path;
  @Getter @Setter private String passphrase;

  public String getDecryptPassphrase() {
    try {
      return AesEncryptionUtil.decrypt(passphrase, key);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public LoginSSHInfo(String key, String id, String path, String passphrase) {
    this.key = key;
    this.id = id;
    this.path = path;
    try {
      this.passphrase = AesEncryptionUtil.encrypt(passphrase, key);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
