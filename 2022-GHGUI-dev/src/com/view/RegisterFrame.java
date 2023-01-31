package com.view;

import com.entity.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Random;

public class RegisterFrame {
    private JFrame window;
    private JPanel body;
    private JLabel title,usernameLabel,passwordLabel,emailLabel,passwordLabel2,codeLabel;
    private JTextField usernameField,codeField;
    private JPasswordField passwordField;
    private JPasswordField passwordField2;
    private JTextField emailField;
    private JButton register;
    private JLabel hint,back;

    public RegisterFrame(){
        User user = new User();
        window = new JFrame("register");
        body = new JPanel(null);
        title = new JLabel("Create an account to GHGUI");
        usernameLabel = new JLabel("Enter your username");
        passwordLabel = new JLabel("Enter your password");
        passwordLabel2= new JLabel("Confirm your password");
        passwordField2 = new JPasswordField();
        back = new JLabel("Return to login");
        String generateCode = generateCode();
        codeLabel = new JLabel("Enter verification code (" + generateCode + ")");
        codeField = new JTextField();
        emailLabel = new JLabel("Enter your email");
        usernameField = new JTextField();
        passwordField = new JPasswordField();
        emailField = new JTextField();
        register = new JButton("Register");
        title.setFont(new Font("Console",Font.PLAIN,28));
        title.setBounds(60,50,400,50);
        body.add(title);
        hint = new JLabel();
        hint.setFont(new Font("Console",Font.PLAIN,20));
        hint.setForeground(Color.red);
        hint.setBounds(60,10,400,50);
        body.add(hint);
        usernameLabel.setBounds(50,100,300,50);
        usernameLabel.setFont(new Font("Console",Font.PLAIN,18));
        body.add(usernameLabel);
        usernameField.setBounds(50,150,380,30);
        usernameField.setFont(new Font("Console",Font.PLAIN,18));
        body.add(usernameField);
        emailLabel.setBounds(50,200,300,50);
        emailLabel.setFont(new Font("Console",Font.PLAIN,18));
        body.add(emailLabel);
        emailField.setBounds(50,250,380,30);
        emailField.setFont(new Font("Console",Font.PLAIN,18));
        body.add(emailField);
        passwordLabel.setFont(new Font("Console",Font.PLAIN,18));
        passwordLabel.setBounds(50,300,300,50);
        body.add(passwordLabel);
        passwordField.setFont(new Font("Console",Font.PLAIN,18));
        passwordField.setBounds(50,350,380,30);
        body.add(passwordField);
        passwordLabel2.setFont(new Font("Console",Font.PLAIN,18));
        passwordLabel2.setBounds(50,400,300,50);
        body.add(passwordLabel2);
        passwordField2.setFont(new Font("Console",Font.PLAIN,18));
        passwordField2.setBounds(50,450,380,30);
        body.add(passwordField2);
        codeLabel.setFont(new Font("Console",Font.PLAIN,18));
        codeLabel.setBounds(50,500,300,50);
        body.add(codeLabel);
        codeField.setFont(new Font("Console",Font.PLAIN,18));
        codeField.setBounds(50,550,380,30);
        body.add(codeField);
        register.setBounds(50,610,380,30);
        register.setFont(new Font("Console",Font.PLAIN,18));
        body.add(register);
        back.setBounds(350,660,200,30);
        back.setFont(new Font("Console",Font.PLAIN,14));
        back.setForeground(Color.RED);
        body.add(back);
        window.add(body);
        window.setVisible(true);
        window.setSize(500,750);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);

        register.addActionListener(e -> {
            String username = usernameField.getText();
            String email = emailField.getText();
            String pwd1 = new String(passwordField.getPassword());
            String pwd2 = new String(passwordField2.getPassword());
            String code = codeField.getText();
            if (username.equals("") || email.equals("") || pwd1.equals("") || pwd2.equals("")){
                hint.setText("Please complete!");
                return;
            }
            if (!pwd1.equals(pwd2)){
                hint.setText("The two password entries are inconsistent!");
                return;
            }
            if (!code.equals(generateCode)){
                hint.setText("Verification code error!");
                return;
            }
            user.setEmail(email);
            user.setPassword(pwd1);
            user.setUsername(username);
            hint.setText("register succeed!");
        });

        back.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new LoginFrame(user);
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

    private String generateCode() {
        Random random = new Random();   // 随机数对象
        String[] chars = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"}; // 验证码池
        StringBuilder code = new StringBuilder();   // 验证码
        for (int i = 0; i < 4; i++) {   // 通过for循环四次，来随机生成一个验证码
            code.append(chars[random.nextInt(chars.length)]);   // 每次append添加到code后面
        }
        return code.toString();
    }
}
