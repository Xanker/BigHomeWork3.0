package mapper;
import products.*;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

public abstract class ProductDAO<T extends Product> {
    protected static Connection connection;

    public ProductDAO() throws SQLException {
        String url = "jdbc:mysql://127.0.0.1/jj";
        String name = "root";
        String pass = "2856208614";
        connection = DriverManager.getConnection(url, name, pass);
    }

    protected void insertProductTable(Product product) throws SQLException {
        String sql = "insert into product(id, name, type, price, description, purchday , stock) values(?,?,?,?,?,?,?)";
        PreparedStatement ps = connection.prepareStatement(sql);

        ps.setInt(1, product.getID());
        ps.setString(2, product.getName());
        ps.setString(3, product.getType());
        ps.setDouble(4, product.getunitprice());
        ps.setString(5, product.getDescription());
        ps.setDate(6,Date.valueOf(product.getDate()));
        ps.setInt(7, product.getStock()); // 初始库存为0
        ps.executeUpdate();

        ps.close();
    }

    public ArrayList<Product> loadProductTable() throws SQLException {
        String sql = "SELECT * FROM product p " +
                "LEFT JOIN fruit f on f.id = p.id " +
                "LEFT JOIN wood w ON w.id = p.id " +
                "LEFT JOIN herb d ON d.id = f.id ";

        PreparedStatement ps = connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        ArrayList<Product> products = new ArrayList<>();
        HashMap<T, Integer> productsCount = new HashMap<>();
        while (rs.next()) {
            //Product表
            int id = rs.getInt("id");
            String name = rs.getString("name");
            String type = rs.getString("type");
            double price = rs.getDouble("price");
            String description = rs.getString("description");
            int stock = rs.getInt("stock");

            switch (type) {
                case "Fruit" -> {
                    String color = rs.getString("color");
                    double weight = rs.getDouble("weight");
                    String unit = rs.getString("unit");
                    String origin = rs.getString("origin");
                    double unitPrice = rs.getDouble("unitPrice");
                    LocalDate purchdate = LocalDate.of(2024, 1, 1);
                    Fruit fruit = new Fruit(name,id,color,weight,unit,origin,purchdate,unitPrice);
                    products.add(fruit);
                    productsCount.put((T) fruit, stock);
                }
                case "Wood" -> {
                    double length = rs.getDouble("length");
                    double width = rs.getDouble("width");
                    double thickness = rs.getDouble("height");
                    LocalDate purchdate = rs.getDate("purchday").toLocalDate();
                    String origin = rs.getString("origin");
                    Wood wood = new Wood(name,id,price,length,width,thickness,purchdate,origin,stock);
                    products.add(wood);
                    productsCount.put((T) wood, stock);
                    // 这里需要补充 Wood 的构造函数调用
                }
                case "Herb" -> {
                    String origin = rs.getString("origin");
                    String pSeason = rs.getString("pSeason");
                    String property = rs.getString("property");
                    LocalDate purchdate = rs.getDate("purchday").toLocalDate();
                    Herb herb = new Herb(name,id,origin,pSeason,property,purchdate,price,stock);
                    products.add(herb);
                    productsCount.put((T) herb, stock);
                    // 这里需要补充 Herb 的构造函数调用
                }
            }
        }
        ps.close();
        return products;
    }

    public abstract void insert(T product) throws SQLException;

    public abstract boolean update(T product) throws SQLException;

    public boolean delete(int id) throws SQLException{
        String sql = "DELETE FROM product WHERE id = ?";
       try(PreparedStatement ps = connection.prepareStatement(sql))
       {
           ps.setInt(1, id);
           ps.executeUpdate();
           return true;
       }catch (SQLException e) {
           return false;
       }

    }

    public void updateStock(T product, int count) throws SQLException
    {
        String sql = "UPDATE product SET stock = ? WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, count);
            ps.setInt(2, product.getID());
            ps.executeUpdate();
        }
        if(product.getType().equals("Fruit"))
        {
            String sql1 = "UPDATE fruit SET weight = ? WHERE id = ?";
            PreparedStatement ps1 = connection.prepareStatement(sql1);
            ps1.setInt(1, count);
            ps1.setInt(2, product.getID());
            ps1.executeUpdate();
        }
        else if(product.getType().equals("Wood"))
        {
            String sql1 = "UPDATE wood SET stock = ? WHERE id = ?";
            PreparedStatement ps1 = connection.prepareStatement(sql1);
            ps1.setInt(1, count);
            ps1.setInt(2, product.getID());
            ps1.executeUpdate();
        } else if (product.getType().equals("Herb")) {
            String sql1 = "UPDATE herb SET stock = ? WHERE id = ?";
            PreparedStatement ps1 = connection.prepareStatement(sql1);
            ps1.setInt(1, count);
            ps1.setInt(2, product.getID());
            ps1.executeUpdate();
        }
        checkStock(product.getID());

    }
    public void checkStock(int id) throws SQLException {
        String sql = "SELECT * FROM product p " +
                "LEFT JOIN fruit f on f.id = p.id " +
                "LEFT JOIN wood w ON w.id = p.id ";

        PreparedStatement ps = connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            //Product表

            int stock = rs.getInt("stock");
            if(stock <= 0)
            {
                String str  = "Delete from product where id = ?";
                PreparedStatement ps1 = connection.prepareStatement(str);
                ps1.setInt(1, id);
                ps1.executeUpdate();
            }

        }
        ps.close();

    }

    public Connection getConnection() {
        return connection;
    }
    public String getTypes(int id) throws SQLException {
        String url = "select type from product where id = ?";
        PreparedStatement ps = connection.prepareStatement(url);
        ResultSet rs = ps.executeQuery();

        rs.next();
        return rs.getString("type");
    }
}