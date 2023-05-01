package uk.ac.bristol.model;

import java.io.Serializable;
import uk.ac.bristol.util.AesEncryptionUtil;

/** * token info */
public class LoginTokenInfo implements Serializable {

  private String key;
  private String id;
  private String encryptToken;

  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getEncryptToken() {
    return encryptToken;
  }

  public void setEncryptToken(String encryptToken) {
    this.encryptToken = encryptToken;
  }

  public String getDecryptToken() {

    try {
      return AesEncryptionUtil.decrypt(encryptToken, key);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public LoginTokenInfo(String key, String id, String encryptToken) {
    this.key = key;
    this.id = id;

    try {
      this.encryptToken = AesEncryptionUtil.encrypt(encryptToken, key);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public String toString() {
    return "LoginTokenInfo{"
        + "key='"
        + key
        + '\''
        + ", id='"
        + id
        + '\''
        + ", encryptToken='"
        + encryptToken
        + '\''
        + '}';
  }
}
