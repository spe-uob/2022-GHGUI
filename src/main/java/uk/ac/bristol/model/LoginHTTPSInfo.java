package uk.ac.bristol.model;

import lombok.Getter;
import lombok.Setter;
import uk.ac.bristol.util.AesEncryptionUtil;

/** https Info. */
public class LoginHTTPSInfo {

  @Getter @Setter private String key;
  @Getter @Setter private String id;
  @Getter @Setter private String userName;
  @Getter @Setter private String encryptPassword;

  public final String getDecryptPassword() {
    try {
      return AesEncryptionUtil.decrypt(encryptPassword, key);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public LoginHTTPSInfo(
      final String key, final String id, final String userName, final String encryptPassword) {
    this.key = key;
    this.id = id;
    this.userName = userName;

    try {
      this.encryptPassword = AesEncryptionUtil.encrypt(encryptPassword, key);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
