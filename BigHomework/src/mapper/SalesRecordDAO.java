package mapper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import products.Product;

public class SalesRecordDAO {
    private static final String URL = "jdbc:mysql://127.0.0.1/jj";
    private static final String USER = "root";
    private static final String PASSWORD = "2856208614";

    public void insertSalesRecord(SalesRecord record) throws SQLException {
        String sql = "INSERT INTO sales_record (sales_time, customer_id, productname,product_id, product_quantity, product_unit_price) VALUES (?, ?, ?, ?, ?,?)";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, record.getSalesTime().toString());
            ps.setInt(2, record.getCustomerID());
            ps.setString(3, record.getProduct().getName());
            ps.setInt(4, record.getProduct().getID());
            ps.setInt(5, record.getProductQuantity());
            ps.setDouble(6, record.getProductUnitPrice());
            ps.executeUpdate();
        }
    }
}