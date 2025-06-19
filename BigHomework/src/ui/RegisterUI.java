package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class RegisterUI extends JFrame{
    private JTextField name;
    private JTextField MemberuserID;
    private JTextField MemberuserPassword;
    private String sname;
    private String userID;
    private String userPassword;

    public RegisterUI()
    {
          setTitle("注册界面");
          setSize(500, 500);
          setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
          setLocationRelativeTo(null);

          Container c = getContentPane();
          c.setLayout(new GridLayout(4,2,10,10));
          name = new JTextField();
          c.add(new JLabel("姓名:"));
          c.add(name);

          MemberuserID = new JTextField();
          c.add(new JLabel("账号:"));
          c.add(MemberuserID);

          MemberuserPassword = new JTextField();
          c.add(new JLabel("密码:"));
          c.add(MemberuserPassword);


          JButton register = new JButton("注册");
          register.addActionListener(new ActionListener()
          {
              @Override
              public void actionPerformed(ActionEvent e) {
                  sname = name.getText();
                  userID = MemberuserID.getText();
                  userPassword = MemberuserPassword.getText();
                  try {
                      LoadInfo loadInfo = new LoadInfo();
                      Members members = new Members();
                      if(!sname.equals("")&&!userID.equals("")&&!userPassword.equals(""))
                      {
                          members.setName(sname);
                          members.setpassword(userPassword);
                          members.setCustomersID(Integer.parseInt(userID));
                          loadInfo.inserInfoTable(members);
                          JOptionPane.showMessageDialog(new JOptionPane(), "注册成功");
                      }
                      else {
                          JOptionPane.showMessageDialog(new JOptionPane(), "输入信息错误");
                      }

                  } catch (SQLException ex) {
                      throw new RuntimeException(ex);
                  }


              }
          });
          c.add(register);

          JButton back = new JButton("返回");
          back.addActionListener(new ActionListener() {
              @Override
              public void actionPerformed(ActionEvent e) {
                  return;
              }
          });

    }
}
