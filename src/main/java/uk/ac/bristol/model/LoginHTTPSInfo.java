package uk.ac.bristol.model;

import uk.ac.bristol.util.AesEncryptionUtil;

/**
 * * https Info
 **/
public class LoginHTTPSInfo {


    private String key;
    private String  id;
    private String userName;
    private String encryptPassword;


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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEncryptPassword() {
        return encryptPassword;
    }

    public void setEncryptPassword(String encryptPassword) {
        this.encryptPassword = encryptPassword;
    }


    public String  getDecryptPassword(){

        try {
            return AesEncryptionUtil.decrypt(encryptPassword,key);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public LoginHTTPSInfo(String key, String id, String userName, String encryptPassword) {
        this.key = key;
        this.id = id;
        this.userName = userName;

        try {
            this.encryptPassword = AesEncryptionUtil.encrypt(encryptPassword,key);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
