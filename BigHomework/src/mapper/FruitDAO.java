package mapper;

import products.Fruit;


import products.Fruit;
import products.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static com.mysql.cj.conf.PropertyKey.logger;
import static java.sql.DriverManager.getConnection;

public class FruitDAO extends ProductDAO<Fruit> {
    public FruitDAO() throws SQLException {
        super();
    }

    @Override
    public void insert(Fruit fruit) throws SQLException {
        //插入Product表
        insertProductTable(fruit);

        String sql = "INSERT INTO fruit(id,color, weight, unit, origin, ripeness,purchday,unitPrice,totalPrice,name) VALUES(?, ?, ?, ?, ?, ?, ?, ?,?,?)";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1,fruit.getID());
        ps.setString(2, fruit.getColor());
        ps.setDouble(3, fruit.getWeight());
        ps.setString(4, fruit.getUnit());
        ps.setString(5, fruit.getOrigin());
        ps.setString(6, fruit.getMaturity());
        java.sql.Date sqlDate = java.sql.Date.valueOf(fruit.getDate());
        ps.setDate(7,sqlDate);
        ps.setDouble(8, fruit.getUnitPrice());
        ps.setDouble(9, fruit.getTotalPrice());
        ps.setString(10, fruit.getName());
        ps.executeUpdate();
        ps.close();
    }

    @Override
    public boolean update(Fruit fruit) throws SQLException {
        // 1. 更新product表
        boolean productUpdated = updateTable(fruit);

        // 2. 更新fruit表
        boolean fruitUpdated = update1(fruit);

        return productUpdated && fruitUpdated;
    }

    public boolean updateTable(Fruit fruit) throws SQLException {
        String sql = "UPDATE product SET name = ?, type = ?, price = ?, description = ?, stock = ? WHERE id = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, fruit.getName());
            ps.setString(2, fruit.getType());
            ps.setDouble(3, fruit.getTotalPrice());
            ps.setString(4, fruit.getDescription());
            ps.setInt(5, fruit.getStock());
            ps.setInt(6, fruit.getID());

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        }
    }

    public boolean update1(Fruit fruit) throws SQLException {

        String url1 = "UPDATE fruit SET color = ? ,weight = ?, unit = ?, origin = ?, ripeness = ? ,purchday = ? ,unitPrice = ?,totalPrice = ? , name = ?WHERE id = ?";
        PreparedStatement ps = connection.prepareStatement(url1);


        ps.setString(1, fruit.getColor());
        ps.setDouble(2, fruit.getWeight());
        ps.setString(3, fruit.getUnit());
        ps.setString(4, fruit.getOrigin());
        ps.setString(5, fruit.getMaturity());
        ps.setDate(6, java.sql.Date.valueOf(fruit.getDate()));
        ps.setDouble(7, fruit.getUnitPrice());
        ps.setDouble(8, fruit.getTotalPrice());
        ps.setString(9, fruit.getName());
        ps.setInt(10, fruit.getID());

        int rowsAffected = ps.executeUpdate();
        return rowsAffected > 0;
    }

    public Fruit findById(int id) throws SQLException {
        String sql = "SELECT p.*, f.color, f.weight, f.unit, f.origin, f.ripeness, "
                + "f.purchday, f.unitPrice, f.totalPrice "
                + "FROM product p "
                + "JOIN fruit f ON p.id = f.id "
                + "WHERE p.id = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (var rs = ps.executeQuery()) {
                if (rs.next()) {
                    // 创建并填充Fruit对象
                    Fruit fruit = new Fruit(
                            rs.getString("name"),
                            id,
                            rs.getString("color"),
                            rs.getDouble("weight"),
                            rs.getString("unit"),
                            rs.getString("origin"),
                            rs.getDate("purchday").toLocalDate(),
                            rs.getDouble("unitPrice")
                    );

                    // 设置其他属性
                    fruit.setType(rs.getString("type"));
                    fruit.setDescription(rs.getString("description"));

                    return fruit;
                }
            }
        }
        return null;
    }

    public List<Fruit> getAllFruits() throws SQLException {
        List<Fruit> fruits = new ArrayList<>();
        String sql = "SELECT * FROM fruit"; // 直接查询fruit表

        try (
                Connection conn = getConnection();  // 使用连接池获取连接
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Fruit fruit = new Fruit();

                // 设置基础属性
                fruit.setID(rs.getInt("id"));
                fruit.setName(rs.getString("name"));
                fruit.setUnitPrice(rs.getDouble("unitPrice"));


                // 设置水果特有属性
                fruit.setColor(rs.getString("color"));
                fruit.setWeight(rs.getDouble("weight"));
                fruit.setUnit(rs.getString("weight"));
                fruit.setOrigin(rs.getString("origin"));

                fruit.setDate(rs.getDate("purchday").toLocalDate());

                fruits.add(fruit);
            }
        } catch (SQLException e) {
            // 记录日志并重新抛出异常
            throw new SQLException("数据库查询错误: " + e.getMessage());
        }
        return fruits;
    }
    public String getType()
    {
        return "Fruit";
    }

}