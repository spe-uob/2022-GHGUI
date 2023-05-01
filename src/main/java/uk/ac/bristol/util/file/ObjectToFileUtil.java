package uk.ac.bristol.util.file;



import uk.ac.bristol.model.LoginHTTPSInfo;
import uk.ac.bristol.model.LoginSSHInfo;
import uk.ac.bristol.model.LoginTokenInfo;

import java.io.BufferedInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Provides methods for serializing Java objects into files and deserializing Java objects from a file.
 */
public class ObjectToFileUtil {


    /**
     * Writes an object to the specified file.
     *
     * @param path filePath
     * @param t    Object
     * @param <T>
     */
    public static <T> void write(String path, T t) {
        List<T> ts = new ArrayList<T>();
        ts.add(t);
        writeList(path, ts);
    }


    /**
     * Writes an object to the specified file (overwrite the file).
     *
     * @param path filePath
     * @param t    Object
     * @param <T>
     */
    public static <T> void writeByOverWrite(String path, T t) {
        List<T> ts = new ArrayList<T>();
        ts.add(t);
        writeListByOverWrite(path, ts);
    }




    /**
     *  Writes multiple objects to the specified file.
     * @param path filePath
     * @param ts    Object
     * @param <T>
     */
    public static <T> void writeList(String path, List<T> ts) {
        ObjectOutputStream out = null;
        try {
            File file = new File(path);


            if (file.exists() == false) {
                file.createNewFile();
            }

            FileOutputStream fos = new FileOutputStream(file, true);
            if (file.length() < 1) {
                out = new ObjectOutputStream(fos);
            } else {
                out = new ObjectAppendOutputStream(fos);
            }
            for (T t : ts) {
                out.writeObject(t);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * Writes multiple objects to the specified file (overwrite the file).
     * @param path filePath
     * @param ts    Object
     * @param <T>
     */
    public static <T> void writeListByOverWrite(String path, List<T> ts) {
        ObjectOutputStream out = null;
        try {
            File file = new File(path);

            if (file.exists() == false) {
                file.createNewFile();
            }

            FileOutputStream fos = new FileOutputStream(file);
            if (file.length() < 1) {
                out = new ObjectOutputStream(fos);
            } else {
                out = new ObjectAppendOutputStream(fos);
            }

            for (T t : ts) {
                out.writeObject(t);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



    /**
     * Reads an object from the specified file.
     * @param path
     * @return
     * @param <T>
     */
    public static <T> T readFirst(String path) {
        List<T> temp = readList(path);
        if (temp == null || temp.size() == 0) {
            return null;
        }

        return temp.get(0);
    }




    /**
     * Reads all objects from the specified file.
     * @param path
     * @return
     * @param <T>
     */
    public static <T> List<T> readList(String path) {
        List<T> result = new ArrayList<T>();
        ObjectInputStream in = null;
        try {
            in = new ObjectInputStream(new BufferedInputStream(new FileInputStream(path)));
            Object temp = null;
            while ((temp = in.readObject()) != null) {
                T t = (T) temp;
                result.add(t);
            }
        } catch (EOFException e) {
            // Do nothing
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }


    public static List<LoginTokenInfo> loadTokenList() {
        List<Object> objects = ObjectToFileUtil.readList("token.csv");
        List<LoginTokenInfo> tokenInfos = new ArrayList<LoginTokenInfo>();
        for (int i = 0; i < objects.size(); i++) {
            tokenInfos.add((LoginTokenInfo) objects.get(i));
        }
        return tokenInfos;
    }


    public static List<LoginHTTPSInfo> loadHttpsList() {
        List<Object> objects = ObjectToFileUtil.readList("https.csv");
        List<LoginHTTPSInfo> httpsInfos = new ArrayList<LoginHTTPSInfo>();
        for (int i = 0; i < objects.size(); i++) {
            httpsInfos.add((LoginHTTPSInfo) objects.get(i));
        }
        return httpsInfos;
    }

    public static List<LoginSSHInfo> loadSSHList() {
        List<Object> objects = ObjectToFileUtil.readList("sshs.csv");
        List<LoginSSHInfo> loginSSHInfos = new ArrayList<LoginSSHInfo>();
        for (int i = 0; i < objects.size(); i++) {
            loginSSHInfos.add((LoginSSHInfo) objects.get(i));
        }
        return loginSSHInfos;
    }

    public static void saveToken(LoginTokenInfo tokenInfo) {
        write("token.csv",tokenInfo);
    }

    public static void saveHttps(LoginHTTPSInfo httpsInfo) {
        write("https.csv",httpsInfo);
    }


    public static void saveSSH(LoginSSHInfo loginSSHInfo) {
        write("sshs.csv",loginSSHInfo);
    }
}