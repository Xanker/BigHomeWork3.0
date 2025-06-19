package mapper;

import products.Product;

import java.time.LocalDate;


public class SalesRecord {
    private LocalDate salesTime;
    private int customerID;
    private Product productname;
    private double productQuantity;
    private double productUnitPrice;

    public SalesRecord(LocalDate salesTime, int customerID, Product product, double productQuantity, double productUnitPrice) {
        this.salesTime = salesTime;
        this.customerID = customerID;
        this.productname = product;
        this.productQuantity = productQuantity;
        this.productUnitPrice = productUnitPrice;
    }

    public LocalDate getSalesTime() {
        return salesTime;
    }

    public int getCustomerID() {
        return customerID;
    }

    public Product getProduct() {
        return productname;
    }

    public int getProductQuantity() {
        return (int) productQuantity;
    }

    public double getProductUnitPrice() {
        return productUnitPrice;
    }

    @Override
    public String toString() {
        return "销售时间: " + salesTime +
                ", 客户ID: " + customerID +
                ", 商品: " + productname.getName() +
                ", 商品数量: " + productQuantity +
                ", 商品单价: " + productUnitPrice;
    }
}
