package uk.ac.bristol.controllers;

import java.io.File;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.eclipse.jgit.util.FS;
import uk.ac.bristol.model.LoginHTTPSInfo;
import uk.ac.bristol.model.LoginSSHInfo;
import uk.ac.bristol.model.LoginTokenInfo;
import uk.ac.bristol.util.AesEncryptionUtil;
import uk.ac.bristol.util.GitInfo;
import uk.ac.bristol.util.JgitUtil;
import uk.ac.bristol.util.errors.AlertBuilder;
import uk.ac.bristol.util.errors.ErrorHandler;
import uk.ac.bristol.util.file.ObjectToFileUtil;

/**
 * The FXML controller for the git login menu.
 */
public class LoginController {

    /**
     * The root pane for this controller.
     */
    @FXML
    private VBox root;

    /**
     * ID for token login.
     */
    @FXML
    private TextField tokenID;
    /**
     * The box where a user can enter a GH Token.
     */
    @FXML
    private TextField token;

    /**
     * ID for ssh login.
     */
    @FXML
    private TextField sshID;
    /**
     * The box where a user can enter a path to an ssh key.
     */
    @FXML
    private TextField keyPath;
    /**
     * The box where a user can enter a passphrase for an ssh key.
     */
    @FXML
    private PasswordField passphrase;

    /**
     * ID for https login.
     */
    @FXML
    private TextField httpsID;
    /**
     * The box where a user can enter their username.
     */
    @FXML
    private TextField username;
    /**
     * The box where a user can enter a password.
     */
    @FXML
    private PasswordField password;


