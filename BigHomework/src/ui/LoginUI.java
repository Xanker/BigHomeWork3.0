package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class LoginUI extends JFrame {
    // 用户名、密码输入框
    private JTextField useridField;
    private JPasswordField passwordField;
    private String memberusername[] = new String[20], memberpassword[] = new String[20];
    public LoginUI() throws SQLException {
        load();
        setTitle("登录界面");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // 居中显示

        // 布局
        Container container = getContentPane();
        container.setLayout(new GridLayout(5, 4, 11, 12));

        // 添加组件
        container.add(new JLabel("用户ID:"));
        useridField = new JTextField();
        container.add(useridField);

        container.add(new JLabel("密码:"));
        passwordField = new JPasswordField();
        container.add(passwordField);

        JButton shop = new JButton("默认购物车");
        shop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Members members = new Members();
                ShoppingCartUI shoppingCartUI = null;
                try {
                    shoppingCartUI = new ShoppingCartUI(members);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
                shoppingCartUI.setVisible(true);
            }
        });
        container.add(shop);
        JButton loginBtn = new JButton("登录");
        // 后续你可在此给按钮绑定事件，比如校验登录信息
        loginBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){

                String username = useridField.getText();
                String password = String.valueOf(passwordField.getPassword());
                if (username.equals("admin") && password.equals("admin")) {
                    JOptionPane.showMessageDialog(new JOptionPane(), "你将以管理员身份登录系统");
                    ManagerUI managerUI = null;
                    managerUI = new ManagerUI();
                    managerUI.setVisible(true);
                }
                else if (check()) {
                    JOptionPane.showMessageDialog(new JOptionPane(), "你将以顾客身份登录系统");
                    Members members = new Members();
                    ShoppingCartUI shoppingCartUI = null;
                    try {
                        shoppingCartUI = new ShoppingCartUI(members);
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                    shoppingCartUI.setVisible(true);
                }
                else {
                    JOptionPane.showMessageDialog(new JOptionPane(), "请输入正确的信息");
                }

            }
        });
        container.add(loginBtn);

        JButton registerBtn = new JButton("注册");
        // 同理，注册逻辑后续补充
        registerBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RegisterUI registerUI = new RegisterUI();
                registerUI.setVisible(true);
                try {
                    load();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        container.add(registerBtn);
    }

    // 启动登录界面
    public static void showLogin() throws SQLException {
        LoginUI loginUI = new LoginUI();
        loginUI.setVisible(true);
    }

    // 获取用户名
    public String getUsername() {
        return useridField.getText();
    }

    // 获取密码（注意密码是 char[] ，需转成字符串处理）
    public String getPassword() {
        return new String(passwordField.getPassword());
    }

    public void load() throws SQLException {
        LoadInfo loadInfo = new LoadInfo();
        memberusername = loadInfo.loadName();
        memberpassword = loadInfo.loadPassword();
        for(int i = 0; i < memberusername.length; i++) {
            System.out.println(memberusername[i]);
        }

    }
    public boolean check()
    {
        for (int i = 0; i < 20; i++) {
            if(getUsername().equals(memberusername[i]) && getPassword().equals(memberpassword[i]))
            {
                return true;
            }
        }
        return false;
    }
}