package uk.ac.bristol;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.experimental.UtilityClass;

/** A class for performing file operations. */
// CHECKSTYLE:IGNORE HideUtilityClassConstructorCheck 1
@UtilityClass
public class FileOperator {

  /** Static list of all available logins. */
  @Getter private static List<User> userList;

  /**
   * Write to the config file.
   *
   * @param user The user's data to write to the password file
   */
  public static void write(final User user) {
    final File fileDest = new File("data.txt");
    try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileDest, true))) {
      bw.append(user.getUsername() + "/" + user.getPassword() + "\n");
      bw.flush();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /** Read user data from the config file. */
  public static void read() {
    userList = new ArrayList<>();
    final File src = new File("data.txt");
    try (BufferedReader reader = new BufferedReader(new FileReader(src))) {
      String line = null;
      while ((line = reader.readLine()) != null) {
        final String[] strList = line.split("/");

        final String userName = strList[0];
        final String password = strList[1];
        final User user = new User(userName, password);
        userList.add(user);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
