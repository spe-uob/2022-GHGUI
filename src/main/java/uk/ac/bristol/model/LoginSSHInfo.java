package uk.ac.bristol.model;

import uk.ac.bristol.util.AesEncryptionUtil;

/**
 * * ssh info
 **/
public class LoginSSHInfo {

    private String key;
    private String  id;
    private String path;
    private String passphrase;

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

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getPassphrase() {
        return passphrase;
    }

    public void setPassphrase(String passphrase) {
        this.passphrase = passphrase;
    }

    public String  getDecryptPassphrase(){

        try {
            return AesEncryptionUtil.decrypt(passphrase,key);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public LoginSSHInfo(String key, String id, String path, String passphrase) {
        this.key = key;
        this.id = id;
        this.path = path;

        try {
            this.passphrase =  AesEncryptionUtil.encrypt(passphrase,key);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
