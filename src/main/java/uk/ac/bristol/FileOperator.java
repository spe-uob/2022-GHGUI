package uk.ac.bristol;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileOperator {

  public static List<User> userList;

  public static void write(User user) {
    File fileDest = new File("data.txt");
    BufferedWriter bw = null;
    try {
      bw = new BufferedWriter(new FileWriter(fileDest, true));
      bw.append(user.getUsername() + "/" + user.getPassword() + "\n");
      bw.flush();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      try {
        if (null != bw) {
          bw.close();
        }
      } catch (Exception e) {
      }
    }
  }

  public static void read() {
    userList = new ArrayList<>();
    File src = new File("data.txt");
    BufferedReader reader = null;
    try {
      reader = new BufferedReader(new FileReader(src));
      String line = null;
      while ((line = reader.readLine()) != null) {
        String[] strList = line.split("/");

        String userName = strList[0];
        String password = strList[1];
        User user = new User(userName, password);
        userList.add(user);
      }

    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      try {
        if (null != reader) {
          reader.close();
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
}
