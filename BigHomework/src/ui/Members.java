package ui;
import products.Inventory;
import products.Product;

import java.util.HashMap;
import java.util.Map;



public class Members {
    private String name;
    private int password;
    private int CustomersID;
    private int Level;
    private double Discount = 0.8;
    private Inventory inventory;
    private Boolean flag;
    // 商品 ID 映射到 商品对象和数量
    private Map<String, Map.Entry<Product, Integer>> cartMap = new HashMap<>();

    

    public Map<String, Map.Entry<Product, Integer>> getCartMap()
    {
        return cartMap;
    }





    // 添加商品到购物车
    public void addProduct(Product product, int quantity) {
        if (cartMap.containsKey(product.getID())) {
            Map.Entry<Product, Integer> entry = cartMap.get(product.getID());
            entry.setValue(entry.getValue() + quantity);
        } else {
            cartMap.put(String.valueOf(product.getID()), new java.util.AbstractMap.SimpleEntry<>(product, quantity));
        }
    }

    // 获取购物车商品数量
    public int getProductCount() {
        return cartMap.size();
    }

    // 获取购物车总价（示例计算）
    public double GetTotalPrice() {
        double total = 0;
        for (Map.Entry<Product, Integer> entry : cartMap.values()) {
            total += entry.getKey().getunitprice() * entry.getValue();
        }
        return total;
    }

    // 后续可补充：删除商品、修改数量等方法
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getCustomersID() {
        return CustomersID;
    }
    public void setCustomersID(int CustomersID) {
        this.CustomersID = CustomersID;
    }
    public int getLevel() {
        return Level;
    }
    public void setLevel(int Level) {
        this.Level = Level;
    }
    public double getDiscount() {
        return Discount;
    }
    public void setDiscount(double Discount) {
        this.Discount = Discount;
    }

    public int getpassword() {

        return password;
    }
    public  void setpassword(String password) {
        this.password = Integer.parseInt(password);
    }

    public void setVerify(boolean flag) {
        this.flag = flag;
    }
    public boolean getVerify() {
        return flag;
    }
}
