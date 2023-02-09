package uk.ac.bristol;

import lombok.Getter;
import lombok.Setter;

public class User {
  @Getter @Setter public String username; // 账号
  @Getter @Setter public String password; // 密码

  public User(String username, String password) {
    this.username = username;
    this.password = password;
  }

  @Override
  public String toString() {
    return "Customer [username=" + username + ", password=" + password + "]";
  }
}
