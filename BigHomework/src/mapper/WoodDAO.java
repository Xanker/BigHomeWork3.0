package mapper;

import products.Fruit;
import products.Wood;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class WoodDAO extends ProductDAO<Wood> {
    public WoodDAO() throws SQLException {
        super();
    }

    public void insert(Wood wood) throws SQLException {

        insertProductTable(wood);
        String sql = "INSERT INTO wood (id, wood_type, origin, grade, length, width, height, purchday,price,stock,name,unitprice) VALUES (?,?,?,?,?,?,?,?,?,?,?,?) ";


        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, wood.getID());
        ps.setString(2, wood.getType());
        ps.setString(3, wood.getOrigin());
        ps.setDouble(4, wood.getWaterness());
        ps.setDouble(5, wood.getLength());
        ps.setDouble(6, wood.getWidth());
        ps.setDouble(7, wood.getThickness());
        ps.setDate(8, Date.valueOf(wood.getDate()));
        ps.setDouble(9,wood.getTotalPrice());
        ps.setInt(10, wood.getStock());
        ps.setString(11, wood.getName());
        ps.setDouble(12,wood.getunitprice());

        ps.executeUpdate();
    }

    @Override
    public boolean update(Wood wood) throws SQLException {
        // 1. 更新product表
        boolean productUpdated = updateTable(wood);

        // 2. 更新fruit表
        boolean fruitUpdated = update1(wood);

        return productUpdated && fruitUpdated;
    }

    public boolean updateTable(Wood wood) throws SQLException {
        String sql = "UPDATE product SET name = ?, type = ?, price = ?, description = ?, stock = ? WHERE id = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, wood.getName());
            ps.setString(2, wood.getType());
            ps.setDouble(3, wood.getUnitPrice());
            ps.setString(4, wood.getDescription());
            ps.setInt(5, wood.getStock());
            ps.setInt(6, wood.getID());


            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        }
    }

    public boolean update1(Wood wood) throws SQLException {

        String url1 = "UPDATE wood SET wood_type = ? ,origin = ?, grade = ?, length = ?, width = ? , height = ?, purchday = ?,price =? ,stock = ? , name = ?, unitprice = ? WHERE id = ?";
        PreparedStatement ps = connection.prepareStatement(url1);


        ps.setString(1, wood.getType());
        ps.setString(2,wood.getOrigin());
        ps.setDouble(3, wood.getWaterness());
        ps.setDouble(4, wood.getLength());
        ps.setDouble(5, wood.getWidth());
        ps.setDouble(6, wood.getThickness());
        ps.setDate(7, java.sql.Date.valueOf(wood.getDate()));
        ps.setDouble(8, wood.getTotalPrice());
        ps.setInt(9, wood.getStock());
        ps.setString(10, wood.getName());
        ps.setDouble(11, wood.getunitprice());
        ps.setInt(12, wood.getID());

        int rowsAffected = ps.executeUpdate();
        return rowsAffected > 0;
    }

    public List<Wood> getAllWoods() throws SQLException {
        List<Wood> woods = new ArrayList<>();
        String sql = "SELECT * FROM wood"; // 直接查询wood表

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);

             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Wood wood = new Wood();
                // 设置基础属性
                wood.setID(rs.getInt("id"));

                wood.setStock(rs.getInt("stock"));
                wood.setOrigin(rs.getString("origin"));
                wood.setDate(rs.getDate("purchday").toLocalDate());
                // 设置木材特有属性
                wood.setName(rs.getString("wood_type"));
                wood.setOrigin(rs.getString("origin"));
                wood.setWaterness(Double.parseDouble(rs.getString("grade")));
                wood.setLength(rs.getDouble("length"));
                wood.setWidth(rs.getDouble("width"));
                wood.setThickness(rs.getDouble("height"));
                wood.setName(rs.getString("name"));
                wood.setunitprice(rs.getDouble("unitprice"));
                woods.add(wood);
            }
        } catch (SQLException e) {

            throw new SQLException("木材查询异常: " + e.getMessage());
        }
        return woods;
    }
    public String getType()
    {
        return "Wood";
    }

}