    /**
     * Function to run when the user clicks the submit button.
     *
     * @param e The event associated with clicking the button
     */
    @FXML
    private void addCredentials(final Event e) {
        final Button source = (Button) e.getSource();
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Add Account");
        dialog.setHeaderText("account key: ");
        dialog.setGraphic(null);
        dialog.showAndWait().ifPresent(res -> {


            if (dialog.getEditor().getText().length() < 16) {
                AlertBuilder.warn("key length must be 16 ").showAndWait();
                return;
            }

            switch (source.getId()) {
                case "TokenLogin":
                    if (tokenID.getText().equals("") || token.getText().equals("")) {
                        AlertBuilder.warn("token info can not be null ").show();
                        return;
                    }
                    LoginTokenInfo loginTokenInfo = null;
                    try {
                        loginTokenInfo = new LoginTokenInfo(dialog.getEditor().getText(), tokenID.getText(), token.getText());
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                    ObjectToFileUtil.saveToken(loginTokenInfo);
                    AlertBuilder.info("save token info successful!").show();
                    GitInfo.addToken(tokenID.getText(), token.getText());
                    break;
                case "SSHLogin":
                    if (sshID.getText().equals("") || keyPath.getText().equals("") ||
                            passphrase.getText().equals("")) {
                        AlertBuilder.warn("ssh info can not be null ").show();
                        return;
                    }
                    LoginSSHInfo sshInfo = null;
                    try {
                        sshInfo = new LoginSSHInfo(dialog.getEditor().getText(), sshID.getText(), keyPath.getText(), passphrase.getText());
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                    ObjectToFileUtil.saveSSH(sshInfo);
                    AlertBuilder.info("save ssh info successful!").show();
                    GitInfo.addSSH(sshID.getText(), keyPath.getText(), passphrase.getText());
                    break;
                case "HTTPSLogin":
                    if (httpsID.getText().equals("") || username.getText().equals("") ||
                            password.getText().equals("")) {
                        AlertBuilder.warn("https info can not be null ").show();
                        return;
                    }
                    LoginHTTPSInfo httpsInfo = null;
                    try {
                        httpsInfo = new LoginHTTPSInfo(dialog.getEditor().getText(), httpsID.getText(), username.getText(), password.getText());
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                    ObjectToFileUtil.saveHttps(httpsInfo);
                    AlertBuilder.info("save http info successful!").show();
                    GitInfo.addHTTPS(
                            httpsID.getText(), username.getText(), password.getText());
                    break;
            }


        });

        switch (source.getId()) {
            case "TokenLogin":
                GitInfo.addToken(tokenID.getText(), token.getText());
                break;
            case "SSHLogin":
                GitInfo.addSSH(sshID.getText(), keyPath.getText(), passphrase.getText());
                break;
            case "HTTPSLogin":
                GitInfo.addHTTPS(
                        httpsID.getText(), username.getText(), password.getText());
                break;
        }

        final Stage stage = (Stage) root.getScene().getWindow();
        stage.close();
    }






    /**
     * A function to laod ssh info by key
     */
    @FXML
    void loadSSH(ActionEvent event) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Load SSHList");
        dialog.setHeaderText("account key: ");
        dialog.setGraphic(null);
        dialog.showAndWait().ifPresent(res -> {


            if (dialog.getEditor().getText().length() < 16) {
                AlertBuilder.warn("key length must be 16 ").showAndWait();
                return;
            }

            boolean accountIsExist = false;
            List<LoginSSHInfo> sshInfos = ObjectToFileUtil.loadSSHList();
            for (int i = 0; i < sshInfos.size(); i++) {
                if (sshInfos.get(i).getKey().equals(dialog.getEditor().getText())) {

                    GitInfo.addSSH(sshInfos.get(i).getId(),sshInfos.get(i).getPath(),sshInfos.get(i).getDecryptPassphrase());
                    accountIsExist = true;
                }
            }


            if (accountIsExist == false) {
                AlertBuilder.warn("this key no have any token info!").show();
            } else {
                AlertBuilder.info("load sshList successful!").show();
                final Stage stage = (Stage) root.getScene().getWindow();
                stage.close();
            }


        });

    }
    /**
     * A function to laod https info by key
     */
    @FXML
    void loadHttps(ActionEvent event) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Load HttpsList");
        dialog.setHeaderText("account key: ");
        dialog.setGraphic(null);
        dialog.showAndWait().ifPresent(res -> {


            if (dialog.getEditor().getText().length() < 16) {
                AlertBuilder.warn("key length must be 16 ").showAndWait();
                return;
            }

            boolean accountIsExist = false;
            List<LoginHTTPSInfo> httpsInfos = ObjectToFileUtil.loadHttpsList();
            for (int i = 0; i < httpsInfos.size(); i++) {
                if (httpsInfos.get(i).getKey().equals(dialog.getEditor().getText())) {
                    GitInfo.getHttpAuth().put(httpsInfos.get(i).getId(), new UsernamePasswordCredentialsProvider(httpsInfos.get(i).getUserName(), httpsInfos.get(i).getDecryptPassword()));
                    accountIsExist = true;
                }
            }


            if (accountIsExist == false) {
                AlertBuilder.warn("this key no have any token info!").show();
            } else {
                AlertBuilder.info("load tokenList successful!").show();
                final Stage stage = (Stage) root.getScene().getWindow();
                stage.close();
            }


        });

    }

    /**
     * A function to laod tokenList by key
     */
    @FXML
    void loadToken(ActionEvent event) {

        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Load TokenList");
        dialog.setHeaderText("account key: ");
        dialog.setGraphic(null);
        dialog.showAndWait().ifPresent(res -> {


            if (dialog.getEditor().getText().length() < 16) {
                AlertBuilder.warn("key length must be 16 ").showAndWait();
                return;
            }

            boolean accountIsExist = false;
            List<LoginTokenInfo> tokenInfos = ObjectToFileUtil.loadTokenList();
            for (int i = 0; i < tokenInfos.size(); i++) {
                if (tokenInfos.get(i).getKey().equals(dialog.getEditor().getText())) {
                    GitInfo.getHttpAuth().put(tokenInfos.get(i).getId(), new UsernamePasswordCredentialsProvider(tokenInfos.get(i).getDecryptToken(), ""));
                    accountIsExist = true;
                }
            }


            if (accountIsExist == false) {
                AlertBuilder.warn("this key no have any token info!").show();
            } else {
                AlertBuilder.info("load tokenList successful!").show();
                final Stage stage = (Stage) root.getScene().getWindow();
                stage.close();
            }


        });

    }


    /**
     * A function to open the directory dialog for selecting a key to use.
     */
    @FXML
    private void browse() {
        final FileChooser fileChooser = new FileChooser();
        final File home = FS.detect().userHome();
        final Optional<File> sshDir =
                Stream.of(home.listFiles()).filter(file -> file.getName().equals(".ssh")).findAny();
        fileChooser.setInitialDirectory(sshDir.orElse(home));
        final File selectedFile = fileChooser.showOpenDialog(root.getScene().getWindow());

        if (selectedFile == null) {
            return;
        }
        keyPath.setText(selectedFile.getAbsolutePath());
    }
}
