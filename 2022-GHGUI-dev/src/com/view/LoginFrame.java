package com.view;

import com.entity.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class LoginFrame {
    private JFrame window;
    private JPanel body;
    private JLabel title,usernameLabel,passwordLabel;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginBtn;
    private JLabel register,hint;

    public LoginFrame(User user){
        window = new JFrame("login");
        body = new JPanel(null);
        title = new JLabel("Sign in to GHGUI");
        usernameLabel = new JLabel("Username or email address");
        passwordLabel = new JLabel("Password");
        usernameField = new JTextField();
        passwordField = new JPasswordField();
        loginBtn = new JButton("Sign in");
        hint = new JLabel("new to GHGUI ?");
        register = new JLabel("Create an account");
        title.setFont(new Font("Console",Font.PLAIN,30));
        title.setBounds(120,30,400,50);
        body.add(title);
        usernameLabel.setBounds(50,100,300,50);
        usernameLabel.setFont(new Font("Console",Font.PLAIN,18));
        body.add(usernameLabel);
        usernameField.setBounds(50,150,380,30);
        usernameField.setFont(new Font("Console",Font.PLAIN,18));
        body.add(usernameField);
        passwordLabel.setBounds(50,200,300,50);
        passwordLabel.setFont(new Font("Console",Font.PLAIN,18));
        body.add(passwordLabel);
        passwordField.setBounds(50,250,380,30);
        passwordField.setFont(new Font("Console",Font.PLAIN,18));
        body.add(passwordField);
        loginBtn.setBounds(50,320,380,30);
        loginBtn.setFont(new Font("Console",Font.PLAIN,18));
        body.add(loginBtn);
        hint.setFont(new Font("Console",Font.PLAIN,18));
        hint.setBounds(70,400,400,50);
        body.add(hint);
        register.setFont(new Font("Console",Font.PLAIN,18));
        register.setBounds(240,400,400,50);
        register.setForeground(Color.red);
        body.add(register);
        window.add(body);
        window.setVisible(true);
        window.setSize(500,550);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);

        loginBtn.addActionListener(e -> {
            try {
                String usernameFieldText = usernameField.getText();
                String password = new String(passwordField.getPassword());
                if (user.getUsername().equals(usernameFieldText)
                        || user.getEmail().equals(usernameFieldText)
                        && user.getPassword().equals(password)){
                    JOptionPane.showMessageDialog(null,"Login succeeded.");
                }else {
                    JOptionPane.showMessageDialog(null,"Incorrect username or password. ");
                }
            }catch (NullPointerException exception){
                JOptionPane.showMessageDialog(null,"Please register first.");
            }
        });

        register.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new RegisterFrame();
                window.setVisible(false);
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
    }

    public static void main(String[] args) {
        new LoginFrame(null);
    }

}
