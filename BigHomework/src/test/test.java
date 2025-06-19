package test;
import mapper.FruitDAO;
import products.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
public class test {
    public static void main(String[] args) throws SQLException {
        ///test1();
        test3();
    }
    public static void test1() {
        LocalDate purchdate = LocalDate.of(2024, 1, 1);
        Wood wood = new Wood("柏树",2,2,2,2,2,purchdate,"china",2);
        System.out.println(wood.getType());
    }
    public static void test2() throws SQLException {
        Fruit apple = new Fruit("苹果", 101, "红色", 2.5, "斤",
                "山东烟台", LocalDate.of(2001,4,2), 8.0);

        Fruit banana = new Fruit("香蕉", 102, "黄色", 6.0, "个",
                "海南三亚", LocalDate.now(), 2.5);
        System.out.println(apple);
        System.out.println(banana);
        FruitDAO fruitDAO = new FruitDAO();
        fruitDAO.insert(apple);
    }
    public static void test3() throws SQLException {
        FruitDAO fruitDAO = new FruitDAO();
        fruitDAO.delete(111);
    }

}
