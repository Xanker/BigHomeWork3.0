package ui;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LoadInfo {
     protected Connection con;
       public LoadInfo() throws SQLException {
           String sql = "jdbc:mysql://127.0.0.1/jj";
           String name = "root";
           String password = "2856208614";
           con = DriverManager.getConnection(sql, name, password);
       }
    protected void inserInfoTable(Members members) throws SQLException {
        String sql = "INSERT INTO memberinfo(ID, name, password) VALUES(?,?,?)";
        PreparedStatement ps = con.prepareStatement(sql);

        ps.setInt(1, members.getCustomersID());
        ps.setString(2, members.getName());
        ps.setString(3, String.valueOf(members.getpassword())); // 转换为字符串

        ps.executeUpdate();
        ps.close();
    }
       public int[] loadID() throws SQLException {
           String sql =  "select * from memberinfo";
           PreparedStatement ps = con.prepareStatement(sql);
           ResultSet rs = ps.executeQuery();
           int[] id = null;
           int i = 1;
           while (rs.next()) {
               id = new int[20];
               id[i] = rs.getInt("ID");
               i = i + 1;
           }
           return id;
       }
    public String[] loadName() throws SQLException {
        String sql = "SELECT name FROM memberinfo";
        PreparedStatement ps = con.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        // 使用ArrayList动态存储结果
        List<String> names = new ArrayList<>();

        while (rs.next()) {
            names.add(rs.getString("name"));
        }

        // 转换为数组返回
        return names.toArray(new String[0]);
    }

    public String[] loadPassword() throws SQLException {
        String sql = "SELECT password FROM memberinfo";
        PreparedStatement ps = con.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        List<String> passwords = new ArrayList<>();

        while (rs.next()) {
            // 正确获取字符串类型的密码
            passwords.add(rs.getString("password"));
        }

        return passwords.toArray(new String[0]);
    }
}

