package mapper;

import products.Herb;

import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class HerbDAO extends ProductDAO<Herb> {
    public HerbDAO() throws SQLException {
        super();
    }

    public void insert(Herb herb) throws SQLException {
        insertProductTable(herb);

        String sql = "insert into herb(id, name, origin, pSeason, pMonth,purchday, property,TotalPrice,stock,unitPrice) values(?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1,herb.getID());
        ps.setString(2,herb.getName());
        ps.setString(3,herb.getOrigin());
        ps.setString(4,herb.getSeason());
        ps.setInt(5,herb.getMonth());
        ps.setDate(6,Date.valueOf(herb.getPurchday()));
        ps.setString(7,herb.getProperty());
        ps.setDouble(8,herb.getTotalPrice());
        ps.setInt(9, herb.getStock());
        ps.setDouble(10,herb.getUnitPrice());
        ps.executeUpdate();
        ps.close();
    }

    public boolean update(Herb herb) throws SQLException {
        // 1. 更新product表
        boolean productUpdated = updateProductTable(herb);

        // 2. 更新herb表
        boolean herbUpdated = updateHerbTable(herb);

        return productUpdated && herbUpdated;
    }

    // 更新product表的单独方法
    private boolean updateProductTable(Herb herb) throws SQLException {
        String sql = "UPDATE product SET name = ?, type = ?, price = ?, description = ?, stock = ? WHERE id = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, herb.getName());
            ps.setString(2, herb.getType());
            ps.setDouble(3, herb.getUnitPrice());
            ps.setString(4, herb.getDescription());
            ps.setInt(5, herb.getStock());
            ps.setInt(6, herb.getID());

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        }
    }

    // 更新herb表的单独方法
    public boolean updateHerbTable(Herb herb) throws SQLException {
        String sql = "UPDATE herb SET name = ?, origin = ?, pSeason = ?, pMonth = ?, purchday = ?,  property = ?,totalprice = ?, stock = ? ,unitPrice = ?WHERE id = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, herb.getName());
            ps.setString(2, herb.getOrigin());
            ps.setString(3, herb.getSeason());
            ps.setInt(4, herb.getMonth());
            ps.setDate(5, Date.valueOf(herb.getPurchday()));
            ps.setString(6, herb.getProperty());
            ps.setDouble(7, herb.getTotalPrice());
            ps.setDouble(8, herb.getStock());
            ps.setDouble(9, herb.getUnitPrice());
            ps.setInt(10, herb.getID());

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        }
    }


    public List<Herb> getAllHerbs() throws SQLException {
        List<Herb> herbs = new ArrayList<>();
        String sql = "SELECT * FROM herb"; // 直接查询herb表

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Herb herb = new Herb();
                // 设置基础属性
                herb.setID(rs.getInt("id"));
                herb.setName(rs.getString("name"));
                herb.setUnitPrice(rs.getDouble("unitPrice"));
                herb.setStock(rs.getInt("stock"));
                herb.setOrigin(rs.getString("origin"));

                // 设置草药特有属性
                herb.setSeason(rs.getString("pSeason"));

                herb.setProperty(rs.getString("property"));


                herb.setDate(rs.getDate("purchday").toLocalDate());
                herb.setMonth(rs.getInt("pMonth"));
                herbs.add(herb);
            }
        } catch (SQLException e) {
            throw new SQLException("草药查询异常: " + e.getMessage());
        }
        return herbs;
    }
    public String getType()
    {
        return "Herb";
    }
}
