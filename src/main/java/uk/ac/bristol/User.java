package uk.ac.bristol;

public class User {
  public String username; // 账号
  public String password; // 密码

  public User(String username, String password) {
    super();
    this.username = username;
    this.password = password;
  }

  public User() {
    super();
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  @Override
  public String toString() {
    return "Customer [username=" + username + ", password=" + password + "]";
  }
}
